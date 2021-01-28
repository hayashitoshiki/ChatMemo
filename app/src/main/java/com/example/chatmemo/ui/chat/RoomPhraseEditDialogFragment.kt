package com.example.chatmemo.ui.chat

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.example.chatmemo.R
import com.example.chatmemo.databinding.DialogRoomPhraseEditBinding
import com.example.chatmemo.model.entity.ChatRoom
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.koin.core.parameter.parametersOf

/**
 * ルームの定型文設定変更ダイアログ
 */
class RoomPhraseEditDialogFragment : DialogFragment() {

    private val viewModel: RoomPhraseEditViewModel by inject {
        parametersOf(
            requireArguments().getSerializable(
                "room"
            ) as ChatRoom
        )
    }
    private lateinit var binding: DialogRoomPhraseEditBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.dialog_room_phrase_edit, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.roomTitleValueInt.observe(viewLifecycleOwner, Observer { viewModel.validate() })
        viewModel.modeValueInt.observe(viewLifecycleOwner, Observer { viewModel.validate() })

        // 送信ボタン
        binding.btnSubmit.setOnClickListener {
            lifecycleScope.launch {
                viewModel.submit()
                dismiss()
            }
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