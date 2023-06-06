package kr.co.nottodo.presentation.achieve

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import kr.co.nottodo.databinding.FragmentCustomDialogAchieveBinding
import kr.co.nottodo.databinding.ItemAchieveDialogBinding

class CustomDialogAchieveFragment() : DialogFragment() {

    private var _binding: FragmentCustomDialogAchieveBinding? = null
    private val binding get() = _binding!!
    private val achieveViewModel by viewModels<AchieveFragmentViewModel>()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        _binding = FragmentCustomDialogAchieveBinding.inflate(LayoutInflater.from(context))
        return AlertDialog.Builder(requireActivity())
            .setView(binding.root)
            .create()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        achieveViewModel.getAchieveDialogDaily("2023-06-01")
        setData()
        Log.d("achieveDialog0", "setData: ")
    }

    private fun setData() {
        Log.d("achieveDialog1", "setData: ")
        binding.tvAchieveDate
        achieveViewModel.getAchieveDialog.observe(viewLifecycleOwner) {
            Log.d("achieveDialog", "setData: $it")
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