package com.myapp.chatmemo.presentation.template

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.myapp.chatmemo.domain.model.entity.Template
import com.myapp.chatmemo.presentation.R
import com.myapp.chatmemo.presentation.databinding.FragmentTemplateListBinding
import com.myapp.chatmemo.presentation.utils.expansion.BaseFragment
import com.myapp.chatmemo.presentation.utils.expansion.OnTemplateTitleListItemClickListener
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder
import dagger.hilt.android.AndroidEntryPoint

/**
 * 定型文一覧画面
 */
@AndroidEntryPoint
class TtemplateListFragment : BaseFragment() {

    private lateinit var binding: FragmentTemplateListBinding
    private val viewModel: TemplateListViewModel by viewModels()
    private lateinit var clickListener: OnTemplateTitleListItemClickListener

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_template_list, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel
        return binding.root
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) {
        super.onViewCreated(view, savedInstanceState)
        (activity as AppCompatActivity).supportActionBar?.title = requireContext().getString(R.string.title_template_list)
        viewModel.templateList.observe(viewLifecycleOwner, { viewUpDate(it) })
        viewModel.status.observe(viewLifecycleOwner, { if (it != null && !it) showErrorToast() })

        // テンプレートリスト
        binding.recyclerView.let {
            val adapter = GroupAdapter<ViewHolder>()
            val layoutManager = LinearLayoutManager(requireContext())
            val controller = AnimationUtils.loadLayoutAnimation(context, R.anim.fall_down)
            it.adapter = adapter
            it.layoutManager = layoutManager
            it.layoutAnimation = controller
            clickListener = object : OnTemplateTitleListItemClickListener {
                override fun onItemClickListener(
                    view: View,
                    position: Int,
                    item: Template
                ) {
                    when (view.id) {
                        R.id.txt_name -> {
                            val extras = FragmentNavigatorExtras(view to "end_title_transition")
                            val sendData = bundleOf("data" to item)
                            findNavController().navigate(
                                R.id.action_templateListFragment_to_templateAddFragment, sendData, null, extras
                            )
                        }
                        R.id.btn_delete -> {
                            AlertDialog.Builder(requireActivity())
                                .setTitle(requireContext().getString(R.string.dialog_title_template_delete))
                                .setMessage(requireContext().getString(R.string.dialog_question_delete))
                                .setPositiveButton(requireContext().getString(R.string.dialog_positive)) { _, _ ->
                                    viewModel.deletePhrase(item)
                                }
                                .setNegativeButton(requireContext().getString(R.string.dialog_negative), null)
                                .show()
                        }
                    }
                }
            }
        }
        // 追加ボタン
        binding.fab.setOnClickListener {
            val extras = FragmentNavigatorExtras(it to "end_fab_transition")
            val data = bundleOf("data" to null)
            findNavController().navigate(R.id.action_templateListFragment_to_templateAddFragment, data, null, extras)
        }
    }

    // データ反映
    private fun viewUpDate(data: List<Template>) {
        val items = data.map { TemplateTitleItem(it, clickListener) }
        val adapter = binding.recyclerView.adapter as GroupAdapter<*>
        adapter.update(items)
    }

    // エラートースト表示
    private fun showErrorToast() {
        Toast.makeText(requireContext(), requireContext().getString(R.string.error_delete_template), Toast.LENGTH_SHORT)
            .show()
        viewModel.status.value = null
    }
}
