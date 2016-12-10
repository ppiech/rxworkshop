package pawel.myapplication.search;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import rx.Observable;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class SeachRemote implements Search {

    private final OkHttpClient client = new OkHttpClient();

    @Inject
    public SeachRemote() {

    }

    @Override
    public Observable<List<ResultEntry>> searchResults(String input, int origin, int limit) {
        return Observable.fromCallable(() -> syncSearchResults(input, origin, limit));
    }

    private List<ResultEntry> syncSearchResults(String input, int origin, int limit) throws IOException, JSONException {
        HttpUrl url = HttpUrl.parse("https://en.wikipedia.org/w/api.php?action=query&list=search&formatversion=2&format=json")
            .newBuilder()
            .addQueryParameter("srsearch", "morelike:" + input)
            .addQueryParameter("sroffset", Integer.toString(origin))
            .addQueryParameter("srlimit", Integer.toString(limit))
            .build();

        //Log.i("MyApplication", "requesting: " + url);

        Request request = new Request.Builder()
            .url(url)
            .build();

        Response response = client.newCall(request).execute();
        if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

        JSONObject json = new JSONObject(response.body().string());
        JSONArray searchResultsJson = json.getJSONObject("query").getJSONArray("search");

        List<ResultEntry> retVal = new ArrayList<>();
        for (int i = 0; i < searchResultsJson.length() ; i++) {
            JSONObject entryJson = searchResultsJson.getJSONObject(i);
            retVal.add(ResultEntry.create(entryJson.getString("title"), null));
        }
        return retVal;
    }
}
