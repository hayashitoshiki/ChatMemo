package com.myapp.chatmemo.presentation.chat

import androidx.lifecycle.*
import androidx.savedstate.SavedStateRegistryOwner
import com.myapp.chatmemo.domain.model.entity.ChatRoom
import com.myapp.chatmemo.domain.model.value.TemplateConfiguration
import com.myapp.chatmemo.domain.model.value.TemplateMode
import com.myapp.chatmemo.domain.usecase.ChatUseCase
import com.myapp.chatmemo.domain.usecase.TemplateUseCase
import com.myapp.chatmemo.presentation.utils.expansion.BaseViewModel
import com.myapp.chatmemo.presentation.utils.expansion.ViewModelLiveData
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject

/**
 * ルームの定型文設定変更ダイアログ_ロジック
 * @property chatRoom 設定を変更するチャットルーム
 * @property templateUseCase templateに関するUseCase
 * @property chatUseCase Chatに関するUseCase
 */
class RoomPhraseEditViewModel @AssistedInject constructor(
    @Assisted private var chatRoom: ChatRoom,
    private val templateUseCase: TemplateUseCase,
    private val chatUseCase: ChatUseCase
) : BaseViewModel() {

    @AssistedFactory
    interface RoomPhraseEditViewModelAssistedFactory {
        fun create(chatRoomEntity: ChatRoom): RoomPhraseEditViewModel
    }

    companion object {
        fun provideFactory(
            owner: SavedStateRegistryOwner,
            assistedFactory: RoomPhraseEditViewModelAssistedFactory,
            chatRoomEntity: ChatRoom
        ): ViewModelProvider.Factory = object : AbstractSavedStateViewModelFactory(owner, null) {
            override fun <T : ViewModel?> create(
                key: String,
                modelClass: Class<T>,
                handle: SavedStateHandle
            ): T {
                return assistedFactory.create(chatRoomEntity) as T
            }
        }
    }

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
        val templateTitleList = templateTitleList.value ?: return
        val templateModeList = templateModeList.value ?: return
        val templateModeValue = templateModeValue.value ?: return
        if (templateTitleValue.value != templateTitleList[0].title) {
            val template = templateTitleList.first { it.title == templateTitleValue.value }
            val templateMode = templateModeList.first { it.massage == templateModeValue }
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
        val templateConfiguration = chatRoom.templateConfiguration

        _isEnableSubmitButton.value = when {
            // テンプレートが存在しないされていない場合
            templateList == null || templateList.size == 1 -> false
            // 現在、テンプレートが設定されていない場合
            templateConfiguration == null -> {
                val emptyTitle = templateList[0].title
                !title.isNullOrEmpty() && title != emptyTitle && !mode.isNullOrEmpty()
            }
            // 現在、テンプレートが設定されている場合
            else -> {
                val oldTitle = templateConfiguration.template.title
                val oldMode = templateConfiguration.templateMode.massage
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
