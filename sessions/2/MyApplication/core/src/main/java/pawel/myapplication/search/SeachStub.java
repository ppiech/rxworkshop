package pawel.myapplication.search;

import rx.Observable;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class SeachStub implements Search {

    @Inject
    public SeachStub() {

    }

    @Override
    public Observable<List<ResultEntry>> searchResults(String input, int origin, int limit) {
        List<ResultEntry> retVal = new ArrayList<>();

        for (int i = 0; i < input.length(); i++) {
            retVal.add(ResultEntry.create(input.substring(i), ""));
        }
        return Observable.just(retVal);
    }
}
