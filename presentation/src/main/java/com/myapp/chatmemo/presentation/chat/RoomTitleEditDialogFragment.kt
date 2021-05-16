package com.myapp.chatmemo.presentation.chat

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.lifecycleScope
import com.myapp.chatmemo.domain.model.entity.ChatRoom
import com.myapp.chatmemo.presentation.R
import com.myapp.chatmemo.presentation.databinding.DialogRoomTitleEditBinding
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.koin.core.parameter.parametersOf

/**
 * ルーム名変更ダイアログ
 */
class RoomTitleEditDialogFragment : DialogFragment() {

    private val viewModel: RoomTitleEditViewModel by inject {
        parametersOf(requireArguments().getSerializable("room") as ChatRoom)
    }
    private lateinit var binding: DialogRoomTitleEditBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.dialog_room_title_edit, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel
        return binding.root
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) {
        super.onViewCreated(view, savedInstanceState)

        dialog?.window?.setBackgroundDrawableResource(R.color.transparent)

        // 決定ボタン
        binding.btnSubmit.setOnClickListener {
            lifecycleScope.launch {
                viewModel.newRoomTitle.value?.let {
                    viewModel.changeRoomName(it)
                    dismiss()
                }
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

    override fun onStart() {
        super.onStart()
        dialog?.window?.let {
            val width = (resources.displayMetrics.widthPixels * 0.9).toInt()
            val height = ViewGroup.LayoutParams.WRAP_CONTENT
            it.setBackgroundDrawable(null)
            it.setLayout(width, height)
        }
    }
}
