package com.example.chatmemo.ui.phrase

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.chatmemo.R
import com.example.chatmemo.databinding.FragmentFixedPhraseListBinding
import com.example.chatmemo.model.entity.Template
import com.example.chatmemo.ui.adapter.PhraseTitleListAdapter
import org.koin.android.viewmodel.ext.android.viewModel

/**
 * 定型文一覧画面
 */
class FixedPhraseListFragment : Fragment() {

    private lateinit var binding: FragmentFixedPhraseListBinding
    private val viewModel: FixedPhraseListViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_fixed_phrase_list, container, false
        )
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as AppCompatActivity).supportActionBar?.title = "定型文一覧"
        viewModel.getList()
        viewModel.phraseList.observe(viewLifecycleOwner, Observer { viewUpDate(it) })
        viewModel.status.observe(
            viewLifecycleOwner,
            Observer { if (it != null && !it) showErrorToast() })

        val adapter = PhraseTitleListAdapter(listOf())
        val layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = layoutManager
        // リストビューの各項目タップ
        adapter.setOnItemClickListener(object : PhraseTitleListAdapter.OnItemClickListener {
            override fun onItemClickListener(view: View, position: Int, item: Template) {
                when (view.id) {
                    R.id.txt_name -> {
                        val action = FixedPhraseListFragmentDirections.actionFixedPhraseListFragmentToFixedPhraseAddFragment(
                            item
                        )
                        findNavController().navigate(action)
                    }
                    R.id.btn_delete -> {
                        AlertDialog.Builder(requireActivity()).setTitle("定型文削除")
                            .setMessage("削除しますか？").setPositiveButton("はい") { _, _ ->
                                viewModel.deletePhrase(item)
                            }.setNegativeButton("いいえ", null).show()
                    }
                }
            }
        })
        // 追加ボタン
        binding.fab.setOnClickListener {
            val action = FixedPhraseListFragmentDirections.actionFixedPhraseListFragmentToFixedPhraseAddFragment(
                null
            )
            findNavController().navigate(action)
        }
    }

    // データ反映
    private fun viewUpDate(data: List<Template>) {
        binding.progressBar.visibility = View.GONE
        if (data.isNotEmpty()) {
            binding.recyclerView.visibility = View.VISIBLE
            binding.noDataText.visibility = View.GONE
            val adapter = binding.recyclerView.adapter as PhraseTitleListAdapter
            adapter.setData(data)
            adapter.notifyDataSetChanged()
        } else {
            binding.recyclerView.visibility = View.GONE
            binding.noDataText.visibility = View.VISIBLE
        }
    }

    // エラートースト表示
    private fun showErrorToast() {
        Toast.makeText(requireContext(), "現在使用中のため削除できません", Toast.LENGTH_SHORT).show()
        viewModel.status.value = null
    }
}