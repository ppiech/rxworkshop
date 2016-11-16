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
    private final PublishSubject<Integer> mPagingSubject = PublishSubject.create();

    public void onInputTextChanged(String text) {
        mInputSubject.onNext(text);
        mResults = new ArrayList<>();
        mScreen.setCount(0);
        mScreen.setProgressVisible(true);
    }

    public void onActivityResume() {
        mInputSubject
            .debounce(2, TimeUnit.SECONDS)
            .switchMap(input ->
                mPagingSubject
                    .startWith(0)
                    .observeOn(mBackgroundScheduler)
                    .flatMap(position -> mSearch.searchResults(input, position,  10))
            )
            .observeOn(mMainScheduler)
            .doOnError(error -> Log.e("MyApplication", error.getMessage()))
            .onErrorReturn(error -> Collections.emptyList())
            .subscribe(results -> {
                mResults.addAll(results);
                mScreen.setCount(mResults.size() + 1);
                mScreen.setProgressVisible(false);
            });
    }

    void onBindItem(SearchActivityScreen.Item item, int position) {
        if (position < mResults.size()) {
            ResultEntry entry = mResults.get(position);
            item.setText(entry.value(), entry.imageUrl());
        }
        else if (position != 0 && position == mResults.size()) {
            item.setProgress();
            // fetch next page
            mPagingSubject.onNext(position);
        }
    }

}
