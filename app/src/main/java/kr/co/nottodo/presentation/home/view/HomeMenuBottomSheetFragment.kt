package kr.co.nottodo.presentation.home.view

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kr.co.nottodo.R
import kr.co.nottodo.data.local.ParcelizeBottomDetail
import kr.co.nottodo.data.remote.model.home.ResponHomeMissionDetail
import kr.co.nottodo.databinding.FragmentHomeMenuBottomSheetBinding
import kr.co.nottodo.databinding.ItemHomeBottomActionsBinding
import kr.co.nottodo.presentation.home.view.HomeFragment.Companion.MISSION_ID
import kr.co.nottodo.presentation.modification.view.ModificationActivity

class HomeMenuBottomSheetFragment : BottomSheetDialogFragment() {
    private var _binding: FragmentHomeMenuBottomSheetBinding? = null
    private val binding: FragmentHomeMenuBottomSheetBinding
        get() = requireNotNull(_binding)
    private val viewModel by activityViewModels<HomeViewModel>()
    private lateinit var detailData: ParcelizeBottomDetail
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeMenuBottomSheetBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initData(requireArguments().getLong(MISSION_ID))
        setOnClick()
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

    }

    private fun setOnClick() {
        binding.ivHomeDialogCancle.setOnClickListener { dismiss() }
        binding.tvHomeDialogEdit.setOnClickListener {
            //todo 파셀라블 적용
            val intent = Intent(context, ModificationActivity::class.java)
            intent.putExtra(DETAIL, detailData)
            startActivity(intent)
        }
        binding.tvHomeDialogAddDay.setOnClickListener {
            //todo calender 넣어야됨
        }
        binding.btnHomeDelete.setOnClickListener {
            clickDelete(requireArguments().getLong(MISSION_ID))
            viewModel.getHomeDaily("2023-05-21")
            dismiss()
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
            }
            //동적추가
            binding.linearHomeAction.run {
                val createLinearBindinding = {
                    ItemHomeBottomActionsBinding.inflate(LayoutInflater.from(binding.root.context))
                }
                removeAllViews()
                it.actions?.map { actions ->
                    createLinearBindinding().apply {
                        tvHomeBottomActionItem.text = actions.name.toString()
                    }
                }?.forEach {
                    addView(it.root)
                }
            }
            parcelizeData(it)
        }

    }

    private fun parcelizeData(it: ResponHomeMissionDetail.HomeMissionDetail) {
        detailData =
            ParcelizeBottomDetail(it.id, it.title, it.situation, it.count, it.goal)
    }

    private fun clickDelete(missionId: Long) {
        viewModel.deleteTodo(missionId)
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }

    companion object {
        const val DETAIL = "DETAIL"
        const val ACTIONS = "ACTIONS"
    }
}