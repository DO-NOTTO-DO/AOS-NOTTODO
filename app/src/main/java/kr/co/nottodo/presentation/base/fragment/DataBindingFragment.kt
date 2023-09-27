package kr.co.nottodo.presentation.base.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment

abstract class DataBindingFragment<DB : ViewDataBinding>(@LayoutRes private val layoutResId: Int) :
    Fragment() {

    private var _binding: DB? = null
    protected val binding get() = requireNotNull(_binding) { "binding is null" }

    protected val contextNonNull by lazy { requireContext() }
    protected val activityNonNull by lazy { requireActivity() }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        _binding = DataBindingUtil.inflate(inflater, layoutResId, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        bindViewModelWithBinding()
        return binding.root
    }

    protected abstract fun bindViewModelWithBinding()

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}