package com.myapp.chatmemo.presentation.template

import androidx.lifecycle.*
import androidx.savedstate.SavedStateRegistryOwner
import com.myapp.chatmemo.domain.model.entity.Template
import com.myapp.chatmemo.domain.model.value.TemplateId
import com.myapp.chatmemo.domain.model.value.TemplateMessage
import com.myapp.chatmemo.domain.usecase.TemplateUseCase
import com.myapp.chatmemo.presentation.utils.expansion.BaseViewModel
import com.myapp.chatmemo.presentation.utils.expansion.ViewModelLiveData
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.launch

/**
 * 定型文作成画面_UIロジック
 *
 * @property template DBアクセス用repository
 * @property templateUseCase テンプレート用UseCase
 */
class TempalteAddViewModel @AssistedInject constructor(
    @Assisted private val template: Template?,
    private val templateUseCase: TemplateUseCase
) : BaseViewModel() {

    @AssistedFactory
    interface TempalteAddViewModelAssistedFactory {
        fun create(template: Template?): TempalteAddViewModel
    }

    companion object {
        fun provideFactory(
            owner: SavedStateRegistryOwner,
            assistedFactory: TempalteAddViewModelAssistedFactory,
            template: Template?
        ): ViewModelProvider.Factory = object : AbstractSavedStateViewModelFactory(owner, null) {
            override fun <T : ViewModel?> create(
                key: String,
                modelClass: Class<T>,
                handle: SavedStateHandle
            ): T {
                return assistedFactory.create(template) as T
            }
        }
    }

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
                val list = templateUseCase.getTemplateMessageById(templateId)
                    .toMutableList()
                phraseList.postValue(list)
                submitText.postValue("更新")
            }
        } else {
            phraseList.postValue(mutableListOf())
            submitText.postValue("追加")
        }
    }

    /**
     * テンプレート登録
     *
     */
    fun submit() {
        viewModelScope.launch {
            var result = false
            val templateTitle = titleText.value ?: return@launch
            val phraseList = phraseList.value ?: return@launch
            when (submitText.value) {
                "追加" -> {
                    val templateId: TemplateId = templateUseCase.getNextTemplateId()
                    val template = Template(templateId, templateTitle, phraseList)
                    result = templateUseCase.createTemplate(template)
                }
                "更新" -> {
                    val templateId = template?.templateId ?: return@launch
                    val template = Template(templateId, templateTitle, phraseList)
                    result = templateUseCase.updateTemplate(template)
                }
            }
            submitState.postValue(result)
        }
    }

    /**
     * テンプレート文言追加
     *
     */
    fun addPhrase() {
        val phrase = TemplateMessage(phraseText.value ?: return)
        val list = phraseList.value ?: return
        list.add(phrase)
        phraseList.postValue(list)
        phraseText.value = ""
    }

    /**
     * テンプレート文言リスト更新
     *
     * @param item
     */
    fun updatePhraseList(item: TemplateMessage) {
        val phraseListTmp = phraseList.value
        phraseListTmp?.let {
            it.remove(item)
            phraseList.setValue(it)
        }
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
