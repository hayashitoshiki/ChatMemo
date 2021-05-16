package com.myapp.chatmemo.data.repository

import com.myapp.chatmemo.data.database.entity.TemplateMessageEntity
import com.myapp.chatmemo.domain.model.entity.Template
import com.myapp.chatmemo.domain.model.value.TemplateId
import com.myapp.chatmemo.domain.model.value.TemplateMessage
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class LocalTemplateRepositoryImpTest {

    // region Coroutine関連

    @ExperimentalCoroutinesApi
    private val testDispatcher = TestCoroutineDispatcher()

    // endregoin

    // mock
    private lateinit var repository: LocalTemplateRepositoryImp
    private lateinit var templateDao: com.myapp.chatmemo.data.database.dao.TemplateDao
    private lateinit var phraseDao: com.myapp.chatmemo.data.database.dao.PhraseDao

    // data
    private val templateTableSize = 3
    private val templateMessageEntity1 = TemplateMessageEntity(1, "message1", 1)
    private val templateMessageEntity2 = TemplateMessageEntity(2, "message2", 1)
    private val templateMessageEntityList = listOf(templateMessageEntity1, templateMessageEntity2)
    private val templateMessage1 = TemplateMessage("message1")
    private val templateMessage2 = TemplateMessage("message2")
    private val templateMessageList = listOf(templateMessage1, templateMessage2)
    private val template = Template(TemplateId(1), "Template1", templateMessageList)
    private val templateList = listOf(Template(TemplateId(1), "Template1", listOf()))
    private val templateEntity = Converter.templateEntityFromTemplate(template)
    private val templateEntityList = listOf(templateEntity)

    @ExperimentalCoroutinesApi
    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        templateDao = mockk<com.myapp.chatmemo.data.database.dao.TemplateDao>().also {
            coEvery { it.getNextId() } returns templateTableSize.toLong()
            coEvery { it.insert(any()) } returns Unit
            coEvery { it.update(any()) } returns Unit
            coEvery { it.getTemplateById(any()) } returns templateEntity
            coEvery { it.deleteByTemplateId(any()) } returns Unit
            coEvery { it.getAll() } returns flow { emit(templateEntityList) }
        }
        phraseDao = mockk<com.myapp.chatmemo.data.database.dao.PhraseDao>().also {
            coEvery { it.insert(any()) } returns Unit
            coEvery { it.delete(any()) } returns Unit
            coEvery { it.deleteByTemplateId(any()) } returns Unit
            coEvery { it.getAllByTitle(any()) } returns templateMessageEntityList
        }
        repository = LocalTemplateRepositoryImp(templateDao, phraseDao, testDispatcher)
    }

    @ExperimentalCoroutinesApi
    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    // region 次のテンプレート文連番取得

    /**
     * テンプレートIDの次の連番取得
     *
     * 条件；なし
     * 結果：DBから取得した最後の連番の次の連番を返すこと
     */
    @Test
    fun getNextTemplateId() {
        runBlocking {
            val result = repository.getNextTemplateId().value
            assertEquals(templateTableSize + 1, result)
        }
    }

    // endregion

    // region テンプレート文登録

    /**
     * テンプレート文登録
     *
     * 条件；なし
     * 結果：引数とした渡したテンプレートがテンプレートタイトルテーブルとテンプレートメッセージテーブルに登録されること
     */
    @Test
    fun createTemplate() {
        runBlocking {
            repository.createTemplate(template)
            coVerify(exactly = 1) { (templateDao).insert(any()) }
            coVerify(exactly = template.templateMessageList.size) { (phraseDao).insert(any()) }
        }
    }

    // endregion

    // region テンプレート文更新

    /**
     * テンプレート文更新
     *
     * 条件；なし
     * 結果：引数とした渡したテンプレートがテンプレートタイトルテーブルとテンプレートメッセージテーブルに登録されること
     */
    @Test
    fun updateTemplate() {
        runBlocking {
            repository.updateTemplate(template)
            coVerify(exactly = 1) { (templateDao).update(any()) }
            coVerify(exactly = template.templateMessageList.size) { (phraseDao).insert(any()) }
            coVerify(exactly = 1) { (phraseDao).deleteByTemplateId(template.templateId.value) }
        }
    }

    // endregion

    // region テンプレート文削除

    /**
     * テンプレート文削除
     *
     * 条件；なし
     * 結果：
     * ・引数とした渡したテンプレートIDに紐づくがテンプレートタイトルテーブルが削除されるメソッドが呼ばれること
     * ・引数として渡したテンプレートIDに紐づくテンプレートメッセージテーブルが削除されるメソッドが呼ばれること
     */
    @Test
    fun deleteTemplate() {
        runBlocking {
            repository.deleteTemplate(template.templateId)
            val id = template.templateId.value
            coVerify(exactly = 1) { (templateDao).deleteByTemplateId(id) }
            coVerify(exactly = 1) { (phraseDao).deleteByTemplateId(id) }
        }
    }

    // endregion

    // region テンプレート一覧取得

    /**
     * テンプレート一覧リスト取得
     *
     * 条件；なし
     * 結果：テンプレート一覧が返ること
     */
    @Test
    fun getTemplateAll() {
        runBlocking {
            val resultList = repository.getTemplateAll()
                .first()
            resultList.forEachIndexed { index, template ->
                assertEquals(templateList[index], template)
            }
        }
    }

    // endregion

    // region テンプレートに紐づいたテンプレート文取得

    /**
     * テンプレートメッセージリスト取得
     *
     * 条件；なし
     * 結果：引数とした渡したテンプレートIDに紐づくテンプレートメッセージリストが返ること
     */
    @Test
    fun getTemplateMessageById() {
        runBlocking {
            val result = repository.getTemplateMessageById(template.templateId)
            assertEquals(templateMessageList, result)
        }
    }

    // endregion
}
