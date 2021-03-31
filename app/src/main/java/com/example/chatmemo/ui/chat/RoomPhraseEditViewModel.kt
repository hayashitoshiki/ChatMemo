package com.example.chatmemo.ui.chat

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chatmemo.model.entity.ChatRoomEntity
import com.example.chatmemo.model.entity.TemplateEntity
import com.example.chatmemo.model.repository.DataBaseRepository
import kotlinx.coroutines.launch

/**
 * ルームの定型文設定変更ダイアログ_ロジック
 * @property dataBaseRepository DB取得リポジトリ
 */
class RoomPhraseEditViewModel(
    private var chatRoomEntity: ChatRoomEntity,
    private val modelist: List<String>,
    private val dataBaseRepository: DataBaseRepository
) : ViewModel() {

    private var mTemplateList = listOf<TemplateEntity?>()
    private val _templateTitleList = MutableLiveData<List<String>>()
    val templateTitleList: LiveData<List<String>> = _templateTitleList
    val templateTitleValue = MutableLiveData<String>()
    val modeValue = MutableLiveData<String>()
    private val _isEnableSubmitButton = MutableLiveData(false)
    val isEnableSubmitButton: LiveData<Boolean> = _isEnableSubmitButton

    init {
        viewModelScope.launch {
            val list = arrayListOf("選択なし")
            val templateList = dataBaseRepository.getPhraseTitle()
            templateList.forEach { list.add(it.title) }
            mTemplateList = templateList
            _templateTitleList.postValue(list)
            chatRoomEntity.templateId?.also {
                var templateTitle: String? = null
                templateList.forEach { template ->
                    if (template.id!! == it) {
                        templateTitle = template.title
                    }
                }
                if (templateTitle != null) {
                    val index = list.indexOf(templateTitle!!)
                    if (index != -1) {
                        templateTitleValue.postValue(list[index])
                    }
                }
            }
            chatRoomEntity.templateMode?.also {
                modeValue.postValue(modelist[it - 1])
            }
        }
    }

    // 入力バリデート
    fun validate() {
        if (templateTitleList.value != null && templateTitleValue.value != null && modeValue.value != null) {
            val titleList = templateTitleList.value!!
            val title = templateTitleValue.value!!
            val titleIndex = titleList.indexOf(title)
            val modeName = modeValue.value!!
            val modeId = modelist.indexOf(modeName) + 1

            _isEnableSubmitButton.value = if (titleIndex != 0) {
                val templateId = mTemplateList[titleIndex - 1]!!.id
                (templateId != chatRoomEntity.templateId || modeId != chatRoomEntity.templateMode)
            } else {
                chatRoomEntity.templateId != null
            }
        }
    }

    // 送信
    suspend fun submit() {
        if (templateTitleValue.value!! != "選択なし") {
            chatRoomEntity.templateId = mTemplateList[templateTitleList.value!!.indexOf(
                templateTitleValue.value!!
            ) - 1]!!.id
            chatRoomEntity.templateMode = modelist.indexOf(modeValue.value!!)
            chatRoomEntity.phrasePoint = null
        } else {
            chatRoomEntity.templateId = null
            chatRoomEntity.templateMode = null
            chatRoomEntity.phrasePoint = null
        }
        dataBaseRepository.updateRoom(chatRoomEntity)
    }
}