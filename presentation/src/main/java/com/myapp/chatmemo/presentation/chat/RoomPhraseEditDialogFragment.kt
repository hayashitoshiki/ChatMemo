package com.myapp.chatmemo.presentation.chat

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.myapp.chatmemo.domain.model.entity.ChatRoom
import com.myapp.chatmemo.domain.model.entity.Template
import com.myapp.chatmemo.domain.model.value.TemplateMode
import com.myapp.chatmemo.presentation.R
import com.myapp.chatmemo.presentation.databinding.DialogRoomPhraseEditBinding
import com.myapp.chatmemo.presentation.utils.expansion.firsText
import com.myapp.chatmemo.presentation.utils.expansion.text
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ルームの定型文設定変更ダイアログ
 */
@AndroidEntryPoint
class RoomPhraseEditDialogFragment : DialogFragment() {

    @Inject
    lateinit var assistedFactory: RoomPhraseEditViewModel.RoomPhraseEditViewModelAssistedFactory

    private val viewModel: RoomPhraseEditViewModel by viewModels {
        RoomPhraseEditViewModel.provideFactory(
            this,
            assistedFactory,
            requireArguments().getSerializable("room") as ChatRoom
        )
    }
    private lateinit var binding: DialogRoomPhraseEditBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.dialog_room_phrase_edit, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel
        return binding.root
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) {
        super.onViewCreated(view, savedInstanceState)

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

    // テンプレートタイトルスピナー設定
    private fun setTemplateTitleSpiner(templateList: List<Template>) {
        val titleList = templateList.mapIndexed { index, template ->
            if (index == 0) {
                val message = requireContext().getString(template.firsText)
                viewModel.templateTitleList.value!![0].title = message
                return@mapIndexed message
            } else {
                return@mapIndexed template.title
            }
        }
        val arrayAdapter: ArrayAdapter<String> =
            ArrayAdapter(requireActivity(), android.R.layout.simple_dropdown_item_1line, titleList)
        binding.spinnerTitle.setAdapter(arrayAdapter)
    }

    // テンプレートモードスピナー設定
    private fun setTemplateModeSpiner(templateModeList: List<TemplateMode>) {
        val modeList = templateModeList.mapIndexed { index, template ->
            val message = requireContext().getString(template.text)
            viewModel.templateModeList.value!![index].massage = message
            return@mapIndexed message
        }
        val arrayAdapter: ArrayAdapter<String> =
            ArrayAdapter(requireActivity(), android.R.layout.simple_dropdown_item_1line, modeList)
        binding.spinnerMode.setAdapter(arrayAdapter)
    }
}
