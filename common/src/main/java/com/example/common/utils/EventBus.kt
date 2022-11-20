package com.example.common.utils

import com.example.common.domain.models.AppEvent
import com.example.common.domain.models.AppEvent.GoToMovieReviews
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class EventBus @Inject constructor() {
    private val _event = MutableSharedFlow<AppEvent>()
    val event = _event.asSharedFlow()


    suspend fun invokeMovieReviewsEvent(appEvent: GoToMovieReviews) =
        _event.emit(appEvent)
}