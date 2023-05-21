package kr.co.nottodo.presentation.home.view

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kr.co.nottodo.R
import kr.co.nottodo.databinding.FragmentHomeMenuBottomSheetBinding
import kr.co.nottodo.presentation.home.view.HomeFragment.Companion.MISSION_ID
import timber.log.Timber

class HomeMenuBottomSheetFragment : BottomSheetDialogFragment() {
    private var _binding: FragmentHomeMenuBottomSheetBinding? = null
    private val binding: FragmentHomeMenuBottomSheetBinding
        get() = requireNotNull(_binding)
    private val viewModel by activityViewModels<HomeViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeMenuBottomSheetBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setOnClick()
        initData(requireArguments().getLong(MISSION_ID))
        getMissionData()
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = BottomSheetDialog(requireContext(), theme)
        dialog.behavior.state = BottomSheetBehavior.STATE_EXPANDED
        dialog.behavior.skipCollapsed = true
        return dialog
    }

    private fun initData(index: Long) {
        viewModel.getHomeBottomDetail(index)
        Timber.tag("bottomFragment")
    }

    private fun setOnClick() {
        binding.ivHomeDialogCancle.setOnClickListener { dismiss() }
        binding.tvHomeDialogEdit.setOnClickListener {
            //todo 파셀라블 적용
        }
        binding.tvHomeDialogAddDay.setOnClickListener {
            //todo calender 넣어야됨
        }
    }

    private fun getMissionData() {
        viewModel.getHomeBottomDetail.observe(viewLifecycleOwner) {
            val completeCount = getString(R.string.home_dialog_statics, it.count)
            with(binding) {
                tvHomeDialogGoalDescription.text = it.goal
                tvHomeDialogSituation.text = it.situation
                tvHomeDialogNotodo.text = it.title
                tvHomeDialogGoalDescription.text = it.goal
                tvHomeDialogStatics.text = completeCount
                tvHomeDialogActionDescription.text = it.actions
            }

        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }
}