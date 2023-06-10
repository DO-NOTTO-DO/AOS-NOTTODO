package kr.co.nottodo.presentation.home.view

import android.app.Activity.RESULT_OK
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.activityViewModels
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kr.co.nottodo.R
import kr.co.nottodo.data.local.ParcelizeBottomDetail
import kr.co.nottodo.data.local.ParcelizeBottomDetailRegister
import kr.co.nottodo.data.remote.model.home.ResponHomeMissionDetail
import kr.co.nottodo.databinding.FragmentHomeMenuBottomSheetBinding
import kr.co.nottodo.databinding.ItemHomeBottomActionsBinding
import kr.co.nottodo.presentation.home.view.HomeFragment.Companion.MISSION_ID
import kr.co.nottodo.presentation.modification.view.ModificationActivity
import kr.co.nottodo.util.getParcelable

class HomeMenuBottomSheetFragment : BottomSheetDialogFragment() {
    private var _binding: FragmentHomeMenuBottomSheetBinding? = null
    private val binding: FragmentHomeMenuBottomSheetBinding
        get() = requireNotNull(_binding)
    private val viewModel by activityViewModels<HomeViewModel>()

    //    private lateinit var detailData: ParcelizeBottomDetail
    private lateinit var detailActionData: ParcelizeBottomDetail.Action
    private lateinit var resultLauncher: ActivityResultLauncher<Intent>

    //    private lateinit var changeParcle: List<ParcelizeBottomDetail.Action>
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentHomeMenuBottomSheetBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initData(requireArguments().getLong(MISSION_ID))
        getMissionData()
        setOnClick()
        clickAddAnotherDay()
        getModifyData()
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
            val intent = Intent(requireActivity(), ModificationActivity::class.java)
            resultLauncher.launch(intent)
            startActivity(intent)
        }
        binding.tvHomeDialogAddDay.setOnClickListener {
            val dialogFragment = HomeDoAnotherFragment()
            dialogFragment.show(childFragmentManager, "dialog_fragment")
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
            isEmptyActions(it)
            isGoalEmpty(it.goal)
            with(binding) {
                tvHomeDialogNotodo.text = it.title
                tvHomeDialogStatics.text = completeCount
                tvHomeDialogSituation.text = it.situation
            }
            parcelizeData(it)
        }
    }

    private fun isEmptyActions(it: ResponHomeMissionDetail.HomeMissionDetail) {
        if (it.actions.isNullOrEmpty()) {
            binding.tvHomeBottomActionEmptyDescription.visibility = View.VISIBLE
            binding.ivActionEmpty.visibility = View.VISIBLE
            binding.linearHomeAction.run { removeAllViews() }
        } else {
            binding.tvHomeBottomActionEmptyDescription.visibility = View.GONE
            binding.ivActionEmpty.visibility = View.GONE
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
        }
    }

    private fun isGoalEmpty(it: String?) {
        if (it.isNullOrEmpty()) {
            binding.tvHomeBottomGoalEmpty.visibility = View.VISIBLE
            binding.ivHomeBottomGoalEmpty.visibility = View.VISIBLE
        } else {
            binding.tvHomeBottomGoalEmpty.visibility = View.GONE
            binding.ivHomeBottomGoalEmpty.visibility = View.GONE
            binding.tvHomeDialogGoalDescription.text = it
        }
    }

    private fun parcelizeData(item: ResponHomeMissionDetail.HomeMissionDetail) {
        val actionHome = item.actions
//        detailData =
        ParcelizeBottomDetail(
            item.id,
            item.title,
            item.situation,
            actionHome?.map { ParcelizeBottomDetail.Action(it.name) },
            item.count,
            item.goal
        )
    }

    private fun getModifyData() {
        resultLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == RESULT_OK) {
                    val parcelableData =
                        result.data?.getParcelable(
                            DETAIL,
                            ParcelizeBottomDetailRegister::class.java
                        )
                    // 결과 처리
                    if (parcelableData != null) {
                        binding.tvHomeDialogSituation.text = parcelableData.title
                        binding.tvHomeDialogNotodo.text = parcelableData.situation
                        binding.tvHomeDialogGoal.text = parcelableData.goal
                        // 데이터 클래스 값 사용
                    }
                }
            }
    }

    private fun clickAddAnotherDay() {
        binding.tvHomeDialogDoAnother.setOnClickListener {
            dismiss()
        }
    }

    private fun clickDelete(missionId: Long) {
        viewModel.deleteTodo(missionId)

    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    companion object {
        const val DETAIL = "DETAIL"
    }
}