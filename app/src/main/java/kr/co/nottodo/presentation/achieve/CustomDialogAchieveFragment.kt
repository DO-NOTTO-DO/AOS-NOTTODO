package kr.co.nottodo.presentation.achieve

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import kr.co.nottodo.R
import kr.co.nottodo.databinding.FragmentCustomDialogAchieveBinding
import kr.co.nottodo.databinding.ItemAchieveDialogBinding
import kr.co.nottodo.presentation.achieve.AchieveFragment.Companion.CLICK_DATE
import kr.co.nottodo.util.NotTodoAmplitude
import timber.log.Timber

class CustomDialogAchieveFragment() : DialogFragment() {

    private var _binding: FragmentCustomDialogAchieveBinding? = null
    private val binding get() = _binding!!
    private val achieveViewModel by viewModels<AchieveFragmentViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentCustomDialogAchieveBinding.inflate(inflater, container, false)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        // 각 버튼 클릭 시 각각의 함수 호출
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setData()
        val selectDay = requireArguments().getString(CLICK_DATE)
        binding.tvAchieveDate.text = selectDay
        achieveViewModel.getAchieveDialogDaily(selectDay.toString())
    }

    private fun setData() {
        var totalAddMissionCount = 0
        achieveViewModel.getAchieveDialog.observe(viewLifecycleOwner) {
            totalAddMissionCount = it.size
            if (it.isNullOrEmpty()) {
                binding.tvAchieveNoTodo.visibility = View.VISIBLE
                binding.layoutAchieveTodo.visibility = View.GONE
                return@observe
            }
            // 동적추가
            binding.tvAchieveNoTodo.visibility = View.INVISIBLE
            binding.layoutAchieveTodo.visibility = View.VISIBLE
            binding.layoutAchieveTodo.run {
                val createLinearBindinding = {
                    ItemAchieveDialogBinding.inflate(LayoutInflater.from(binding.root.context))
                }
                removeAllViews()
                it?.map { actions ->
                    createLinearBindinding().apply {
                        tvAchieveTitle.text = actions.title
                        tvAchieveSituation.text = actions.situationName
                        if (actions.completionStatus == "UNCHECKED") {
                            ivAchieveItemCheck.visibility = View.INVISIBLE
                        } else {
                            ivAchieveItemCheck.visibility = View.VISIBLE
                        }
                    }
                }?.forEach {
                    addView(it.root)
                }
            }
            NotTodoAmplitude.trackEventWithProperty(
                getString(R.string.appear_daily_mission_modal),
                getString(R.string.total_add_mission),
                it.size,
            )
        }
    }

    override fun onDestroyView() {
        NotTodoAmplitude.trackEvent(getString(R.string.close_daily_mission_modal))
        super.onDestroyView()
        _binding = null
    }
}
