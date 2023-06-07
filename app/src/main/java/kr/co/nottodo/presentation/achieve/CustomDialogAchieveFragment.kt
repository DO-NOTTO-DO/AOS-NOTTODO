package kr.co.nottodo.presentation.achieve

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import kr.co.nottodo.databinding.FragmentCustomDialogAchieveBinding
import kr.co.nottodo.databinding.ItemAchieveDialogBinding
import kr.co.nottodo.presentation.achieve.AchieveFragment.Companion.CLICK_DATE

class CustomDialogAchieveFragment() : DialogFragment() {

    private var _binding: FragmentCustomDialogAchieveBinding? = null
    private val binding get() = _binding!!
    private val achieveViewModel by viewModels<AchieveFragmentViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCustomDialogAchieveBinding.inflate(layoutInflater, container, false)
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
        achieveViewModel.getAchieveDialog.observe(viewLifecycleOwner) {
            //동적추가
            binding.layoutAchieveTodo.run {
                val createLinearBindinding = {
                    ItemAchieveDialogBinding.inflate(LayoutInflater.from(binding.root.context))
                }
                removeAllViews()
                it?.map { actions ->
                    createLinearBindinding().apply {
                        tvAchieveTitle.text = actions.title
                        tvAchieveSituation.text = actions.situationName
                        if (actions.completionStatus == "NOTYET") {
                            ivAchieveItemCheck.visibility = View.INVISIBLE
                        } else {
                            ivAchieveItemCheck.visibility = View.VISIBLE
                        }
                    }
                }?.forEach {
                    addView(it.root)
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}