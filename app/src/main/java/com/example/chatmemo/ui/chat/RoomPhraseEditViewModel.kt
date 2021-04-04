package com.example.chatmemo.ui.chat

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chatmemo.domain.model.ChatRoom
import com.example.chatmemo.domain.model.Template
import com.example.chatmemo.domain.usecase.ChatUseCase
import com.example.chatmemo.domain.usecase.TemplateUseCase
import com.example.chatmemo.domain.value.TemplateConfiguration
import com.example.chatmemo.domain.value.TemplateId
import com.example.chatmemo.domain.value.TemplateMode
import kotlinx.coroutines.launch

/**
 * ルームの定型文設定変更ダイアログ_ロジック
 * @property templateUseCase templateに関するUseCase
 * @property chatUseCase Chatに関するUseCase
 */
class RoomPhraseEditViewModel(
    private var chatRoomEntity: ChatRoom,
    private val templateUseCase: TemplateUseCase,
    private val chatUseCase: ChatUseCase
) : ViewModel() {

    private val _templateTitleList = MutableLiveData<List<Template>>()
    val templateTitleList: LiveData<List<Template>> = _templateTitleList
    val templateTitleValue = MutableLiveData<String>()
    val templateIndexValue = MutableLiveData<Int>()
    private val _tempalteModeList = MutableLiveData<List<TemplateMode>>()
    val tempalteModeList: LiveData<List<TemplateMode>> = _tempalteModeList
    val modeValue = MutableLiveData<Int>()

    private val _isEnableSubmitButton = MutableLiveData(false)
    val isEnableSubmitButton: LiveData<Boolean> = _isEnableSubmitButton

    init {
        viewModelScope.launch {
            TemplateMode::class.sealedSubclasses

            val modeList = listOf(TemplateMode.Order("順番"), TemplateMode.Randam("ランダム"))
            _tempalteModeList.postValue(modeList)
            val list = arrayListOf(Template(TemplateId(0), "選択なし", listOf()))
            val templateList = templateUseCase.getTemplateAll()
            list.addAll(templateList)
            _templateTitleList.postValue(list)
            chatRoomEntity.templateConfiguration?.also { templateConfiguration ->
                val templateIndex = templateList.indexOfFirst { it.templateId == templateConfiguration.template.templateId }
                templateIndexValue.postValue(templateIndex + 1)
                val modeIndex = modeList.indexOfFirst { it.javaClass == templateConfiguration.templateMode.javaClass }
                modeValue.postValue(modeIndex)
            }
        }
    }

    // 入力バリデート
    fun validate() {
        if (templateTitleList.value != null && templateTitleValue.value != null && modeValue.value != null) {
            val titleIndex = templateIndexValue.value!!
            val modeId = modeValue.value!!
            // TODO : 今と違うかつ、しっかり入力されているか
            _isEnableSubmitButton.value = if (chatRoomEntity.templateConfiguration == null) {
                // 現在、テンプレートが設定されていない場合
                titleIndex != 0 && modeId != 0
            } else {
                // 現在、テンプレートが設定されている場合
                val templateIndexOld = _templateTitleList.value!!.indexOfFirst { it.templateId == chatRoomEntity.templateConfiguration!!.template.templateId } + 1
                val modeIndex = tempalteModeList.value!!.indexOfFirst { it.javaClass == chatRoomEntity.templateConfiguration!!.templateMode.javaClass }
                titleIndex == 0 || !(titleIndex == templateIndexOld && modeId == modeIndex)
            }
        }
    }

    // 送信
    suspend fun submit() {
        if (templateTitleValue.value!! != "選択なし") {
            val template = templateTitleList.value!![templateIndexValue.value!!]
            val templateMode = tempalteModeList.value!![modeValue.value!!]
            val templateConfiguration = TemplateConfiguration(template, templateMode)
            chatRoomEntity.templateConfiguration = templateConfiguration
        } else {
            chatRoomEntity.templateConfiguration = null
        }
        chatUseCase.updateRoom(chatRoomEntity)
    }
}