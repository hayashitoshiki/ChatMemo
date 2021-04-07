package com.example.chatmemo.ui.chat

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.example.chatmemo.domain.model.ChatRoom
import com.example.chatmemo.domain.model.Template
import com.example.chatmemo.domain.usecase.ChatUseCase
import com.example.chatmemo.domain.usecase.TemplateUseCase
import com.example.chatmemo.domain.value.Comment
import com.example.chatmemo.domain.value.TemplateConfiguration
import com.example.chatmemo.domain.value.TemplateMode
import com.example.chatmemo.ui.utils.BaseViewModel
import com.example.chatmemo.ui.utils.ViewModelLiveData

/**
 * 新規ルーム作成画面_ロジック
 * @property templateUseCase templateに関するUseCase
 * @property chatUseCase Chatに関するUseCase
 */
class RoomAddViewModel(
    private val templateUseCase: TemplateUseCase, private val chatUseCase: ChatUseCase
) : BaseViewModel() {

    val titleText = MutableLiveData("")
    val templateTitleList: LiveData<List<Template>> = templateUseCase.getSpinnerTemplateAll()
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

        val modeList = listOf(TemplateMode.Order("順番"), TemplateMode.Randam("ランダム"))
        templateModeList.setValue(modeList)
    }

    // 新規ルーム作成
    suspend fun createRoom(): ChatRoom {
        val roomId = chatUseCase.getNextRoomId()
        val title = titleText.value!!
        val templateConfiguration: TemplateConfiguration?
        val comment = mutableListOf<Comment>()

        templateConfiguration = if (!templateTitleValue.value.isNullOrEmpty()) {
            val template = templateTitleList.value!!.first { it.title == templateTitleValue.value!! }
            val templateMode = templateModeList.value!!.first { it.massage == templateModeValue.value!! }
            TemplateConfiguration(template, templateMode)
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
                title.isEmpty()                                                    -> false
                !templateTitle.isNullOrEmpty() && templateTitle != templateModeNon -> !templateMode.isNullOrEmpty()
                else                                                               -> true
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