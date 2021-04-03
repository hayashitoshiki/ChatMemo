package com.example.chatmemo.ui.chat

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
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.transition.ChangeTransform
import androidx.transition.TransitionSet
import com.example.chatmemo.R
import com.example.chatmemo.databinding.FragmentRoomAddBinding
import com.example.chatmemo.domain.model.ChatRoom
import com.example.chatmemo.ui.MainActivity
import com.example.chatmemo.ui.transition.FabTransform
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.koin.core.parameter.parametersOf

/**
 * 新規ルーム作成画面
 */
class RoomAddFragment : Fragment() {

    private lateinit var binding: FragmentRoomAddBinding
    private val viewModel: RoomAddViewModel by inject { parametersOf() }

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_room_add, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val transition = TransitionSet().apply {
            addTransition(ChangeTransform())
        }
        val trans = FabTransform(
            resources.getColor(R.color.bg_color_accent, null), R.drawable.btn_circle_brown
        )
        sharedElementEnterTransition = trans
        sharedElementReturnTransition = transition

        (activity as AppCompatActivity).supportActionBar?.title = "ルーム作成"

        viewModel.templateTitleList.observe(viewLifecycleOwner, Observer { templateList ->
            val templateTitleList = templateList.map { it.title }
            val arrayAdapter: ArrayAdapter<String> = ArrayAdapter(
                requireActivity(), android.R.layout.simple_dropdown_item_1line, templateTitleList
            )
            binding.spinnerTitle.setAdapter(arrayAdapter)
        })
        viewModel.tempalteModeList.observe(viewLifecycleOwner, Observer { modeList ->
            val modeTitleList = modeList.map { it.massage }
            val arrayAdapter: ArrayAdapter<String> = ArrayAdapter(
                requireActivity(), android.R.layout.simple_dropdown_item_1line, modeTitleList
            )
            binding.spinnerTitle.setAdapter(arrayAdapter)
        })

        // spinner 設定
        binding.spinnerTitle.let { spinner ->
            val arrayAdapter = ArrayAdapter(
                requireActivity(), android.R.layout.simple_dropdown_item_1line, listOf("")
            )
            spinner.setAdapter(arrayAdapter)
            spinner.keyListener = null
        }
        binding.spinnerMode.let { spinner ->
            val arrayAdapter = ArrayAdapter(
                requireActivity(), android.R.layout.simple_dropdown_item_1line, listOf("")
            )
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
                delay(resources.getInteger(R.integer.fade_out_time).toLong())
                val chatRoomEntity: ChatRoom = viewModel.createRoom()
                val action = RoomAddFragmentDirections.actionRoomAddFragmentToChatFragment(
                    chatRoomEntity
                )
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
}