package pawel.myapplication.search;

import rx.Observable;

import java.util.List;

public interface Search {

    public Observable<List<ResultEntry>> searchResults(String input, int origin, int limit);

}
