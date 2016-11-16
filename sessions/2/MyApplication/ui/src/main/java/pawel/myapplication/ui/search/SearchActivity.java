package pawel.myapplication.ui.search;

import android.animation.Animator;
import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.lookout.plugin.android.Components;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import pawel.myapplication.ui.R;
import pawel.myapplication.ui.R2;
import pawel.myapplication.ui.UiComponent;

import javax.inject.Inject;

public class SearchActivity extends Activity implements SearchActivityScreen {

    @BindView(R2.id.input) EditText mInput;
    @BindView(R2.id.results) RecyclerView mResults;
    @BindView(R2.id.progress) View mProgress;

    private RecyclerView.Adapter mAdapter;

    @Inject SearchPresenter mPresenter;

    private int mCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        ButterKnife.bind(this);

        Components.from(this, UiComponent.class).
            createSearchActivitySubcomponent(new SearchActivityModule(this)).inject(this);

        mInput.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override public void afterTextChanged(Editable s) {
                mPresenter.onInputTextChanged(s.toString());
            }
        });

        mAdapter = new RecyclerView.Adapter<ResultItemHolder>() {
            @Override
            public ResultItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                return new ResultItemHolder(parent);
            }

            @Override
            public void onBindViewHolder(ResultItemHolder holder, int position) {
                mPresenter.onBindItem(holder, position);
            }

            @Override
            public int getItemCount() {
                return mCount;
            }
        };

        mResults.setLayoutManager(new LinearLayoutManager(this));
        mResults.setAdapter(mAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mPresenter.onActivityResume();
    }

    @Override
    public void setCount(int size) {
        mCount = size;
        mAdapter.notifyDataSetChanged();
    }
    @Override
    public void setProgressVisible(boolean visible) {
        if (visible) {
            mProgress.setVisibility(View.VISIBLE);
            mResults.setVisibility(View.GONE);
        } else {
            mResults.setVisibility(View.VISIBLE);
            mProgress
                .animate()
                .alpha(0)
                .setListener(new Animator.AnimatorListener() {
                    @Override public void onAnimationStart(Animator animation) {}
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        mProgress.setVisibility(View.GONE);
                        mProgress.setAlpha(1);
                    }
                    @Override public void onAnimationCancel(Animator animation) {}
                    @Override public void onAnimationRepeat(Animator animation) {}
                })
                .start();
        }
    }

    public class ResultItemHolder extends RecyclerView.ViewHolder implements Item {

        @BindView(R2.id.result_container)
        ViewGroup mContainer;

        @BindView(R2.id.result_button)
        TextView mText;

        @BindView(R2.id.picture)
        ImageView mPicture;

        @BindView(R2.id.progress)
        View mProgress;

        public ResultItemHolder(ViewGroup parent) {
            super(getLayoutInflater().inflate(R.layout.result_item, parent, false));
            ButterKnife.bind(this, itemView);
        }

        @Override
        public void setText(String text, String imageUrl) {
            mText.setText(text);
            Picasso.with(SearchActivity.this).load(imageUrl).into(mPicture);

            mContainer.setVisibility(View.VISIBLE);
            mProgress.setVisibility(View.GONE);
        }

        @Override
        public void setProgress() {
            mContainer.setVisibility(View.GONE);
            mProgress.setVisibility(View.VISIBLE);
        }
    }


}
