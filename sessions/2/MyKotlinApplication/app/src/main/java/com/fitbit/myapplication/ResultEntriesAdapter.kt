package com.fitbit.myapplication

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import kotlinx.android.extensions.LayoutContainer

import kotlinx.android.synthetic.main.result_item.view.*

class ResultEntriesAdapter(
    private val onLastItemShown: (position: Int) -> Unit

) : RecyclerView.Adapter<ResultItemHolder>()
{
    private var showProgress: Boolean = false
    private var resultEntries: List<ResultEntry> = listOf()

    override fun onBindViewHolder(holder: ResultItemHolder, position: Int) {
        if (position >= resultEntries.size) {
            holder.setProgress()
        } else {
            holder.setText(resultEntries[position].value, resultEntries[position].imageUrl)

            if (position == resultEntries.size - 1) {
                onLastItemShown.invoke(position)
            }
        }
    }

    override fun getItemCount(): Int = resultEntries.size + (if (showProgress) 1 else 0)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ResultItemHolder {
        val inflater = parent.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater;
        return ResultItemHolder(inflater.inflate(R.layout.result_item, parent, false))
    }

    fun setResultEntries(resultEntries: List<ResultEntry>) {
        this.resultEntries = resultEntries
        notifyDataSetChanged()
    }

    fun showProgress(showProgress: Boolean) {
        if (this.showProgress == showProgress) {
            return
        }

        this.showProgress = showProgress

        if (showProgress) {
            notifyItemInserted(resultEntries.size)
        } else {
            notifyItemRemoved(resultEntries.size)
        }
    }
}

class ResultItemHolder(
    override val containerView: View) :
    RecyclerView.ViewHolder(containerView),
    LayoutContainer
{
    fun setText(text: String, imageUrl: String?) {
        itemView.result_button.text = text
        if (imageUrl != null) {
            Picasso.with(containerView.context).load(imageUrl).into(itemView.picture)
        } else {
            // Clear current image in bitmap
            itemView.picture.setImageDrawable(null)
        }

        itemView.result_container.visibility = View.VISIBLE
        itemView.progress.visibility = View.GONE
    }

    fun setProgress() {
        itemView.result_container.visibility = View.GONE
        itemView.progress.visibility = View.VISIBLE
    }
}
