package com.example.moviereviewsscreen

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.common.domain.models.MovieReview
import com.example.movieReviews.R
import com.example.movieReviews.databinding.MovieReviewsItemLayoutBinding
import javax.inject.Inject

class MovieReviewsAdapter @Inject constructor() :
    ListAdapter<MovieReview, MovieReviewsAdapter.MovieReviewViewHolder>(reviewsDiffUtil) {
    companion object {
        val reviewsDiffUtil = object : DiffUtil.ItemCallback<MovieReview>() {
            override fun areItemsTheSame(oldItem: MovieReview, newItem: MovieReview): Boolean =
                oldItem == newItem

            override fun areContentsTheSame(oldItem: MovieReview, newItem: MovieReview) =
                oldItem == newItem

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieReviewViewHolder =
        MovieReviewViewHolder(
            MovieReviewsItemLayoutBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    override fun onBindViewHolder(holder: MovieReviewViewHolder, position: Int) =
        holder.bind(getItem(position))

    inner class MovieReviewViewHolder(
        private val binding: MovieReviewsItemLayoutBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(movieReview: MovieReview) {
            with(binding) {
                movieReview.apply {
                    contentText.text = content
                    helpfulText.text = helpful
                    dateText.text = date
                    usernameText.text = username
                    titleText.text = title
                    ratingText.apply {
                        with(context) {
                            text = if (rate.isNotBlank()) {
                                getString(R.string.ratingReview, rate)
                            } else {
                                getString(R.string.unknownRating)
                            }
                        }
                    }
                }
            }
        }

    }

}