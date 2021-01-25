package com.example.chatmemo.ui.chat

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.graphics.Rect
import android.os.Bundle
import android.speech.RecognizerIntent
import android.view.*
import android.view.ViewTreeObserver.OnPreDrawListener
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.chatmemo.R
import com.example.chatmemo.databinding.FragmentChatBinding
import com.example.chatmemo.model.entity.Comment
import com.example.chatmemo.ui.MainActivity
import com.example.chatmemo.ui.adapter.ChatRecyclerAdapter
import kotlinx.coroutines.*
import org.koin.android.ext.android.inject
import org.koin.core.parameter.parametersOf
import java.util.*
import kotlin.collections.ArrayList

import kotlin.coroutines.CoroutineContext


/**
 * チャット画面
 */
class ChatFragment : Fragment(), CoroutineScope{

    companion object {
        private const val REQUEST_CODE = 1
    }

    private var isKeyboardShowing = false
    private var isFirst = true

    private lateinit var binding: FragmentChatBinding
    private val args: ChatFragmentArgs by navArgs()
    private val viewModel: ChatViewModel by inject { parametersOf(args.data.id) }
    private val job = SupervisorJob()
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_chat, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as AppCompatActivity).supportActionBar?.title = args.data.title
        (activity as MainActivity).hideNavigationBottom()
        setHasOptionsMenu(true)

        viewModel.commentList.observe(viewLifecycleOwner, Observer { viewUpDate(it) })
        viewModel.chatRoom.observe(viewLifecycleOwner, Observer {
            (activity as AppCompatActivity).supportActionBar?.title = it.title
            viewModel.updateRoom(it)
        })

        val adapter = ChatRecyclerAdapter(requireContext(), listOf())
        val layoutManager = LinearLayoutManager(requireContext())
        layoutManager.stackFromEnd = true
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = layoutManager

        // 文字入力
        viewModel.commentText.observe(viewLifecycleOwner, Observer{
            viewModel.changeSubmitButton(it)
            // 高さ自動統制
            binding.editText.viewTreeObserver.addOnPreDrawListener(object : OnPreDrawListener {
                override fun onPreDraw(): Boolean {
                    binding.editText.viewTreeObserver.removeOnPreDrawListener(this)
                    if (binding.editText.lineCount in 1..4) {
                        val scrollViewLayoutParam = binding.scrollView.layoutParams as ViewGroup.MarginLayoutParams
                        val layoutInputLayoutParams = binding.layoutInput.layoutParams as ViewGroup.MarginLayoutParams
                        scrollViewLayoutParam.height = binding.editText.height
                        layoutInputLayoutParams.height = binding.editText.height + (16f * requireContext().resources.displayMetrics.density).toInt()
                        binding.scrollView.layoutParams = scrollViewLayoutParam
                        binding.layoutInput.layoutParams = layoutInputLayoutParams
                    } else if (isFirst && binding.editText.lineCount > 4) {
                        val scrollViewLayoutParam = binding.scrollView.layoutParams as ViewGroup.MarginLayoutParams
                        val layoutInputLayoutParams = binding.layoutInput.layoutParams as ViewGroup.MarginLayoutParams
                        scrollViewLayoutParam.height = 340
                        layoutInputLayoutParams.height = 340 + (16f * requireContext().resources.displayMetrics.density).toInt()
                        binding.scrollView.layoutParams = scrollViewLayoutParam
                        binding.layoutInput.layoutParams = layoutInputLayoutParams
                        isFirst = false
                    }
                    return true
                }
            })
        })

        // キーボード表示時のスクロール
        binding.container.viewTreeObserver.addOnGlobalLayoutListener {
            val r = Rect()
            binding.container.getWindowVisibleDisplayFrame(r)
            val screenHeight = binding.container.rootView.height
            val keypadHeight = screenHeight - r.bottom

            if (keypadHeight > screenHeight * 0.15 && !isKeyboardShowing) {
                isKeyboardShowing = true
                binding.recyclerView.adapter?.also {
                    binding.recyclerView.scrollToPosition(it.itemCount - 1)
                }
            } else if (isKeyboardShowing) {
                isKeyboardShowing = false
            }
        }

        // 送信ボタン
        binding.btnSubmit.setOnClickListener { viewModel.submit() }
        // 変更ボタン
        binding.btnChangeUser.setOnClickListener { viewModel.changeUser() }
        // 音声ボタン
        binding.btnVoice.setOnClickListener {
            val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).let{
                it.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.JAPANESE.toString())
                it.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 10)
                it.putExtra(RecognizerIntent.EXTRA_PROMPT, "話してください")
            }
            startActivityForResult(intent, REQUEST_CODE)
        }
    }

    @Suppress("DEPRECATED_IDENTITY_EQUALS")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode === REQUEST_CODE && resultCode === RESULT_OK) {
            val result: ArrayList<String>? =
                data?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)

            if (result != null && result.size > 0) {
                isFirst = true
                viewModel.commentText.value = result[0]
            } else {
                Toast.makeText(requireContext(), "音声の認識に失敗しました", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        requireActivity().window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)
    }

    override fun onDestroy() {
        job.cancel()
        super.onDestroy()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.action_bar_menu, menu)
        menu.findItem(R.id.menu_edit_room_phrase).isVisible = true
        menu.findItem(R.id.menu_delete_room).isVisible = true
        menu.findItem(R.id.menu_edit_room).isVisible = true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            // 定型文変更ボタン
            R.id.menu_edit_room_phrase -> {
                val dialogFragment = RoomPhraseEditDialogFragment()
                val bundle = Bundle()
                bundle.putSerializable("room", viewModel.chatRoom.value!!)
                dialogFragment.arguments = bundle
                dialogFragment.show(requireActivity().supportFragmentManager, null)
            }
            // ルーム名変更ボタン
            R.id.menu_edit_room -> {
                val dialogFragment = RoomTitleEditDialogFragment()
                val bundle = Bundle()
                bundle.putSerializable("room", viewModel.chatRoom.value!!)
                dialogFragment.arguments = bundle
                dialogFragment.show(requireActivity().supportFragmentManager, null)
            }
            // 新規作成ボタン
            R.id.menu_delete_room -> {
                AlertDialog.Builder(requireActivity())
                    .setTitle("ルームを削除しますか？")
                    .setPositiveButton("はい") { dialog, _ ->
                        viewModel.deleteRoom()
                        findNavController().popBackStack()
                        dialog.dismiss()
                    }
                    .setNegativeButton("いいえ", null)
                    .show()
            }
        }
        return true
    }

    // データ反映
    private fun viewUpDate(data: List<Comment>) {
        launch {
            val adapter = binding.recyclerView.adapter as ChatRecyclerAdapter
            adapter.setData(data)
            adapter.notifyDataSetChanged()
            binding.recyclerView.scrollToPosition(adapter.itemCount - 1)
        }
    }

}