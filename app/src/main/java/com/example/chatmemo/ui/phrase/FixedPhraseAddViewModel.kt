package com.example.chatmemo.ui.phrase

import androidx.lifecycle.*
import com.example.chatmemo.domain.model.Template
import com.example.chatmemo.domain.usecase.TemplateUseCase
import com.example.chatmemo.domain.value.TemplateId
import com.example.chatmemo.domain.value.TemplateMessage
import kotlinx.coroutines.launch

/**
 * 定型文作成画面_UIロジック
 *
 * @property dataBaseRepository DBアクセス用repository
 */
class FixedPhraseAddViewModel(
    private val templateUseCase: TemplateUseCase
) : ViewModel() {

    private var mTemplate = Template(TemplateId(0), "", listOf())

    val titleText = MutableLiveData("")
    val phraseText = MutableLiveData("")
    private val _isEnablePhraseSubmitButton = MediatorLiveData<Boolean>()
    val isPhraseEnableSubmitButton: LiveData<Boolean> = _isEnablePhraseSubmitButton
    private val _isEnableSubmitButton = MediatorLiveData<Boolean>()
    val isEnableSubmitButton: LiveData<Boolean> = _isEnableSubmitButton
    private val _phraseList = MutableLiveData<MutableList<TemplateMessage>>(arrayListOf())
    val phraseList: LiveData<MutableList<TemplateMessage>> = _phraseList

    private val _submitState = MutableLiveData<Boolean>()
    val submitState: LiveData<Boolean> = _submitState
    private val _submitText = MutableLiveData<String>()
    val submitText: LiveData<String> = _submitText

    init {
        _isEnableSubmitButton.addSource(titleText) { changeSubmitButton() }
        _isEnablePhraseSubmitButton.addSource(phraseText) { changePhraseSubmitButton(it) }
    }

    // 初期化
    fun init(template: Template?) {
        if (template != null) {
            viewModelScope.launch {
                mTemplate = template
                val list: MutableList<TemplateMessage> = templateUseCase.getPhraseByTemplateId(
                    template.templateId
                ).toMutableList()
                _phraseList.postValue(list)
                titleText.postValue(template.title)
                _submitText.postValue("更新")
            }
        } else {
            val templateId = templateUseCase.getTemplateId()
            mTemplate = Template(templateId, "", listOf())
            _submitText.postValue("追加")
        }
    }

    // 登録
    fun submit() {
        viewModelScope.launch {

            var result = false
            when (_submitText.value) {
                "追加" -> result = templateUseCase.createTemplate(mTemplate, phraseList.value!!)
                "更新" -> result = templateUseCase.updateTemplate(mTemplate, phraseList.value!!)
            }
            _submitState.postValue(result)
        }
    }

    // 追加
    fun addPhrase() {
        val phrase = TemplateMessage(phraseText.value!!)
        val list = phraseList.value!!
        list.add(phrase)
        _phraseList.postValue(list)
        phraseText.value = ""
        changeSubmitButton()
    }

    // リスト更新
    fun updatePhraseList(items: MutableList<TemplateMessage>) {
        _phraseList.value = items
    }

    // 定型文文章追加ボタン
    private fun changePhraseSubmitButton(phrase: String) {
        _isEnablePhraseSubmitButton.value = phrase.isNotEmpty()
    }

    // 登録・更新ボタン活性・非活性制御
    private fun changeSubmitButton() {
        _isEnableSubmitButton.value = titleText.value!!.isNotEmpty() && _phraseList.value!!.size != 0
    }
}
