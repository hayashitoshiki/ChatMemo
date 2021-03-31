package com.example.chatmemo.ui.chat

import androidx.lifecycle.*
import com.example.chatmemo.model.entity.ChatRoomEntity
import com.example.chatmemo.model.entity.TemplateEntity
import com.example.chatmemo.model.repository.DataBaseRepository
import kotlinx.coroutines.launch

/**
 * 新規ルーム作成画面_ロジック
 * @property dataBaseRepository DB取得リポジトリ
 */
class RoomAddViewModel(
    private val modelist: List<String>, private val dataBaseRepository: DataBaseRepository
) : ViewModel() {

    val titleText = MutableLiveData("")
    private var mTemplateList = listOf<TemplateEntity>()
    private val _templateTitleList = MutableLiveData<List<String>>()
    val templateTitleList: LiveData<List<String>> = _templateTitleList
    val templateTitleValue = MutableLiveData<String>()
    val modeValue = MutableLiveData<String>()
    private val _isEnableSubmitButton = MediatorLiveData<Boolean>()
    val isEnableSubmitButton: LiveData<Boolean> = _isEnableSubmitButton

    init {
        _isEnableSubmitButton.addSource(titleText) { changeSubmitButton() }
        _isEnableSubmitButton.addSource(templateTitleValue) { changeSubmitButton() }
        _isEnableSubmitButton.addSource(modeValue) { changeSubmitButton() }
        viewModelScope.launch {
            val list = arrayListOf("選択なし")
            mTemplateList = dataBaseRepository.getPhraseTitle()
            mTemplateList.forEach { list.add(it.title) }
            _templateTitleList.postValue(list)
        }
    }

    // 新規ルーム作成
    suspend fun createRoom(): ChatRoomEntity {
        val room = ChatRoomEntity(null, titleText.value!!, null, 0, null, null, null)
        if (templateTitleValue.value!! != templateTitleList.value!![0]) {
            val templateId = mTemplateList[templateTitleList.value!!.indexOf(templateTitleValue.value!!) - 1].id
            val mode = modelist.indexOf(modeValue.value!!) + 1
            room.templateId = templateId
            room.templateMode = mode
            room.phrasePoint = null
        } else {
            room.templateId = null
            room.templateMode = null
            room.phrasePoint = null
        }
        dataBaseRepository.createRoom(room)
        return dataBaseRepository.getRoomByTitle(room.title)
    }

    // 作成ボタン活性・非活性制御
    fun changeSubmitButton() {
        _isEnableSubmitButton.postValue(true)
        if (titleText.value != null && templateTitleValue.value != null) {
            _isEnableSubmitButton.postValue(titleText.value!!.isNotEmpty() && ((templateTitleValue.value!! != templateTitleList.value!![0] && modeValue.value != null) || templateTitleValue.value!! == templateTitleList.value!![0]))
        } else {
            _isEnableSubmitButton.postValue(false)
        }
    }
}