package kr.co.nottodo.presentation.home.view

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.coroutines.launch
import kr.co.nottodo.R
import kr.co.nottodo.data.local.ParcelizeBottomDetail
import kr.co.nottodo.data.remote.model.home.ResponHomeMissionDetail
import kr.co.nottodo.databinding.FragmentHomeMenuBottomSheetBinding
import kr.co.nottodo.databinding.ItemHomeBottomActionsBinding
import kr.co.nottodo.presentation.home.view.HomeFragment.Companion.CLICK_DAY
import kr.co.nottodo.presentation.home.view.HomeFragment.Companion.MISSION_ID
import kr.co.nottodo.presentation.home.viewmodel.HomeViewModel
import kr.co.nottodo.util.NotTodoAmplitude.trackEvent
import kr.co.nottodo.util.NotTodoAmplitude.trackEventWithPropertyList
import kr.co.nottodo.view.snackbar.NotTodoSnackbar
import timber.log.Timber

class HomeMenuBottomSheetFragment : BottomSheetDialogFragment(), DialogCloseListener {
    private var _binding: FragmentHomeMenuBottomSheetBinding? = null
    private val binding: FragmentHomeMenuBottomSheetBinding
        get() = requireNotNull(_binding)
    private val viewModel by activityViewModels<HomeViewModel>()
    val bundle = Bundle()
    private lateinit var modifyParcelizeExtra: ParcelizeBottomDetail
    private lateinit var clickDay: String
    private var dialogDismissListener: DialogCloseListener? = null
    private lateinit var trackActions: List<ResponHomeMissionDetail.Action>

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
            if (::modifyParcelizeExtra.isInitialized) {
                HomeFragmentDirections.actionHomeFragmentToModificationFragment(
                    modifyParcelizeExtra
                ).also { action -> findNavController().navigate(action) }
            } else {
                NotTodoSnackbar(binding.root, getString(R.string.net_work_error_message))
            }
        }
        binding.clHomeDoAnotherClickArea.setOnClickListener {
            val dialogFragment = HomeDoAnotherFragment()
            dialogFragment.arguments = bundle
            dialogFragment.setDialogDismissListener(this)
            dialogFragment.show(childFragmentManager, "dialog_fragment")
        }
        binding.btnHomeDelete.setOnClickListener {
            if (::trackActions.isInitialized) {
                showDeleteDialog()
                trackListData(R.string.click_delete_mission)
            }
        }
    }

    private fun trackDestroy() {
        trackEvent(getString(R.string.close_detail_mission))
    }

    private fun trackListData(EventName: Int) {
        val trackList = mutableMapOf<String, CharSequence>().apply {
            put(getString(R.string.title), binding.tvHomeDialogNotodo.text)
            put(getString(R.string.situation), binding.tvHomeDialogSituation.text)
            put(getString(R.string.goal), binding.tvHomeDialogGoalDescription.text)
            val actionCharSequence: CharSequence = trackActions.joinToString(",")
            put(getString(R.string.action), actionCharSequence)
        }
        trackEventWithPropertyList(getString(EventName), trackList)
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
            trackActions = it.actions ?: emptyList()
            trackListData(R.string.appear_detail_mission)
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

    override fun onDestroyView() {
        trackDestroy()
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
            trackListData(R.string.complete_add_mission_another_day)
            dismiss()
        }
    }

    override fun onDeleteButtonClicked() {
        trackListData(R.string.complete_delete_mission)
        lifecycleScope.launch {
            viewModel.deleteTodo(requireArguments().getLong(MISSION_ID)).join()
            dialogDismissListener?.onDeleteButtonClicked()
            viewModel.getHomeDaily(modifyParcelizeExtra.date)
            dismiss()
        }
    }
}
