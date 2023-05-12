package kr.co.nottodo.presentation.achieve

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import kr.co.nottodo.databinding.FragmentAchieveBinding
import kr.co.nottodo.interfaces.MainInterface


class AchieveFragment : Fragment() {
    private var _binding: FragmentAchieveBinding? = null
    private val binding: FragmentAchieveBinding get() = requireNotNull(_binding)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        setActivityBackgroundColor()
        _binding = FragmentAchieveBinding.inflate(layoutInflater, container, false)
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