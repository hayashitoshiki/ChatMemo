package com.example.chatmemo.ui.phrase

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chatmemo.domain.model.Template
import com.example.chatmemo.domain.usecase.TemplateUseCase
import kotlinx.coroutines.launch

/**
 * 定型文一覧画面_UIロジック
 *
 * @property dataBaseRepository DBアクセス用repository
 */
class FixedPhraseListViewModel(
    private val templateUseCase: TemplateUseCase
) : ViewModel() {

    private val _phraseList = MutableLiveData<List<Template>>()
    val phraseList: LiveData<List<Template>> = _phraseList
    val status = MutableLiveData<Boolean>()

    // リスト取得
    fun getList() {
        viewModelScope.launch {
            _phraseList.postValue(templateUseCase.getTemplateAll())
        }
    }

    // 定型文リスト削除
    fun deletePhrase(template: Template) {
        viewModelScope.launch {
            status.postValue(null)
            if (templateUseCase.deleteTemplate(template)) {
                status.postValue(true)
            } else {
                status.postValue(false)
            }
        }
    }
}
