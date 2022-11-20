package com.example.common.base

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

abstract class BaseViewModel:ViewModel() {
//    protected abstract val _viewState:MutableStateFlow
//    abstract val viewState: StateFlow<T>
    abstract fun loadState()
}