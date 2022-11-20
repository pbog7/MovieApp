package com.example.moviereviewsscreen

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.common.SharedViewModel
import com.example.common.base.BaseFragment
import com.example.common.base.BaseNavigationActivity
import com.example.common.domain.models.AppEvent.GoToMovieReviews
import com.example.common.utils.EventBus
import com.example.movieReviews.databinding.MovieReviewsFragmentLayoutBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MovieReviewsFragment : BaseFragment() {
    private lateinit var movieReviewsViewModel: MovieReviewsViewModel
    private lateinit var binding: MovieReviewsFragmentLayoutBinding
    private val sharedViewModel: SharedViewModel by activityViewModels()


    @Inject
    lateinit var movieReviewsAdapter: MovieReviewsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        (activity as BaseNavigationActivity).hideBottomNavigation()
        binding = MovieReviewsFragmentLayoutBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        movieReviewsViewModel = ViewModelProvider(this)[MovieReviewsViewModel::class.java]
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                sharedViewModel.sharedData.collect {
                    movieReviewsViewModel.getMovieReviews(it.movieId)
                }
            }
        }
        binding.apply {
            viewLifecycleOwner.lifecycleScope.launch {
                repeatOnLifecycle(Lifecycle.State.STARTED) {
                    movieReviewsViewModel.viewState.collectLatest { viewState ->
                        with(viewState) {
                            movieReviewsError?.let { showErrorDialog(it) }
                            movieReviews?.let { movieReviewsAdapter.submitList(it) }
                        }
                    }
                }
            }
            movieReviewsRv.adapter = movieReviewsAdapter
            movieReviewsRv.layoutManager =
                LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        }
    }

    override fun onDestroyView() {
        (activity as BaseNavigationActivity).showBottomNavigation()
        Log.d("MovieReviewsFragment","OnDestroyView")
        super.onDestroyView()
    }
}