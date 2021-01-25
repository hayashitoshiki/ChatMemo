package com.example.chatmemo.ui.phrase

import androidx.lifecycle.*
import com.example.chatmemo.model.entity.Phrase
import com.example.chatmemo.model.entity.Template
import com.example.chatmemo.model.repository.DataBaseRepository
import kotlinx.coroutines.launch
import kotlin.collections.ArrayList

/**
 * 定型文作成画面_UIロジック
 *
 * @property dataBaseRepository DBアクセス用repository
 */
class FixedPhraseAddViewModel(private val dataBaseRepository: DataBaseRepository) : ViewModel() {

    private var mTemplate = Template(null, "")
    val titleText = MutableLiveData<String>("")
    val phraseText = MutableLiveData<String>("")
    private val _isEnablePhraseSubmitButton = MutableLiveData<Boolean>(false)
    val isPhraseEnableSubmitButton: LiveData<Boolean> = _isEnablePhraseSubmitButton
    private val _isEnableSubmitButton = MutableLiveData<Boolean>(false)
    val isEnableSubmitButton: LiveData<Boolean> = _isEnableSubmitButton
    private val _phraseList = MutableLiveData<ArrayList<Phrase>>(arrayListOf())
    val phraseList: LiveData<ArrayList<Phrase>> = _phraseList
    private val _submitState = MutableLiveData<Boolean>()
    val submitState: LiveData<Boolean> = _submitState
    private val _submitText = MutableLiveData<String>()
    val submitText: LiveData<String> = _submitText


    // 初期化
    fun init(template: Template?) {
        if (template != null) {
            viewModelScope.launch {
                val list = arrayListOf<Phrase>()
                dataBaseRepository.getPhraseByTitle(template.id!!).forEach { list.add(it) }
                mTemplate = dataBaseRepository.getTemplateById(template.id)
                _phraseList.postValue(list)
                titleText.postValue(template.title)
                _submitText.postValue("更新")
            }
        } else {
            _submitText.postValue("追加")
        }
    }

    // 登録
    fun submit() {
        viewModelScope.launch {
            mTemplate.title = titleText.value!!
            var result = false
            if (mTemplate.id == null) {
                if (dataBaseRepository.createTemplate(mTemplate)) {
                    val template2 = dataBaseRepository.getTemplateByTitle(titleText.value!!)
                    phraseList.value!!.forEach { it.templateId = template2.id!! }
                    dataBaseRepository.addPhrase(phraseList.value!!)
                    result = true
                }
            } else {
                if (dataBaseRepository.updateTemplate(mTemplate)) {
                    phraseList.value!!.forEach { it.templateId = mTemplate.id!! }
                    dataBaseRepository.updatePhrase(phraseList.value!!, mTemplate.id!!)
                    result = true
                }
            }
            _submitState.postValue(result)
        }
    }

    // 追加
    fun addPhrase() {
        val templateId =
            if (mTemplate.id != null) {
                mTemplate.id!!
            } else {
                0
            }
        val phrase = Phrase(null, phraseText.value!!,templateId)
        val list = phraseList.value!!
        list.add(phrase)
        _phraseList.postValue(list)
        phraseText.value = ""
        changeSubmitButton()
    }

    // リスト削除
    fun removePhraseList(position: Int) {
        val list = phraseList.value!!
        list.removeAt(position)
        _phraseList.value = list
        changeSubmitButton()
    }

    // 定型文文章追加ボタン
    fun changePhraseSubmitButton(phrase: String) {
        _isEnablePhraseSubmitButton.value = phrase.isNotEmpty()
    }

    // 登録・更新ボタン活性・非活性制御
    fun changeSubmitButton() {
        _isEnableSubmitButton.value = titleText.value!!.isNotEmpty() && _phraseList.value!!.size != 0
    }
}
