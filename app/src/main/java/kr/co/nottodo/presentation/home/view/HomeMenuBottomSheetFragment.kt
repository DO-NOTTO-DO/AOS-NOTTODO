package kr.co.nottodo.presentation.home.view

import android.app.Activity.RESULT_OK
import android.app.Dialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.coroutines.launch
import kr.co.nottodo.R
import kr.co.nottodo.data.local.ParcelizeBottomDetail
import kr.co.nottodo.data.local.ParcelizeBottomDetailRegister
import kr.co.nottodo.data.remote.model.home.ResponHomeMissionDetail
import kr.co.nottodo.databinding.FragmentHomeMenuBottomSheetBinding
import kr.co.nottodo.databinding.ItemHomeBottomActionsBinding
import kr.co.nottodo.presentation.home.view.HomeFragment.Companion.CLICK_DAY
import kr.co.nottodo.presentation.home.view.HomeFragment.Companion.MISSION_ID
import kr.co.nottodo.presentation.modification.view.ModificationActivity
import kr.co.nottodo.util.getParcelable
import timber.log.Timber

class HomeMenuBottomSheetFragment : BottomSheetDialogFragment(), DialogCloseListener {
    private var _binding: FragmentHomeMenuBottomSheetBinding? = null
    private val binding: FragmentHomeMenuBottomSheetBinding
        get() = requireNotNull(_binding)
    private val viewModel by activityViewModels<HomeViewModel>()
    val bundle = Bundle()
    private lateinit var resultLauncher: ActivityResultLauncher<Intent>
    private lateinit var modifyParcelizeExtra: ParcelizeBottomDetail
    private lateinit var clickDay: String
    private var dialogDismissListener: DialogCloseListener? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentHomeMenuBottomSheetBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initData(requireArguments().getLong(MISSION_ID))
        clickDay = requireArguments().getString(CLICK_DAY).toString()
        getMissionData()
        setOnClick()
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

    // 인터페이스 설정 메서드
    fun setDialogDismissListener(listener: DialogCloseListener) {
        dialogDismissListener = listener
    }

    private fun setOnClick() {
        binding.ivHomeDialogCancle.setOnClickListener { dismiss() }
        binding.tvHomeDialogEdit.setOnClickListener {
            val intent = Intent(requireActivity(), ModificationActivity::class.java)
            intent.putExtra(DETAIL, modifyParcelizeExtra)
            resultLauncher.launch(intent)
        }
        binding.clHomeDoAnotherClickArea.setOnClickListener {
            val dialogFragment = HomeDoAnotherFragment()
            dialogFragment.arguments = bundle
            dialogFragment.setDialogDismissListener(this)
            dialogFragment.show(childFragmentManager, "dialog_fragment")
        }
        binding.btnHomeDelete.setOnClickListener { showDeleteDialog() }
    }

    private fun showDeleteDialog() {
        val dialog = HomeNottodoDeleteFragment()
        dialog.setDeleteButtonClickListener(this)
        dialog.show(childFragmentManager, "delete_dialog_fragment")
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
            bundle.putLong(MISSION_ID, it.id)
            modifyParcelizeExtra = parcelizeData(it)
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
            binding.tvHomeDialogGoalDescription.visibility = View.INVISIBLE
        } else {
            binding.tvHomeBottomGoalEmpty.visibility = View.GONE
            binding.ivHomeBottomGoalEmpty.visibility = View.GONE
            binding.tvHomeDialogGoalDescription.visibility = View.VISIBLE
            binding.tvHomeDialogGoalDescription.text = it
        }
    }

    private fun parcelizeData(item: ResponHomeMissionDetail.HomeMissionDetail): ParcelizeBottomDetail {
        val actionHome = item.actions
        return ParcelizeBottomDetail(
            item.id,
            item.title,
            item.situation,
            actionHome?.map { ParcelizeBottomDetail.Action(it.name) },
            item.count,
            item.goal,
            clickDay,
        )
    }

    private fun getModifyData() {
        resultLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == RESULT_OK) {
                    val parcelableData = result.data?.getParcelable(
                        DETAIL,
                        ParcelizeBottomDetailRegister::class.java,
                    )
                    // 결과 처리
                    if (parcelableData != null) {
                        binding.tvHomeDialogSituation.text = parcelableData.title
                        binding.tvHomeDialogNotodo.text = parcelableData.situation
                        binding.tvHomeDialogGoalDescription.text = parcelableData.goal
                        // 데이터 클래스 값 사용
                    }
                    viewModel.getHomeBottomDetail(requireArguments().getLong(MISSION_ID))
                }
            }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    companion object {
        const val DETAIL = "DETAIL"
        const val MISSIONID = "MISSIONID"
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        dialogDismissListener?.onDismissAndDataPass(null)
    }

    override fun onDismissAndDataPass(selectFirstDay: String?) {
        Timber.d("interface $selectFirstDay")
        dialogDismissListener?.onDismissAndDataPass(selectFirstDay)
        if (!selectFirstDay.isNullOrEmpty()) {
            dismiss()
        }
    }

    override fun onDeleteButtonClicked() {
        lifecycleScope.launch {
            viewModel.deleteTodo(requireArguments().getLong(MISSION_ID)).join()
            dialogDismissListener?.onDeleteButtonClicked()
            viewModel.getHomeDaily(modifyParcelizeExtra.date)
            dismiss()
        }
    }
}
