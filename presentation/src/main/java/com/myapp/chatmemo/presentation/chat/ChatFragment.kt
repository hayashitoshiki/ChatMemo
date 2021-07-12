package com.myapp.chatmemo.presentation.chat

import android.graphics.Rect
import android.os.Bundle
import android.view.*
import android.view.ViewTreeObserver.OnPreDrawListener
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.myapp.chatmemo.domain.model.value.Comment
import com.myapp.chatmemo.domain.model.value.User
import com.myapp.chatmemo.presentation.R
import com.myapp.chatmemo.presentation.databinding.FragmentChatBinding
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder
import com.xwray.groupie.databinding.BindableItem
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

/**
 * チャット画面
 */
@AndroidEntryPoint
class ChatFragment : Fragment() {

    private var isKeyboardShowing = false
    private val args: ChatFragmentArgs by navArgs()
    private lateinit var binding: FragmentChatBinding

    @Inject
    lateinit var assistedFactory: ChatViewModel.ViewModelAssistedFactory
    private val viewModel: ChatViewModel by viewModels {
        ChatViewModel.provideFactory(
            assistedFactory, args.data.roomId
        )
    }

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

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) {
        super.onViewCreated(view, savedInstanceState)
        val anim1 = AnimationUtils.loadAnimation(requireContext(), R.anim.slide_in_bottom)
        binding.layoutInput.startAnimation(anim1)

        setNavigationTitle(args.data.title)
        setHasOptionsMenu(true)

        viewModel.commentList.observe(viewLifecycleOwner, { viewUpDate(it) })
        viewModel.chatRoom.observe(viewLifecycleOwner, { setNavigationTitle(it.title) })
        viewModel.commentText.observe(viewLifecycleOwner, { setAutoHeigth() })

        val adapter = GroupAdapter<ViewHolder>()
        val layoutManager = LinearLayoutManager(requireContext())
        layoutManager.stackFromEnd = true
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = layoutManager

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

    override fun onCreateOptionsMenu(
        menu: Menu,
        inflater: MenuInflater
    ) {
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
                viewModel.chatRoom.value?.let {
                    val dialogFragment = RoomPhraseEditDialogFragment()
                    val bundle = Bundle()
                    bundle.putSerializable("room", it)
                    dialogFragment.arguments = bundle
                    dialogFragment.show(requireActivity().supportFragmentManager, null)
                }
            }
            // ルーム名変更ボタン
            R.id.menu_edit_room -> {
                viewModel.chatRoom.value?.let {
                    val dialogFragment = RoomTitleEditDialogFragment()
                    val bundle = Bundle()
                    bundle.putSerializable("room", it)
                    dialogFragment.arguments = bundle
                    dialogFragment.show(requireActivity().supportFragmentManager, null)
                }
            }
            // 新規作成ボタン
            R.id.menu_delete_room -> {
                AlertDialog.Builder(requireActivity())
                    .setTitle(requireContext().getString(R.string.dialog_title_room_delete))
                    .setMessage(requireContext().getString(R.string.dialog_question_delete))
                    .setPositiveButton(requireContext().getString(R.string.dialog_positive)) { dialog, _ ->
                        viewModel.deleteRoom()
                        findNavController().popBackStack()
                        dialog.dismiss()
                    }
                    .setNegativeButton(requireContext().getString(R.string.dialog_negative), null)
                    .show()
            }
        }
        return true
    }

    // ナビゲーションタイトル設定
    private fun setNavigationTitle(title: String) {
        (activity as AppCompatActivity).supportActionBar?.title = title
    }

    // 高さ自動統制
    private fun setAutoHeigth() {
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

    // データ反映
    private fun viewUpDate(data: List<Comment>) {
        val adapter = GroupAdapter<ViewHolder>()
        val items = mutableListOf<BindableItem<*>>()
        data.forEachIndexed { index, comment ->
            val beforComment = if (index != 0) data[index - 1] else null
            val state = ChatRecyclerItemState(beforComment, comment)
            if (index == 0 || state.isHeader) {
                items.add(CommentHeaderItem(comment))
            }
            when (comment.user) {
                User.BLACK -> items.add(CommentBlackItem(comment, requireContext()))
                User.WHITE -> items.add(CommentWhiteItem(comment, requireContext()))
            }
        }
        binding.recyclerView.adapter = adapter
        adapter.update(items)
    }
}
