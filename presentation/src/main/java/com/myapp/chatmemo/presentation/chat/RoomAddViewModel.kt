package com.myapp.chatmemo.presentation.chat

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import com.myapp.chatmemo.domain.model.entity.ChatRoom
import com.myapp.chatmemo.domain.model.entity.Template
import com.myapp.chatmemo.domain.model.value.Comment
import com.myapp.chatmemo.domain.model.value.TemplateConfiguration
import com.myapp.chatmemo.domain.model.value.TemplateMode
import com.myapp.chatmemo.domain.usecase.ChatUseCase
import com.myapp.chatmemo.domain.usecase.TemplateUseCase
import com.myapp.chatmemo.presentation.utils.expansion.BaseViewModel
import com.myapp.chatmemo.presentation.utils.expansion.ViewModelLiveData

/**
 * 新規ルーム作成画面_ロジック
 * @property templateUseCase templateに関するUseCase
 * @property chatUseCase Chatに関するUseCase
 */
class RoomAddViewModel(
    private val templateUseCase: TemplateUseCase,
    private val chatUseCase: ChatUseCase
) : BaseViewModel() {

    val titleText = MutableLiveData("")
    val templateTitleList: LiveData<List<Template>> = templateUseCase.getSpinnerTemplateAll()
        .asLiveData()
    val templateTitleValue = MutableLiveData<String>()
    val templateModeList = ViewModelLiveData<List<TemplateMode>>()
    val templateModeValue = MediatorLiveData<String>()
    private val _isEnableTemplateMode = MediatorLiveData<Boolean>()
    val isEnableTemplateMode: LiveData<Boolean> = _isEnableTemplateMode
    private val _isEnableSubmitButton = MediatorLiveData<Boolean>()
    val isEnableSubmitButton: LiveData<Boolean> = _isEnableSubmitButton

    init {
        templateModeValue.addSource(templateTitleValue) { changedTemplateTitleValue(it) }
        _isEnableTemplateMode.addSource(templateTitleValue) { changeModeEnable(it) }
        _isEnableSubmitButton.addSource(titleText) { changeSubmitButton() }
        _isEnableSubmitButton.addSource(templateTitleValue) { changeSubmitButton() }
        _isEnableSubmitButton.addSource(templateModeValue) { changeSubmitButton() }
        templateTitleList.observeForever {}

        val modeList = listOf(TemplateMode.Order("順番"), TemplateMode.Randam("ランダム"))
        templateModeList.setValue(modeList)
    }

    // 新規ルーム作成
    suspend fun createRoom(): ChatRoom {
        val roomId = chatUseCase.getNextRoomId()
        val title = titleText.value!!
        val templateConfiguration: TemplateConfiguration?
        val comment = mutableListOf<Comment>()
        val templateTitle = templateTitleValue.value
        val templateList = templateTitleList.value
        val templateMode = templateModeValue.value
        val templateModeList = templateModeList.value!!

        templateConfiguration =
            if (!templateTitle.isNullOrEmpty() && !templateList.isNullOrEmpty() && templateTitle != templateList[0].title) {
                val template = templateList.first { it.title == templateTitle }
                val mode = templateModeList.first { it.massage == templateMode }
                TemplateConfiguration(template, mode)
            } else {
                null
            }

        val room = ChatRoom(roomId, title, templateConfiguration, comment)
        chatUseCase.createRoom(room)
        return room
    }

    // 作成ボタン活性・非活性制御
    fun changeSubmitButton() {
        val title = titleText.value
        val templateTitle = templateTitleValue.value
        val templateMode = templateModeValue.value
        val templateModeNon = templateTitleList.value?.get(0)?.title
        val enable = if (title != null) {
            when {
                title.isEmpty() -> false
                !templateTitle.isNullOrEmpty() && templateTitle != templateModeNon -> !templateMode.isNullOrEmpty()
                else -> true
            }
        } else {
            false
        }
        _isEnableSubmitButton.value = enable
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
