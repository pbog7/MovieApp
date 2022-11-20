package com.example.common.utils

import androidx.transition.Transition

class TransitionListener(
    private val onStartBlock: (() -> Unit)? = null,
    private val onEndBlock: (() -> Unit)? = null,
    private val onCancelBlock: (() -> Unit)? = null,
    private val onPauseBlock: (() -> Unit)? = null,
    private val onResumeBlock: (() -> Unit)? = null,
) : Transition.TransitionListener {
    override fun onTransitionStart(transition: Transition) {
        onStartBlock?.invoke()
    }

    override fun onTransitionEnd(transition: Transition) {
        onEndBlock?.invoke()
    }

    override fun onTransitionCancel(transition: Transition) {
        onCancelBlock?.invoke()
    }

    override fun onTransitionPause(transition: Transition) {
        onPauseBlock?.invoke()
    }

    override fun onTransitionResume(transition: Transition) {
        onResumeBlock?.invoke()
    }
}