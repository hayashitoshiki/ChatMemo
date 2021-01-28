package com.example.chatmemo.ui.chat

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.chatmemo.R
import com.example.chatmemo.databinding.FragmentRoomAddBinding
import com.example.chatmemo.model.entity.ChatRoom
import kotlinx.coroutines.launch
import org.koin.android.viewmodel.ext.android.viewModel

/**
 * 新規ルーム作成画面
 */
class RoomAddFragment : Fragment() {

    private lateinit var binding: FragmentRoomAddBinding
    private val viewModel: RoomAddViewModel by viewModel()

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_room_add, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel

        // editTextフォーカス制御
        binding.editTitle.setOnFocusChangeListener { v, hasFocus ->
            if (! hasFocus) {
                val imm =
                    activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(v.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
            }
        }
        binding.root.setOnTouchListener { v, event ->
            binding.root.requestFocus()
            v?.onTouchEvent(event) ?: true
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as AppCompatActivity).supportActionBar?.title = "ルーム作成"
        viewModel.titleText.observe(viewLifecycleOwner, Observer { viewModel.changeSubmitButton() })
        viewModel.phraseTitleValueInt.observe(
            viewLifecycleOwner,
            Observer { viewModel.changeSubmitButton() })

        // 新規作成ボタン
        binding.btnAddRoom.setOnClickListener {
            lifecycleScope.launch {
                val chatRoom: ChatRoom = viewModel.createRoom()
                val action = RoomAddFragmentDirections.actionRoomAddFragmentToChatFragment(chatRoom)
                findNavController().navigate(action)
            }
        }
    }
}