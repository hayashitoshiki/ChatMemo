package com.myapp.chatmemo.ui.chat

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import com.myapp.chatmemo.domain.model.entity.ChatRoom
import com.myapp.chatmemo.domain.model.value.TemplateConfiguration
import com.myapp.chatmemo.domain.model.value.TemplateMode
import com.myapp.chatmemo.domain.usecase.ChatUseCase
import com.myapp.chatmemo.domain.usecase.TemplateUseCase
import com.myapp.chatmemo.ui.utils.expansion.BaseViewModel
import com.myapp.chatmemo.ui.utils.expansion.ViewModelLiveData

/**
 * ルームの定型文設定変更ダイアログ_ロジック
 * @property chatRoom 設定を変更するチャットルーム
 * @property templateUseCase templateに関するUseCase
 * @property chatUseCase Chatに関するUseCase
 */
class RoomPhraseEditViewModel(
    private var chatRoom: ChatRoom, private val templateUseCase: TemplateUseCase, private val chatUseCase: ChatUseCase
) : BaseViewModel() {

    val templateTitleList = templateUseCase.getSpinnerTemplateAll()
        .asLiveData()
    val templateTitleValue = MutableLiveData("")
    val templateModeList = ViewModelLiveData<List<TemplateMode>>()
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

        val modeList = listOf(TemplateMode.Order("順番"), TemplateMode.Randam("ランダム"))
        templateModeList.setValue(modeList)
        chatRoom.templateConfiguration?.also { templateConfiguration ->
            templateTitleValue.value = templateConfiguration.template.title
            templateModeValue.value = templateConfiguration.templateMode.massage
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
        val title = templateTitleValue.value
        val mode = templateModeValue.value
        val templateList = templateTitleList.value

        _isEnableSubmitButton.value = when {
            // テンプレートが存在しないされていない場合
            templateList == null || templateList.size == 1 -> false
            // 現在、テンプレートが設定されていない場合
            chatRoom.templateConfiguration == null -> {
                val emptyTitle = templateList[0].title
                title!!.isNotEmpty() && title != emptyTitle && !mode.isNullOrEmpty()
            }
            // 現在、テンプレートが設定されている場合
            else -> {
                val oldTitle = chatRoom.templateConfiguration!!.template.title
                val oldMode = chatRoom.templateConfiguration!!.templateMode.massage
                title == null || (mode != null && !(title == oldTitle && mode == oldMode))
            }
        }
    }

    // テンプレート選択制御
    private fun changedTemplateTitleValue(text: String) {
        if (text.isEmpty() || text == "選択なし") {
            templateModeValue.value = ""
        }
    }

    // テンプレート表示形式選択欄のバリデート
    private fun changeModeEnable(text: String) {
        val result = text.isNotEmpty() && text != "選択なし"
        _isEnableTemplateMode.value = result
    }
}
