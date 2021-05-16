package com.myapp.chatmemo.presentation.template

import androidx.lifecycle.*
import com.myapp.chatmemo.domain.model.entity.Template
import com.myapp.chatmemo.domain.usecase.TemplateUseCase
import com.myapp.chatmemo.presentation.utils.expansion.BaseViewModel
import kotlinx.coroutines.launch

/**
 * 定型文一覧画面_UIロジック
 *
 * @property templateUseCase テンプレートに関するUseCase
 */
class TemplateListViewModel(
    private val templateUseCase: TemplateUseCase
) : BaseViewModel() {

    val templateList: LiveData<List<Template>> = templateUseCase.getTemplateAll()
        .asLiveData()
    val status = MutableLiveData<Boolean>()
    private val _isNoDataText: MediatorLiveData<Boolean> = MediatorLiveData()
    val isNoDataText: LiveData<Boolean> = _isNoDataText

    init {
        _isNoDataText.addSource(templateList) { changeNoDataText(it) }
    }

    // チャットルームなし文言テキスト表示制御
    private fun changeNoDataText(templateList: List<Template>) {
        _isNoDataText.value = templateList.isEmpty()
    }

    // 定型文リスト削除
    fun deletePhrase(template: Template) {
        viewModelScope.launch {
            status.postValue(null)
            val result = templateUseCase.deleteTemplate(template.templateId)
            status.postValue(result)
        }
    }
}
