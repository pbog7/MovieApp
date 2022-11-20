package com.example.common.utils


import android.view.View
import android.view.ViewGroup
import androidx.transition.Slide
import androidx.transition.Transition
import androidx.transition.TransitionManager

fun View.animateSlide(
    viewGroup: ViewGroup,
    gravity: Int,
    visibility: Int,
    durationInMillis: Long = 300,
    transitionListener: Transition.TransitionListener? = null,
) {

    val transition: Transition = Slide(gravity)
    transition.apply {
        duration = durationInMillis
        addTarget(this@animateSlide)
        transitionListener?.let { addListener(it) }
    }
    TransitionManager.beginDelayedTransition(viewGroup, transition)
    this.visibility = visibility
}