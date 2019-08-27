package com.fitbit.myapplication

import android.animation.Animator
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import io.reactivex.functions.Consumer
import io.reactivex.plugins.RxJavaPlugins
import kotlinx.android.synthetic.main.activity_search.*

class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: SearchViewModel
    private lateinit var resultsAdater: ResultEntriesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        RxJavaPlugins.setErrorHandler({
            Log.e("MyApplication", "Undelivered error", it)
        })

        setContentView(R.layout.activity_search)

        viewModel = ViewModelProviders.of(this).get(SearchViewModel::class.java)
        viewModel.onCreate()

        results.layoutManager = LinearLayoutManager(this)
        resultsAdater = ResultEntriesAdapter({ position -> viewModel.onEndOfListReached(position)})
        results.adapter = resultsAdater

        input.setText(viewModel.inputString.value)
        input.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable) {
                viewModel.onInputTextChanged(s.toString())
            }
        })
    }

    override fun onResume() {
        super.onResume()

        viewModel.results.observe(this, Observer {  resultsAdater.setResultEntries(it) })
        viewModel.inProgress.observe(this, Observer {
            //resultsAdater.showProgress(it)
            showProgress(it)
        })
    }

    fun showProgress(visible: Boolean) {
        if (visible) {
            progress.setVisibility(View.VISIBLE)
            results.setVisibility(View.GONE)
        } else {
            results.setVisibility(View.VISIBLE)
            progress
                .animate()
                .alpha(0f)
                .setListener(object : Animator.AnimatorListener {
                    override fun onAnimationStart(animation: Animator) {}
                    override fun onAnimationEnd(animation: Animator) {
                        progress.setVisibility(View.GONE)
                        progress.setAlpha(1f)
                    }

                    override fun onAnimationCancel(animation: Animator) {}
                    override fun onAnimationRepeat(animation: Animator) {}
                })
                .start()
        }
    }

}
