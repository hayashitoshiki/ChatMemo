package com.example.chatmemo.ui.phrase

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.view.animation.AnimationUtils
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.transition.ChangeBounds
import androidx.transition.ChangeClipBounds
import androidx.transition.ChangeTransform
import androidx.transition.TransitionSet
import com.example.chatmemo.R
import com.example.chatmemo.databinding.FragmentFixedPhraseAddBinding
import com.example.chatmemo.domain.value.TemplateMessage
import com.example.chatmemo.ui.adapter.PhraseListAdapter
import com.example.chatmemo.ui.transition.FabTransform
import org.koin.android.ext.android.inject
import org.koin.core.parameter.parametersOf

/**
 * 定型文作成画面
 */
class FixedPhraseAddFragment : Fragment() {

    private lateinit var binding: FragmentFixedPhraseAddBinding
    private val args: FixedPhraseAddFragmentArgs by navArgs()
    private val viewModel: FixedPhraseAddViewModel by inject { parametersOf(args.data) }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_fixed_phrase_add, container, false
        )
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val anim1 = AnimationUtils.loadAnimation(requireContext(), R.anim.fade_in_offset_300_anim)
        val anim2 = AnimationUtils.loadAnimation(requireContext(), R.anim.fade_in_offset_400_anim)
        val endTransition = TransitionSet().apply {
            addTransition(ChangeTransform())
        }
        val startTransition = if (args.data != null) {
            TransitionSet().apply {
                addTransition(ChangeBounds())
                addTransition(ChangeTransform())
                addTransition(ChangeClipBounds())
            }
        } else {
            FabTransform(
                resources.getColor(R.color.bg_color_accent, null), R.drawable.btn_circle_brown
            )
        }
        sharedElementEnterTransition = startTransition
        sharedElementReturnTransition = endTransition
        binding.layoutInput.startAnimation(anim1)
        binding.txtList.startAnimation(anim2)
        binding.recyclerView.startAnimation(anim2)

        (activity as AppCompatActivity).supportActionBar?.title = "定型文作成"

        viewModel.submitState.observe(viewLifecycleOwner, Observer { back(it) })
        viewModel.phraseList.observe(viewLifecycleOwner, Observer { viewUpDate(it) })

        // 文字入力
        viewModel.phraseText.observe(viewLifecycleOwner, Observer {
            // 高さ自動統制
            binding.editPharase.viewTreeObserver.addOnPreDrawListener(object : ViewTreeObserver.OnPreDrawListener {
                override fun onPreDraw(): Boolean {
                    binding.editPharase.viewTreeObserver.removeOnPreDrawListener(this)
                    if (binding.editPharase.lineCount in 1..4) {
                        val scrollViewLayoutParam = binding.scrollView.layoutParams as ViewGroup.MarginLayoutParams
                        scrollViewLayoutParam.height = binding.editPharase.height
                        binding.scrollView.layoutParams = scrollViewLayoutParam
                    }
                    return true
                }
            })
        })
        // 定型文リスト
        val adapter = PhraseListAdapter(arrayListOf())
        val layoutManager = LinearLayoutManager(requireContext())
        val itemDecoration = DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL)
        val controller = AnimationUtils.loadLayoutAnimation(context, R.anim.fall_down)
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = layoutManager
        binding.recyclerView.layoutAnimation = controller
        binding.recyclerView.addItemDecoration(itemDecoration)
        adapter.setOnItemClickListener(object : PhraseListAdapter.OnItemClickListener {
            override fun onItemClickListener(
                view: View, position: Int, items: MutableList<TemplateMessage>
            ) {
                when (view.id) {
                    R.id.btn_delete -> {
                        viewModel.updatePhraseList(items)
                    }
                }
            }
        })
        // 追加ボタン
        binding.btnSubmit.setOnClickListener { viewModel.addPhrase() }
        // 登録ボタン
        binding.btnAdd.setOnClickListener { viewModel.submit() }
        // editTextフォーカス制御
        binding.editTitle.setOnFocusChangeListener { v, hasFocus ->
            if (!hasFocus) {
                val imm = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(v.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
            }
        }
        binding.editPharase.setOnFocusChangeListener { v, hasFocus ->
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

    // データ追加
    private fun viewUpDate(data: MutableList<TemplateMessage>) {
        val adapter = binding.recyclerView.adapter as PhraseListAdapter
        if (adapter.itemCount < data.size) {
            adapter.setData(data)
            adapter.notifyDataSetChanged()
        }
    }

    // 設定画面へ画面戻る
    private fun back(result: Boolean) {
        when (result) {
            true  -> findNavController().popBackStack()
            false -> {
                Toast.makeText(
                    requireContext(), R.string.error_phrase_title, Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

}