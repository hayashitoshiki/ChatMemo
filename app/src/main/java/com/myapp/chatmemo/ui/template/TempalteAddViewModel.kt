package com.myapp.chatmemo.ui.template

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.myapp.chatmemo.domain.model.entity.Template
import com.myapp.chatmemo.domain.model.value.TemplateId
import com.myapp.chatmemo.domain.model.value.TemplateMessage
import com.myapp.chatmemo.domain.usecase.TemplateUseCase
import com.myapp.chatmemo.ui.utils.expansion.BaseViewModel
import com.myapp.chatmemo.ui.utils.expansion.ViewModelLiveData
import kotlinx.coroutines.launch

/**
 * 定型文作成画面_UIロジック
 *
 * @property template DBアクセス用repository
 * @property templateUseCase テンプレート用UseCase
 */
class TempalteAddViewModel(
    private val template: Template?, private val templateUseCase: TemplateUseCase
) : BaseViewModel() {

    val titleText = MutableLiveData("")
    val phraseText = MutableLiveData("")
    private val _isEnablePhraseSubmitButton = MediatorLiveData<Boolean>()
    val isPhraseEnableSubmitButton: LiveData<Boolean> = _isEnablePhraseSubmitButton
    private val _isEnableSubmitButton = MediatorLiveData<Boolean>()
    val isEnableSubmitButton: LiveData<Boolean> = _isEnableSubmitButton
    val phraseList = ViewModelLiveData<MutableList<TemplateMessage>>()
    val submitState = ViewModelLiveData<Boolean>()
    val submitText = ViewModelLiveData<String>()

    init {
        _isEnableSubmitButton.addSource(titleText) { changeSubmitButton() }
        _isEnableSubmitButton.addSource(phraseList) { changeSubmitButton() }
        _isEnablePhraseSubmitButton.addSource(phraseText) { changePhraseSubmitButton(it) }
    }

    // 初期化
    init {
        if (template != null) {
            viewModelScope.launch {
                titleText.postValue(template.title)
                val templateId: TemplateId = template.templateId
                val list = templateUseCase.getTemplateMessageById(templateId).toMutableList()
                phraseList.postValue(list)
                submitText.postValue("更新")
            }
        } else {
            phraseList.postValue(mutableListOf())
            submitText.postValue("追加")
        }
    }

    // 登録
    fun submit() {
        viewModelScope.launch {
            var result = false
            val templateTitle = titleText.value!!
            val phraseList = phraseList.value!!
            when (submitText.value) {
                "追加" -> {
                    val templateId: TemplateId = templateUseCase.getNextTemplateId()
                    val template = Template(templateId, templateTitle, phraseList)
                    result = templateUseCase.createTemplate(template)
                }
                "更新" -> {
                    val templateId = template!!.templateId
                    val template = Template(templateId, templateTitle, phraseList)
                    result = templateUseCase.updateTemplate(template)
                }
            }
            submitState.postValue(result)
        }
    }

    // 追加
    fun addPhrase() {
        val phrase = TemplateMessage(phraseText.value!!)
        val list = phraseList.value!!
        list.add(phrase)
        phraseList.postValue(list)
        phraseText.value = ""
    }

    // リスト更新
    fun updatePhraseList(items: MutableList<TemplateMessage>) {
        phraseList.setValue(items)
    }

    // 定型文文章追加ボタン
    private fun changePhraseSubmitButton(phrase: String) {
        _isEnablePhraseSubmitButton.value = phrase.isNotEmpty()
    }

    // 登録・更新ボタン活性・非活性制御
    private fun changeSubmitButton() {
        val templateTitle = titleText.value
        val messageList = phraseList.value
        _isEnableSubmitButton.value = !templateTitle.isNullOrEmpty() && messageList != null && messageList.size != 0
    }
}
