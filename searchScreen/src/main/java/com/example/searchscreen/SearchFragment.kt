package com.example.searchscreen

import android.app.SearchManager
import android.content.Context
import android.database.Cursor
import android.os.Bundle
import android.provider.SearchRecentSuggestions
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.common.base.BaseFragment
import com.example.common.domain.models.AppEvent
import com.example.common.domain.models.CustomResult
import com.example.common.domain.models.FeedItem
import com.example.common.utils.EventBus
import com.example.feeditemlist.FeedItemEventListener
import com.example.feeditemlist.LayoutType
import com.example.searchscreen.databinding.SearchFragmentLayoutBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class SearchFragment : BaseFragment(), FeedItemEventListener, SearchView.OnQueryTextListener,
    SearchView.OnSuggestionListener {
    private lateinit var searchViewModel: SearchViewModel
    private lateinit var binding: SearchFragmentLayoutBinding
    private lateinit var suggestions: SearchRecentSuggestions
    private lateinit var searchManager: SearchManager
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = SearchFragmentLayoutBinding.inflate(inflater, container, false)
        return binding.root
    }

    @Inject
    lateinit var eventBus: EventBus
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        searchViewModel = ViewModelProvider(this)[SearchViewModel::class.java]
        suggestions = SearchRecentSuggestions(
            activity,
            MoviesSearchSuggestionsProvider.AUTHORITY, MoviesSearchSuggestionsProvider.MODE
        )
        searchManager = requireActivity().getSystemService(Context.SEARCH_SERVICE) as SearchManager
        binding.apply {
            searchResultsItemList.setup(this@SearchFragment, LayoutType.LINEAR_VERTICAL)
            searchView.apply {
                setSearchableInfo(searchManager.getSearchableInfo(requireActivity().componentName))
                setOnQueryTextListener(this@SearchFragment)
                setOnSuggestionListener(this@SearchFragment)
            }

            viewLifecycleOwner.lifecycleScope.launch {
                repeatOnLifecycle(Lifecycle.State.STARTED) {
                    searchViewModel.viewState.collectLatest { viewState ->
                        with(viewState) {
                            progressBar.apply {
                                if (loading) {
                                    visibility = View.VISIBLE
                                } else {
                                    visibility = View.GONE
                                    searchError?.let { showErrorDialog(it) }
                                    searchResults?.let { searchResultsItemList.submitList(it) }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    override fun onItemClick(feedItem: FeedItem.Movie) {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            eventBus.invokeMovieReviewsEvent(AppEvent.GoToMovieReviews(feedItem.id))
        }
    }

    override fun onFavoriteClick(feedItem: FeedItem.Movie, position: Int) {
        binding.searchResultsItemList.apply {
            val oldFeedItem = feedItem.deepCopy()
            oldFeedItem.favorite?.let { favorite ->
                viewLifecycleOwner.lifecycleScope.launchWhenStarted {
                    if (favorite) {
                        searchViewModel.removeFromFavorites(feedItem).collect {
                            if (it is CustomResult.Failure<*>) {
                                notifyItemChanged(feedItem = oldFeedItem, position = position)
                            }
                        }
                    } else {
                        searchViewModel.addToFavorites(feedItem).collect {
                            if (it is CustomResult.Failure<*>) {
                                notifyItemChanged(feedItem = oldFeedItem, position = position)
                            }
                        }
                    }
                    setButtonEnabled(true)
                }
                notifyItemChanged(
                    feedItem = feedItem.copy(favorite = !favorite),
                    position = position
                )
                setButtonEnabled(false)
            }
        }

    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        query?.let {
            searchViewModel.searchMovie(it)
            suggestions.saveRecentQuery(it, null)
        }
        return false
    }

    override fun onQueryTextChange(newText: String?): Boolean = false
    override fun onSuggestionSelect(position: Int): Boolean = true
    override fun onSuggestionClick(position: Int): Boolean {
        binding.searchView.apply {
            val cursor: Cursor = suggestionsAdapter.cursor
            cursor.moveToPosition(position)
            val suggestion = cursor.getString(2)
            setQuery(suggestion, true)
            return true
        }
    }
}