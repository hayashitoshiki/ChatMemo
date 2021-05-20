package com.myapp.chatmemo.presentation.chat

import android.os.Bundle
import android.view.*
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.myapp.chatmemo.domain.model.entity.ChatRoom
import com.myapp.chatmemo.presentation.MainActivity
import com.myapp.chatmemo.presentation.R
import com.myapp.chatmemo.presentation.databinding.FragmentHomeBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

/**
 * ホーム画面
 */
class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private val viewModel: HomeViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel
        return binding.root
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) {
        super.onViewCreated(view, savedInstanceState)
        (activity as AppCompatActivity).supportActionBar?.title = requireContext().getString(R.string.title_home)
        (activity as MainActivity).showNavigationBottom()
        viewModel.chatRoomEntityList.observe(viewLifecycleOwner, { viewUpDate(it) })

        // チャットルームリスト
        binding.recyclerView.let {
            val adapter = RoomListAdapter()
            val layoutManager = LinearLayoutManager(requireContext())
            val controller = AnimationUtils.loadLayoutAnimation(context, R.anim.fall_down)
            adapter.setOnItemClickListener(object : RoomListAdapter.OnItemClickListener {
                override fun onItemClickListener(
                    view: View,
                    position: Int,
                    item: ChatRoom
                ) {
                    when (view.id) {
                        R.id.container_main -> {
                            (requireActivity() as MainActivity).hideNavigationBottom()
                            val anim1 = AnimationUtils.loadAnimation(requireContext(), R.anim.fade_out)
                            binding.recyclerView.startAnimation(anim1)
                            binding.fab.startAnimation(anim1)
                            binding.fab.visibility = View.GONE
                            val data = bundleOf("data" to item)
                            val extras = FragmentNavigatorExtras(view to "end_fab_transition")
                            findNavController().navigate(R.id.action_homeFragment_to_chatFragment, data, null, extras)
                        }
                        R.id.btn_delete -> {
                            AlertDialog.Builder(requireActivity())
                                .setTitle(requireContext().getString(R.string.dialog_title_room_delete))
                                .setMessage(requireContext().getString(R.string.dialog_question_delete))
                                .setPositiveButton(requireContext().getString(R.string.dialog_positive)) { _, _ ->
                                    viewModel.deleteRoom(item.roomId)
                                }
                                .setNegativeButton(requireContext().getString(R.string.dialog_negative), null)
                                .show()
                        }
                    }
                }
            })
            it.adapter = adapter
            it.layoutManager = layoutManager
            it.layoutAnimation = controller
        }
        // Fabボタン
        binding.fab.setOnClickListener {
            val extras = FragmentNavigatorExtras(it to "end_fab_transition")
            findNavController().navigate(R.id.action_homeFragment_to_roomAddFragment, null, null, extras)
        }
    }

    override fun onCreateOptionsMenu(
        menu: Menu,
        inflater: MenuInflater
    ) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.action_bar_menu, menu)
        menu.findItem(R.id.menu_delete_room).isVisible = false
    }

    // データ反映
    private fun viewUpDate(data: List<ChatRoom>) {
        val adapter = binding.recyclerView.adapter as RoomListAdapter
        adapter.submitList(data)
    }
}
