package com.fitbit.myapplication


import android.util.Log
import io.reactivex.Single
import okhttp3.HttpUrl
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException
import java.util.ArrayList


class Search  {

    private val client = OkHttpClient()

    fun searchResults(input: String, origin: Int, limit: Int): Single<List<ResultEntry>> =
        Single.fromCallable { syncSearchResults(input, origin, limit) }


    @Throws(IOException::class, JSONException::class)
    private fun syncSearchResults(input: String, origin: Int, limit: Int): List<ResultEntry> {
        val url =
            HttpUrl.parse("https://en.wikipedia.org/w/api.php?action=query&list=search&formatversion=2&format=json")!!
                .newBuilder()
                .addQueryParameter("srsearch", "morelike:$input")
                .addQueryParameter("sroffset", Integer.toString(origin))
                .addQueryParameter("srlimit", Integer.toString(limit))
                .build()

        Log.i("MyApplication", "requesting: $url")

        val request = Request.Builder()
            .url(url)
            .build()

        val response = client.newCall(request).execute()
        if (!response.isSuccessful) throw IOException("Unexpected code $response")

        val json = JSONObject(response.body()!!.string())
        val searchResultsJson = json.getJSONObject("query").getJSONArray("search")

        val retVal = ArrayList<ResultEntry>()
        for (i in 0 until searchResultsJson.length()) {
            val entryJson = searchResultsJson.getJSONObject(i)
            retVal.add(ResultEntry(entryJson.getString("title"), null))
        }
        return retVal
    }
}
