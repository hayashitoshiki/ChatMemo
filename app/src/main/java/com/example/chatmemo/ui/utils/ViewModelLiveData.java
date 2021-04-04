package com.example.chatmemo.ui.utils;


import androidx.lifecycle.LiveData;

/**
 * カスタムLiveData
 * カプセル化のためJavaクラスで生成
 *
 * @param <T>
 */
public class ViewModelLiveData<T> extends LiveData<T> {
    @Override
    protected void postValue(T value) {
        super.postValue(value);
    }

    @Override
    protected void setValue(T value) {
        super.setValue(value);
    }
}