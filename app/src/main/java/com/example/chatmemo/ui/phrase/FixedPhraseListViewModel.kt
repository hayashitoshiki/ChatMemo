package com.example.chatmemo.ui.phrase

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chatmemo.model.entity.TemplateEntity
import com.example.chatmemo.model.repository.DataBaseRepository
import kotlinx.coroutines.launch

/**
 * 定型文一覧画面_UIロジック
 *
 * @property dataBaseRepository DBアクセス用repository
 */
class FixedPhraseListViewModel(private val dataBaseRepository: DataBaseRepository) : ViewModel() {

    private val _phraseList = MutableLiveData<List<TemplateEntity>>()
    val phraseList: LiveData<List<TemplateEntity>> = _phraseList
    val status = MutableLiveData<Boolean>()

    // リスト取得
    fun getList() {
        viewModelScope.launch {
            _phraseList.postValue(dataBaseRepository.getPhraseTitle())
        }
    }

    // 定型文リスト削除
    fun deletePhrase(template: TemplateEntity) {
        viewModelScope.launch {
            status.postValue(null)
            if (dataBaseRepository.getRoomByTemplateId(template.id!!).isEmpty()) {
                dataBaseRepository.deletePhraseByTitle(template.id)
                dataBaseRepository.deleteTemplateTitle(template)
                _phraseList.postValue(dataBaseRepository.getPhraseTitle())
                status.postValue(true)
            } else {
                status.postValue(false)
            }
        }
    }
}
