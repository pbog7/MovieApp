package com.example.favoritesscreen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.common.base.BaseFragment
import com.example.common.domain.models.AppEvent
import com.example.common.domain.models.CustomResult
import com.example.common.domain.models.FeedItem
import com.example.common.domain.models.FeedItem.Movie
import com.example.common.utils.EventBus
import com.example.favoritesscreen.databinding.FavoritesFragmentLayoutBinding
import com.example.feeditemlist.FeedItemEventListener
import com.example.feeditemlist.LayoutType
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class FavoritesFragment : BaseFragment(), SwipeRefreshLayout.OnRefreshListener,
    FeedItemEventListener {
    private lateinit var binding: FavoritesFragmentLayoutBinding
    private lateinit var favoritesViewModel: FavoritesViewModel

    @Inject
    lateinit var eventBus: EventBus
    override fun onRefresh() {
        favoritesViewModel.loadState()
    }

    override fun onItemClick(feedItem: Movie) {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                eventBus.invokeMovieReviewsEvent(AppEvent.GoToMovieReviews(feedItem.id))
            }
        }
    }

    override fun onFavoriteClick(feedItem: Movie, position: Int) {
        binding.feedItemListView.apply {
            val oldFeedItem = feedItem.deepCopy()
            viewLifecycleOwner.lifecycleScope.launch {
                repeatOnLifecycle(Lifecycle.State.STARTED) {
                    favoritesViewModel.removeFromFavorites(feedItem).collect {
                        if (it is CustomResult.Failure<*>) {
                            notifyItemChanged(feedItem = oldFeedItem, position = position)
                        }
                    }
                    setButtonEnabled(true)
                }
            }
            setButtonEnabled(false)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FavoritesFragmentLayoutBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        favoritesViewModel = ViewModelProvider(this)[FavoritesViewModel::class.java]
        binding.apply {
            feedItemListView.setup(this@FavoritesFragment, LayoutType.GRID)
            moviesFragmentRefreshLayout.setOnRefreshListener(this@FavoritesFragment)
            with(favoritesViewModel.viewState) {
                viewLifecycleOwner.lifecycleScope.launch {
                    repeatOnLifecycle(Lifecycle.State.STARTED) {
                        collectLatest { viewState ->
                            with(viewState) {
                                moviesFragmentRefreshLayout.apply {
                                    if (loading) {
                                        isRefreshing = true
                                    } else {
                                        favoritesErrorMessage?.let { showErrorDialog(it) }
                                        favorites?.let { feedItemListView.submitList(it) }
                                        isRefreshing = false
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}