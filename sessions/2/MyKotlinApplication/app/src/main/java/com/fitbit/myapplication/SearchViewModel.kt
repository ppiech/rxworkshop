package com.fitbit.myapplication

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.LiveDataReactiveStreams
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.BackpressureStrategy
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject
import java.util.concurrent.TimeUnit

class SearchViewModel(private val search: Search = Search()) : ViewModel() {

    var initialized = false

    private val inputSubject: BehaviorSubject<String> = BehaviorSubject.createDefault("")
    private val pagingSubject: PublishSubject<Int> = PublishSubject.create()

    private val searchResults = inputSubject
        .debounce(2, TimeUnit.SECONDS)
        .switchMap { input ->
            pagingSubject
                .startWith(0)
                .observeOn(Schedulers.io())
                .flatMap { position -> search.searchResults(input, position, 10).toObservable() }
        }
        .scan(
            listOf<ResultEntry>(),
            { combined, lastSearch-> combined + lastSearch }
        )
        .observeOn(AndroidSchedulers.mainThread())
        .doOnError { error -> Log.e("MyApplication", error.message) }
        .onErrorReturn { emptyList() }
        .cache()

    fun onCreate() {
        if (initialized) return

    }

    fun onInputTextChanged(input: String) {
        inputSubject.onNext(input)
    }

    fun onEndOfListReached(position: Int) {
        pagingSubject.onNext(position)
    }

    val inputString: LiveData<String> by lazy {
        LiveDataReactiveStreams.fromPublisher(
            inputSubject.toFlowable(BackpressureStrategy.LATEST)
        )
    }

    val inProgress: LiveData<Boolean> by lazy {
        LiveDataReactiveStreams.fromPublisher(
            Observable
                .merge(
                    inputSubject.map { true },
                    searchResults.map {false })
                .toFlowable(BackpressureStrategy.LATEST)
        )
    }

    val results: LiveData<List<ResultEntry>> by lazy {
        LiveDataReactiveStreams.fromPublisher(
            searchResults.toFlowable(BackpressureStrategy.LATEST)
        )
    }
}