package kr.co.nottodo.presentation.base.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding

abstract class BaseViewBindingFragment<VB : ViewBinding> : Fragment() {

    private var _binding: VB? = null
    protected val binding get() = requireNotNull(_binding) { "binding is null" }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = setBinding(inflater, container)
        return binding.root
    }

    protected abstract fun setBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
    ): VB

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}