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
 * ルームの定型文設定変更ダイアログ_ロジック
 * @property dataBaseRepository DB取得リポジトリ
 */
class RoomPhraseEditViewModel(private var chatRoom: ChatRoom, private val dataBaseRepository: DataBaseRepository) : ViewModel() {

    private var mTemplateList = listOf<Template?>()
    private val _roomTitleList = MutableLiveData<List<String>>()
    val roomTitleList: LiveData<List<String>> = _roomTitleList
    val roomTitleValueInt = MutableLiveData<Int>(0)
    val modeValueInt = MutableLiveData<Int>(0)
    private val _isEnableSubmitButton = MutableLiveData<Boolean>(false)
    val isEnableSubmitButton: LiveData<Boolean> = _isEnableSubmitButton

    init {
        viewModelScope.launch {
            val list  = arrayListOf("選択なし")
            val templateList = dataBaseRepository.getPhraseTitle()
            templateList.forEach { list.add(it.title) }
            mTemplateList = templateList
            _roomTitleList.postValue(list)
            chatRoom.templateId?.also {
                var templateTitle: String? = null
                templateList.forEach { template ->
                    if (template.id!! == it) {
                        templateTitle = template.title
                    }
                }
                if (templateTitle != null) {
                    val index = list.indexOf(templateTitle!!)
                    if (index != -1) {
                        roomTitleValueInt.postValue(index)
                    }
                }
            }
            chatRoom.templateMode?.also {
                modeValueInt.postValue(it)
            }
        }
    }

    // 入力バリデート
    fun validate() {
        if (_roomTitleList.value != null && roomTitleValueInt.value != null && modeValueInt.value != null) {
            _isEnableSubmitButton.value = if (roomTitleValueInt.value!! != 0) {
                modeValueInt.value!! != 0 && (mTemplateList[roomTitleValueInt.value!!]!!.id != chatRoom.templateId || modeValueInt.value!! != chatRoom.templateMode)
            } else {
                chatRoom.templateId != null
            }
        }
    }

    // 定型文表示形式の変更
    fun checkedChangeMode(checkedId: Int) {
        modeValueInt.value = checkedId
        validate()
    }

    // 送信
    suspend fun submit() {
        if (roomTitleValueInt.value!! != 0) {
            chatRoom.templateId = mTemplateList[roomTitleValueInt.value!!]!!.id
            chatRoom.templateMode = modeValueInt.value!!
            chatRoom.phrasePoint = null
        } else {
            chatRoom.templateId = null
            chatRoom.templateMode = null
            chatRoom.phrasePoint = null
        }
        dataBaseRepository.updateRoom(chatRoom)
    }
}