package com.example.chatmemo.ui.phrase

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.chatmemo.R
import com.example.chatmemo.databinding.FragmentFixedPhraseAddBinding
import com.example.chatmemo.model.entity.Phrase
import com.example.chatmemo.ui.adapter.PhraseListAdapter
import org.koin.android.viewmodel.ext.android.viewModel

/**
 * 定型文作成画面
 */
class FixedPhraseAddFragment : Fragment() {

    private lateinit var binding: FragmentFixedPhraseAddBinding
    private val viewModel: FixedPhraseAddViewModel by viewModel()
    private val args: FixedPhraseAddFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_fixed_phrase_add,
            container,
            false
        )
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as AppCompatActivity).supportActionBar?.title = "定型文作成"

        viewModel.init(args.data)
        viewModel.submitState.observe(viewLifecycleOwner, Observer { back(it) })
        viewModel.phraseList.observe(viewLifecycleOwner, Observer { viewUpDate(it) })
        viewModel.titleText.observe(viewLifecycleOwner, Observer { viewModel.changeSubmitButton() })
        viewModel.phraseText.observe(viewLifecycleOwner, Observer { viewModel.changePhraseSubmitButton(it) })

        // 定型文リスト
        val adapter = PhraseListAdapter(arrayListOf())
        val layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = layoutManager
        adapter.setOnItemClickListener(object : PhraseListAdapter.OnItemClickListener{
            override fun onItemClickListener(view: View, position: Int) {
                when (view.id) {
                    R.id.btn_delete -> { viewModel.removePhraseList(position) }
                }
            }
        })
        // 追加ボタン
        binding.btnSubmit.setOnClickListener { viewModel.addPhrase() }
        // 登録ボタン
        binding.btnAdd.setOnClickListener { viewModel.submit() }
    }

    // データ追加
    private fun viewUpDate(data: ArrayList<Phrase>) {
        val adapter = binding.recyclerView.adapter as PhraseListAdapter
        adapter.setData(data)
        adapter.notifyDataSetChanged()
    }

    // 設定画面へ画面戻る
    private fun back(result: Boolean) {
        when (result) {
            true -> findNavController().popBackStack()
            false -> {
                Toast.makeText(
                    requireContext(),
                    R.string.error_phrase_title,
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

}