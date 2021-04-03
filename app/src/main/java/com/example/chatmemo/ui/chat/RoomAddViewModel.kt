package com.example.chatmemo.ui.chat

import androidx.lifecycle.*
import com.example.chatmemo.domain.model.ChatRoom
import com.example.chatmemo.domain.model.Template
import com.example.chatmemo.domain.usecase.ChatUseCase
import com.example.chatmemo.domain.usecase.TemplateUseCase
import com.example.chatmemo.domain.value.Comment
import com.example.chatmemo.domain.value.TemplateId
import com.example.chatmemo.domain.value.TemplateMode
import kotlinx.coroutines.launch

/**
 * 新規ルーム作成画面_ロジック
 * @property templateUseCase templateに関するUseCase
 * @property chatUseCase Chatに関するUseCase
 */
class RoomAddViewModel(
    private val templateUseCase: TemplateUseCase, private val chatUseCase: ChatUseCase
) : ViewModel() {

    val titleText = MutableLiveData("")
    private val _templateTitleList = MutableLiveData<List<Template>>()
    val templateTitleList: LiveData<List<Template>> = _templateTitleList
    val templateTitleValue = MutableLiveData<Int>()
    private val _tempalteModeList = MutableLiveData<List<TemplateMode>>()
    val tempalteModeList: LiveData<List<TemplateMode>> = _tempalteModeList
    val modeValue = MutableLiveData<Int>()
    private val _isEnableSubmitButton = MediatorLiveData<Boolean>()
    val isEnableSubmitButton: LiveData<Boolean> = _isEnableSubmitButton

    init {
        _isEnableSubmitButton.addSource(titleText) { changeSubmitButton() }
        _isEnableSubmitButton.addSource(templateTitleValue) { changeSubmitButton() }
        _isEnableSubmitButton.addSource(modeValue) { changeSubmitButton() }
        viewModelScope.launch {
            val modeList = listOf(
                TemplateMode.None("選択なし"), TemplateMode.Order("順番"), TemplateMode.Randam("ランダム")
            )
            _tempalteModeList.postValue(modeList)
            val list = arrayListOf(Template(TemplateId(0), "選択なし", listOf()))
            val templateList = templateUseCase.getTemplateAll()
            list.addAll(templateList)
            _templateTitleList.postValue(list)
        }
    }

    // 新規ルーム作成
    suspend fun createRoom(): ChatRoom {
        val roomId = chatUseCase.getNextRoomId()
        val title = titleText.value!!
        val template: Template?
        val templateMode: TemplateMode?
        val comment = mutableListOf<Comment>()

        if (templateTitleValue.value!! != 0) {
            template = templateTitleList.value!![templateTitleValue.value!!]
            templateMode = tempalteModeList.value!![modeValue.value!!]
        } else {
            template = null
            templateMode = null
        }

        val room = ChatRoom(roomId, title, template, templateMode, comment)
        return chatUseCase.createRoom(room)
    }

    // 作成ボタン活性・非活性制御
    fun changeSubmitButton() {
        val enable = if (titleText.value != null && templateTitleValue.value != null) {
            when {
                titleText.value!!.isEmpty()     -> false
                templateTitleValue.value!! != 0 -> modeValue.value!! != 0
                else                            -> true
            }
        } else {
            false
        }
        _isEnableSubmitButton.postValue(enable)
    }
}