package com.example.chatmemo.ui.chat

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.chatmemo.domain.model.ChatRoom
import com.example.chatmemo.domain.model.Template
import com.example.chatmemo.domain.usecase.ChatUseCase
import com.example.chatmemo.domain.usecase.TemplateUseCase
import com.example.chatmemo.domain.value.TemplateConfiguration
import com.example.chatmemo.domain.value.TemplateId
import com.example.chatmemo.domain.value.TemplateMode
import com.example.chatmemo.ui.utils.BaseViewModel
import com.example.chatmemo.ui.utils.ViewModelLiveData
import kotlinx.coroutines.launch

/**
 * ルームの定型文設定変更ダイアログ_ロジック
 * @property templateUseCase templateに関するUseCase
 * @property chatUseCase Chatに関するUseCase
 */
class RoomPhraseEditViewModel(
    private var chatRoom: ChatRoom,
    private val templateUseCase: TemplateUseCase,
    private val chatUseCase: ChatUseCase
) : BaseViewModel() {

    val templateTitleList: ViewModelLiveData<List<Template>> = ViewModelLiveData<List<Template>>()
    val templateTitleValue = MutableLiveData<String>()
    val templateModeList: ViewModelLiveData<List<TemplateMode>> = ViewModelLiveData<List<TemplateMode>>()
    val templateModeValue = MediatorLiveData<String>()
    private val _isEnableTemplateMode = MediatorLiveData<Boolean>()
    val isEnableTemplateMode: LiveData<Boolean> = _isEnableTemplateMode
    private val _isEnableSubmitButton = MediatorLiveData<Boolean>()
    val isEnableSubmitButton: LiveData<Boolean> = _isEnableSubmitButton

    init {
        templateModeValue.addSource(templateTitleValue) { changedTemplateTitleValue(it) }
        _isEnableTemplateMode.addSource(templateTitleValue) { changeModeEnable(it) }
        _isEnableSubmitButton.addSource(templateTitleValue) { changeSubmitButton() }
        _isEnableSubmitButton.addSource(templateModeValue) { changeSubmitButton() }
        viewModelScope.launch {
            val modeList = listOf(TemplateMode.Order("順番"), TemplateMode.Randam("ランダム"))
            templateModeList.postValue(modeList)
            val list = arrayListOf(Template(TemplateId(0), "選択なし", listOf()))
            val templateList = templateUseCase.getTemplateAll()
            list.addAll(templateList)
            templateTitleList.postValue(list)
            chatRoom.templateConfiguration?.also { templateConfiguration ->
                templateTitleValue.postValue(templateConfiguration.template.title)
                templateModeValue.postValue(templateConfiguration.templateMode.massage)
            }
        }
    }

    // 送信
    suspend fun submit() {
        if (templateTitleValue.value!! != templateTitleList.value!![0].title) {
            val template = templateTitleList.value!!.first { it.title == templateTitleValue.value!! }
            val templateMode = templateModeList.value!!.first { it.massage == templateModeValue.value!! }
            val templateConfiguration = TemplateConfiguration(template, templateMode)
            chatRoom.templateConfiguration = templateConfiguration
        } else {
            chatRoom.templateConfiguration = null
        }
        chatUseCase.updateRoom(chatRoom)
    }

    // 変更ボタンのバリデート
    private fun changeSubmitButton() {
        val templateTitle = templateTitleValue.value
        val templateMode = templateModeValue.value

        _isEnableSubmitButton.value = if (chatRoom.templateConfiguration == null) {
            // 現在、テンプレートが設定されていない場合
            templateTitleList.value != null && templateTitle != templateTitleList.value!![0].title && templateMode != null
        } else {
            // 現在、テンプレートが設定されている場合
            val oldTemplateTitle = chatRoom.templateConfiguration!!.template.title
            val oldTemplateMode = chatRoom.templateConfiguration!!.templateMode.massage
            templateTitle == null || (templateMode != null && !(templateTitle == oldTemplateTitle && templateMode == oldTemplateMode))
        }
    }

    // テンプレート選択制御
    private fun changedTemplateTitleValue(text: String) {
        if (text.isEmpty() || text == templateTitleList.value!![0].title) {
            templateModeValue.value = ""
        }
    }

    // テンプレート表示形式選択欄のバリデート
    private fun changeModeEnable(text: String) {
        val result = text.isNotEmpty() && text != templateTitleList.value!![0].title
        _isEnableTemplateMode.value = result
    }
}