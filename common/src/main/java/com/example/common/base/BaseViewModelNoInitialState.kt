package com.example.common.base

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

abstract class BaseViewModelNoInitialState : ViewModel() {
//    protected abstract val _viewState: MutableStateFlow<T>
//    abstract val viewState: StateFlow<T>
}