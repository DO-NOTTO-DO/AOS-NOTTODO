package kr.co.nottodo.presentation.mypage.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import kr.co.nottodo.databinding.FragmentMyPageBinding
import kr.co.nottodo.interfaces.MainInterface

class MyPageFragment : Fragment() {
    private var _binding: FragmentMyPageBinding? = null
    private val binding: FragmentMyPageBinding
        get() = requireNotNull(_binding)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        setActivityBackgroundColor()
        _binding = FragmentMyPageBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    private fun setActivityBackgroundColor() {
        (context as MainInterface).setActivityBackgroundColorBasedOnFragment(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}