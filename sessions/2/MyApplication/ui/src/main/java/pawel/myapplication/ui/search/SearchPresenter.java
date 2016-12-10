package pawel.myapplication.ui.search;

import android.util.Log;

import com.lookout.plugin.android.ActivityScope;
import com.lookout.plugin.android.concurrency.Background;
import com.lookout.plugin.android.concurrency.MainLooper;

import pawel.myapplication.search.ResultEntry;
import pawel.myapplication.search.Search;
import rx.Scheduler;
import rx.subjects.BehaviorSubject;
import rx.subjects.PublishSubject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

@ActivityScope
public class SearchPresenter {

    private final SearchActivityScreen mScreen;
    private final Search mSearch;
    private final Scheduler mBackgroundScheduler;
    private final Scheduler mMainScheduler;

    // Pawel
    private List<ResultEntry> mResults = Collections.emptyList();

    @Inject
    public SearchPresenter(SearchActivityScreen screen, Search search, @Background Scheduler bgSched,
        @MainLooper Scheduler mainScheduler)
    {
        mScreen = screen;
        mSearch = search;
        mBackgroundScheduler = bgSched;
        mMainScheduler = mainScheduler;
    }

    private final BehaviorSubject<String> mInputSubject = BehaviorSubject.create();
    private final PublishSubject<PageInfo> mPagingSubject = PublishSubject.create();

    public void onInputTextChanged(String text) {
        mInputSubject.onNext(text);
        mResults = new ArrayList<>();
        mScreen.setCount(0);
    }

    public void onActivityResume() {
        // TODO: should save subscription and cancel it on onDestroy()
        mInputSubject
            //.debounce(4, TimeUnit.SECONDS) // reduce the number of requests to at most 2/sec
            .switchMap(input -> { // ignore search results from previous input
                return mPagingSubject
                    // put delay inside the switchMap will cause the previous query to be canceled immediately when
                    // new input is set.  This is opposed to putting debounce statement before switchmap, which does
                    // not cancel the previous request.
                    .delay(2, TimeUnit.SECONDS)
                    .startWith(new PageInfo(0, new ArrayList<>()))
                    .observeOn(mBackgroundScheduler)
                    .doOnNext(position -> Log.d("MyApplication", "searching: " + input + ", at: " + position))
                    .flatMap(pagePair ->
                        mSearch
                            .searchResults(input, pagePair.position, 3)
                            .map(results -> {
                                List<ResultEntry> newList = new ArrayList<>(pagePair.results);
                                newList.addAll(results);
                                return Collections.unmodifiableList(newList);
                            }));
                }

            )
            .observeOn(mMainScheduler)
            .doOnError(error -> Log.e("MyApplication", error.getMessage()))
            .onErrorReturn(error -> Collections.emptyList())
            .subscribe(results -> {
                Log.d("MyApplication", "results = " + results);
                mResults = results;
                mScreen.setCount(mResults.size() + 1);
                mScreen.setProgressVisible(false);
            });
    }

    void onBindItem(SearchActivityScreen.Item item, int position) {
        if (position < mResults.size()) {
            ResultEntry entry = mResults.get(position);
            item.setText(entry.value(), entry.imageUrl());
            Log.d("MyApplication", "binding: " + position + " => " + entry.value());
        }
        else if (position != 0 && position == mResults.size()) {
            item.setProgress();
            // fetch next page
            onEndOfPage(position);
            onEndOfPage(position);
            Log.d("MyApplication", "binding: " + position + " => spinner");
        }
    }

    private void onEndOfPage(int position) {
        mPagingSubject.onNext(new PageInfo(position, mResults));

    }

}

class PageInfo {

    PageInfo(int position, List<ResultEntry> results) {
        this.position = position;
        this.results = results;
    }

    List<ResultEntry> results;
    int position;
}