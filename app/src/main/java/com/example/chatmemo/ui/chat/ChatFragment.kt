package com.example.chatmemo.ui.chat

import android.graphics.Rect
import android.os.Bundle
import android.view.*
import android.view.ViewTreeObserver.OnPreDrawListener
import android.view.animation.AnimationUtils
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
import com.example.chatmemo.domain.model.value.Comment
import com.example.chatmemo.ui.adapter.ChatRecyclerAdapter
import com.example.chatmemo.ui.utils.transition.PlayTransition
import kotlin.coroutines.CoroutineContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.koin.core.parameter.parametersOf

/**
 * チャット画面
 */
class ChatFragment : Fragment(), CoroutineScope {

    private var isKeyboardShowing = false
    private val args: ChatFragmentArgs by navArgs()
    private lateinit var binding: FragmentChatBinding
    private val viewModel: ChatViewModel by inject { parametersOf(args.data.roomId) }
    private val job = SupervisorJob()
    override val coroutineContext: CoroutineContext = Dispatchers.Main + job

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_chat, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val trans = PlayTransition(resources.getColor(R.color.white, null))
        sharedElementEnterTransition = trans
        sharedElementReturnTransition = trans
        val anim1 = AnimationUtils.loadAnimation(requireContext(), R.anim.slide_in_bottom)
        binding.layoutInput.startAnimation(anim1)

        (activity as AppCompatActivity).supportActionBar?.title = args.data.title
        setHasOptionsMenu(true)

        viewModel.commentList.observe(viewLifecycleOwner, Observer { viewUpDate(it) })
        viewModel.chatRoom.observe(
            viewLifecycleOwner,
            Observer {
                (activity as AppCompatActivity).supportActionBar?.title = it.title
            }
        )

        val adapter = ChatRecyclerAdapter(requireContext(), listOf())
        val layoutManager = LinearLayoutManager(requireContext())
        layoutManager.stackFromEnd = true
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = layoutManager

        // 文字入力
        viewModel.commentText.observe(
            viewLifecycleOwner,
            Observer {
                // 高さ自動統制
                binding.editText.viewTreeObserver.addOnPreDrawListener(object : OnPreDrawListener {
                    override fun onPreDraw(): Boolean {
                        binding.editText.viewTreeObserver.removeOnPreDrawListener(this)
                        if (binding.editText.lineCount in 1..4) {
                            val scrollViewLayoutParam = binding.scrollView.layoutParams as ViewGroup.MarginLayoutParams
                            scrollViewLayoutParam.height = binding.editText.height
                            binding.scrollView.layoutParams = scrollViewLayoutParam
                        }
                        return true
                    }
                })
            }
        )

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
    }

    override fun onDestroy() {
        val anim1 = AnimationUtils.loadAnimation(requireContext(), R.anim.slide_out_bottom)
        binding.layoutInput.startAnimation(anim1)
        job.cancel()
        super.onDestroy()
    }

    override fun onResume() {
        super.onResume()
        requireActivity().window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
    }

    override fun onPause() {
        super.onPause()
        requireActivity().window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING)
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
                AlertDialog.Builder(requireActivity()).setTitle("ルームを削除しますか？")
                    .setPositiveButton("はい") { dialog, _ ->
                        viewModel.deleteRoom()
                        findNavController().popBackStack()
                        dialog.dismiss()
                    }.setNegativeButton("いいえ", null).show()
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
