package com.example.moviesscreen

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
import com.example.common.domain.models.AppEvent.GoToMovieReviews
import com.example.common.domain.models.CustomResult
import com.example.common.domain.models.FeedItem
import com.example.common.domain.models.FeedItem.Movie
import com.example.common.utils.EventBus
import com.example.feeditemlist.FeedItemEventListener
import com.example.feeditemlist.LayoutType.GRID
import com.example.moviesscreen.databinding.MoviesFragmentLayoutBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MoviesFragment : BaseFragment(), SwipeRefreshLayout.OnRefreshListener, FeedItemEventListener {
    private lateinit var binding: MoviesFragmentLayoutBinding
    private lateinit var moviesViewModel: MoviesViewModel

    @Inject
    lateinit var eventBus: EventBus

    override fun onRefresh() {
        moviesViewModel.loadState()
    }

    override fun onItemClick(feedItem: Movie) {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            eventBus.invokeMovieReviewsEvent(GoToMovieReviews(feedItem.id))
        }
    }

    override fun onFavoriteClick(feedItem: Movie, position: Int) {
        binding.feedItemListView.apply {
            val oldFeedItem = feedItem.deepCopy()
            oldFeedItem.favorite?.let { favorite ->
                viewLifecycleOwner.lifecycleScope.launch {
                    repeatOnLifecycle(Lifecycle.State.STARTED) {
                        if (favorite) {
                            moviesViewModel.removeFromFavorites(feedItem).collect {
                                if (it is CustomResult.Failure<*>) {
                                    notifyItemChanged(feedItem = oldFeedItem, position = position)
                                }
                            }
                        } else {
                            moviesViewModel.addToFavorites(feedItem).collect {
                                if (it is CustomResult.Failure<*>) {
                                    notifyItemChanged(feedItem = oldFeedItem, position = position)
                                }
                            }
                        }
                        setButtonEnabled(true)
                    }
                }
                notifyItemChanged(
                    feedItem = feedItem.copy(favorite = !favorite),
                    position = position
                )
                setButtonEnabled(false)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = MoviesFragmentLayoutBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        moviesViewModel = ViewModelProvider(this)[MoviesViewModel::class.java]
        binding.apply {
            feedItemListView.setup(this@MoviesFragment, GRID)
            moviesFragmentRefreshLayout.setOnRefreshListener(this@MoviesFragment)
            with(moviesViewModel.viewState) {
                viewLifecycleOwner.lifecycleScope.launch {
                    repeatOnLifecycle(Lifecycle.State.STARTED) {
                        collectLatest { viewState ->
                            with(viewState) {
                                moviesFragmentRefreshLayout.apply {
                                    if (loading) {
                                        isRefreshing = true
                                    } else {
                                        movieErrorMessage?.let { showErrorDialog(it) }
                                        moviesList?.let { feedItemListView.submitList(it) }
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