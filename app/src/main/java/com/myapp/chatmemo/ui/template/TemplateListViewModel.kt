package com.myapp.chatmemo.ui.template

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.myapp.chatmemo.domain.model.entity.Template
import com.myapp.chatmemo.domain.usecase.TemplateUseCase
import com.myapp.chatmemo.ui.utils.expansion.BaseViewModel
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

    // 定型文リスト削除
    fun deletePhrase(template: Template) {
        viewModelScope.launch {
            status.postValue(null)
            val result = templateUseCase.deleteTemplate(template.templateId)
            status.postValue(result)
        }
    }
}
