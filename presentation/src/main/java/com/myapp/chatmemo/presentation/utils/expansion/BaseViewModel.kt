package com.myapp.chatmemo.presentation.utils.expansion

import androidx.lifecycle.ViewModel

/**
 * BaseViewModel
 */
abstract class BaseViewModel : ViewModel() {
    protected fun <T> ViewModelLiveData<T>.postValue(value: T) {
        postValue(value)
    }

    protected fun <T> ViewModelLiveData<T>.setValue(value: T) {
        setValue(value)
    }
}
