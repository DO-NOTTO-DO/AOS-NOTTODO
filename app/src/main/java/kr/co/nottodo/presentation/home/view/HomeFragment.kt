package kr.co.nottodo.presentation.home.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import kr.co.nottodo.data.model.ResponseHomeDaily
import kr.co.nottodo.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding: FragmentHomeBinding
        get() = requireNotNull(_binding)
    private lateinit var homeAdpater: HomeAdpater

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentHomeBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initAdapter()
    }

    private fun initAdapter() {
        homeAdpater = HomeAdpater()
        binding.rvHomeTodoList.adapter = homeAdpater
        val todoList = listOf(
            ResponseHomeDaily(
                missions = 1,
                id = 1,
                title = "어쩌구",
                completionStatus = "저쩌구",
                situation = "잉"
            ),
            ResponseHomeDaily(
                missions = 1,
                id = 2,
                title = "어쩌구2",
                completionStatus = "저쩌구2",
                situation = "잉"
            ),
            ResponseHomeDaily(
                missions = 1,
                id = 3,
                title = "어쩌구3",
                completionStatus = "저쩌구3",
                situation = "잉"
            ),
        )


        homeAdpater.submitList(todoList)
    }

//    private fun menuclick(index:Int) {
//return ind
//    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}