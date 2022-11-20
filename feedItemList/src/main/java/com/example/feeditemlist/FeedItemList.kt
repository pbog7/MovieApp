package com.example.feeditemlist

import android.content.Context
import android.util.AttributeSet
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.common.domain.models.FeedItem
import com.example.feeditemlist.LayoutType.GRID
import com.example.feeditemlist.LayoutType.LINEAR_VERTICAL
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class FeedItemList @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : RecyclerView(context, attrs, defStyleAttr) {
    @Inject
    lateinit var feedItemListAdapter: FeedItemListAdapter
    fun notifyItemChanged(position: Int, feedItem: FeedItem) {
        feedItemListAdapter.notifyItemChanged(position, feedItem)
    }
    fun setButtonEnabled(buttonEnabled:Boolean){
        feedItemListAdapter.buttonClickEnabled = buttonEnabled
    }
    fun submitList(items: List<FeedItem>) = feedItemListAdapter.submitList(items)

    fun setup(feedItemListeners: FeedItemEventListener, layoutType: LayoutType) {
        feedItemListAdapter.apply {
            actionListener = feedItemListeners
        }
        this.apply {
            layoutManager = when (layoutType) {
                GRID -> GridLayoutManager(context, 3)
                LINEAR_VERTICAL -> LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            }
            adapter = feedItemListAdapter
        }
    }
}