package com.example.chatmemo.ui.chat

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chatmemo.model.entity.ChatRoom
import com.example.chatmemo.model.entity.Template
import com.example.chatmemo.model.repository.DataBaseRepository
import kotlinx.coroutines.launch

/**
 * 新規ルーム作成画面_ロジック
 * @property dataBaseRepository DB取得リポジトリ
 */
class RoomAddViewModel(private val dataBaseRepository: DataBaseRepository) : ViewModel() {

    val titleText = MutableLiveData<String>("")
    private var mTemplateList = listOf<Template>()
    private val _phraseTitleList = MutableLiveData<List<String>>()
    val phraseTitleList: LiveData<List<String>> = _phraseTitleList
    val phraseTitleValueInt = MutableLiveData<Int>(0)
    val modeValueInt = MutableLiveData<Int>(0)
    private val _isEnableSubmitButton = MutableLiveData<Boolean>(false)
    val isEnableSubmitButton: LiveData<Boolean> = _isEnableSubmitButton

    init {
        viewModelScope.launch {
            val list = arrayListOf("選択なし")
            mTemplateList = dataBaseRepository.getPhraseTitle()
            mTemplateList.forEach { list.add(it.title) }
            _phraseTitleList.postValue(list)
        }
    }

    // 新規ルーム作成
    suspend fun createRoom() : ChatRoom {
        val room = ChatRoom(null, titleText.value!!, null, 0, null,null, null)
        if (phraseTitleValueInt.value!! != 0) {
            room.templateId = mTemplateList[phraseTitleValueInt.value!! - 1].id
            room.templateMode = modeValueInt.value!!
            room.phrasePoint = null
        } else {
            room.templateId = null
            room.templateMode = null
            room.phrasePoint = null
        }
        dataBaseRepository.createRoom(room)
        return dataBaseRepository.getRoomByTitle(room.title)
    }

    // 定型文表示形式の変更
    fun checkedChangeMode(checkedId: Int) {
        modeValueInt.value = checkedId
    }

    // 作成ボタン活性・非活性制御
    fun changeSubmitButton() {
        if (titleText.value != null && phraseTitleValueInt.value != null && modeValueInt.value != null) {
            _isEnableSubmitButton.postValue(titleText.value!!.isNotEmpty() && ((phraseTitleValueInt.value!! != 0 && modeValueInt.value!! != 0) || phraseTitleValueInt.value!! == 0))
        } else {
            _isEnableSubmitButton.postValue(false)
        }
    }
}