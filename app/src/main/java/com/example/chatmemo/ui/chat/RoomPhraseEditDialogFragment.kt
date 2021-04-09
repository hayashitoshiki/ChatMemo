package com.example.chatmemo.ui.chat

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.example.chatmemo.R
import com.example.chatmemo.databinding.DialogRoomPhraseEditBinding
import com.example.chatmemo.domain.model.ChatRoom
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
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater, R.layout.dialog_room_phrase_edit, container, false
        )
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.templateTitleList.observe(viewLifecycleOwner, Observer { templateList ->
            val titleList = templateList.map { it.title }
            val arrayAdapter: ArrayAdapter<String> = ArrayAdapter(
                requireActivity(), android.R.layout.simple_dropdown_item_1line, titleList
            )
            binding.spinnerTitle.setAdapter(arrayAdapter)
        })
        viewModel.templateModeList.observe(viewLifecycleOwner, Observer { templateModeList ->
            val modeList = templateModeList.map { it.massage }
            val arrayAdapter: ArrayAdapter<String> = ArrayAdapter(
                requireActivity(), android.R.layout.simple_dropdown_item_1line, modeList
            )
            binding.spinnerMode.setAdapter(arrayAdapter)
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