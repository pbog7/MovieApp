package com.example.common

import androidx.lifecycle.ViewModel
import com.example.common.domain.models.SharedData
import kotlinx.coroutines.flow.MutableSharedFlow

class SharedViewModel:ViewModel() {
    val sharedData = MutableSharedFlow<SharedData>(replay = 1)
}