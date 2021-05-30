package com.myapp.chatmemo.presentation.chat

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.view.inputmethod.InputMethodManager
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.transition.ChangeTransform
import androidx.transition.TransitionSet
import com.myapp.chatmemo.domain.model.entity.ChatRoom
import com.myapp.chatmemo.domain.model.entity.Template
import com.myapp.chatmemo.domain.model.value.TemplateMode
import com.myapp.chatmemo.presentation.MainActivity
import com.myapp.chatmemo.presentation.R
import com.myapp.chatmemo.presentation.databinding.FragmentRoomAddBinding
import com.myapp.chatmemo.presentation.utils.expansion.firsText
import com.myapp.chatmemo.presentation.utils.expansion.text
import com.myapp.chatmemo.presentation.utils.transition.FabTransform
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * 新規ルーム作成画面
 */
@AndroidEntryPoint
class RoomAddFragment : Fragment() {

    private lateinit var binding: FragmentRoomAddBinding
    private val viewModel: RoomAddViewModel by viewModels()

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_room_add, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel
        return binding.root
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) {
        super.onViewCreated(view, savedInstanceState)

        val transition = TransitionSet().apply {
            addTransition(ChangeTransform())
        }
        val trans = FabTransform(resources.getColor(R.color.bg_color_accent, null), R.drawable.btn_circle_brown)
        sharedElementEnterTransition = trans
        sharedElementReturnTransition = transition

        (activity as AppCompatActivity).supportActionBar?.title = requireContext().getString(R.string.title_room_add)

        viewModel.templateTitleList.observe(viewLifecycleOwner, { setTemplateTitleSpiner(it) })
        viewModel.templateModeList.observe(viewLifecycleOwner, { setTemplateModeSpiner(it) })

        // spinner 設定
        binding.spinnerTitle.let { spinner ->
            val arrayAdapter = ArrayAdapter(requireActivity(), android.R.layout.simple_dropdown_item_1line, listOf(""))
            spinner.setAdapter(arrayAdapter)
            spinner.keyListener = null
        }
        binding.spinnerMode.let { spinner ->
            val arrayAdapter = ArrayAdapter(requireActivity(), android.R.layout.simple_dropdown_item_1line, listOf(""))
            spinner.setAdapter(arrayAdapter)
            spinner.keyListener = null
        }
        // 新規作成ボタン
        binding.btnAddRoom.setOnClickListener {
            lifecycleScope.launch {
                (requireActivity() as MainActivity).hideNavigationBottom()
                val anim1 = AnimationUtils.loadAnimation(requireContext(), R.anim.fade_out)
                binding.container.startAnimation(anim1)
                binding.container.visibility = View.GONE
                delay(
                    resources.getInteger(R.integer.fade_out_time)
                        .toLong()
                )
                val chatRoom: ChatRoom = viewModel.createRoom()
                val action = RoomAddFragmentDirections.actionRoomAddFragmentToChatFragment(chatRoom)
                findNavController().navigate(action)
            }
        }
        // editTextフォーカス制御
        binding.editTitle.setOnFocusChangeListener { v, hasFocus ->
            if (!hasFocus) {
                val imm = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(v.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
            }
        }
        binding.root.setOnTouchListener { v, event ->
            binding.root.requestFocus()
            v?.onTouchEvent(event) ?: true
        }
    }

    // テンプレートタイトルスピナー設定
    private fun setTemplateTitleSpiner(templateList: List<Template>) {
        val templateTitleList = templateList.mapIndexed { index, template ->
            if (index == 0) {
                val message = requireContext().getString(template.firsText)
                viewModel.templateTitleList.value!![0].title = message
                return@mapIndexed message
            } else {
                return@mapIndexed template.title
            }
        }
        val arrayAdapter: ArrayAdapter<String> =
            ArrayAdapter(requireActivity(), android.R.layout.simple_dropdown_item_1line, templateTitleList)
        binding.spinnerTitle.setAdapter(arrayAdapter)
    }

    // テンプレートモードスピナー設定
    private fun setTemplateModeSpiner(templateModeList: List<TemplateMode>) {
        val modeTitleList = templateModeList.mapIndexed { index, template ->
            val message = requireContext().getString(template.text)
            viewModel.templateModeList.value!![index].massage = message
            return@mapIndexed message
        }
        val arrayAdapter: ArrayAdapter<String> =
            ArrayAdapter(requireActivity(), android.R.layout.simple_dropdown_item_1line, modeTitleList)
        binding.spinnerMode.setAdapter(arrayAdapter)
    }
}
