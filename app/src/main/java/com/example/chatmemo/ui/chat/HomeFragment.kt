package com.example.chatmemo.ui.chat

import android.os.Bundle
import android.view.*
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.chatmemo.R
import com.example.chatmemo.databinding.FragmentHomeBinding
import com.example.chatmemo.model.entity.ChatRoom
import com.example.chatmemo.ui.MainActivity
import com.example.chatmemo.ui.adapter.RoomListAdapter
import kotlinx.coroutines.*
import org.koin.android.viewmodel.ext.android.viewModel
import kotlin.coroutines.CoroutineContext


/**
 * ホーム画面
 */
class HomeFragment : Fragment(), CoroutineScope {

    private lateinit var binding: FragmentHomeBinding
    private val viewModel: HomeViewModel by viewModel()
    private val job = SupervisorJob()
    override val coroutineContext: CoroutineContext = Dispatchers.Main + job

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as AppCompatActivity).supportActionBar?.title = "ルーム一覧"
        (activity as MainActivity).showNavigationBottom()
        viewModel.chatRoomList.observe(viewLifecycleOwner, Observer { viewUpDate(it) })

        val adapter = RoomListAdapter(listOf())
        val layoutManager = LinearLayoutManager(requireContext())
        val controller = AnimationUtils.loadLayoutAnimation(context, R.anim.fall_down)
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = layoutManager
        binding.recyclerView.layoutAnimation = controller

        // リストビューの各項目タップ
        adapter.setOnItemClickListener(object : RoomListAdapter.OnItemClickListener {
            override fun onItemClickListener(view: View, position: Int, item: ChatRoom) {
                when (view.id) {
                    R.id.container_main -> {
                        (requireActivity() as MainActivity).hideNavigationBottom()
                        val anim1 = AnimationUtils.loadAnimation(requireContext(), R.anim.fade_out)
                        binding.recyclerView.startAnimation(anim1)
                        binding.fab.startAnimation(anim1)
                        binding.fab.visibility = View.GONE
                        launch {
                            delay(resources.getInteger(R.integer.fade_out_time).toLong())
                            binding.recyclerView.visibility = View.GONE
                            val action = HomeFragmentDirections.actionHomeFragmentToChatFragment(
                                item
                            )
                            findNavController().navigate(action)
                        }
                    }
                    R.id.btn_delete     -> {
                        AlertDialog.Builder(requireActivity()).setTitle("ルーム削除")
                            .setMessage("削除しますか？").setPositiveButton("はい") { _, _ ->
                                viewModel.deleteRoom(item.id!!)
                            }.setNegativeButton("いいえ", null).show()
                    }
                }
            }
        })
        // Fabボタン
        binding.fab.setOnClickListener {
            val extras = FragmentNavigatorExtras(it to "end_fab_transition")
            findNavController().navigate(
                R.id.action_homeFragment_to_roomAddFragment, null, null, extras
            )
        }
    }

    override fun onDestroy() {
        job.cancel()
        super.onDestroy()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.action_bar_menu, menu)
        menu.findItem(R.id.menu_delete_room).isVisible = false
    }

    // データ反映
    private fun viewUpDate(data: List<ChatRoom>) {
        launch {
            val adapter = binding.recyclerView.adapter as RoomListAdapter
            adapter.setData(data)
            adapter.notifyDataSetChanged()
            if (data.isEmpty()) {
                binding.noDataText.visibility = View.VISIBLE
            } else {
                binding.noDataText.visibility = View.GONE
            }
        }
    }

}