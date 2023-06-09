package kr.co.nottodo.presentation.home.view

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import kr.co.nottodo.databinding.FragmentHomeDoAnotherBinding


class HomeDoAnotherFragment : DialogFragment() {

    private var _binding: FragmentHomeDoAnotherBinding? = null
    private val binding get() = _binding!!

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        _binding = FragmentHomeDoAnotherBinding.inflate(layoutInflater)
        val view = binding.root

        // 뷰들을 찾고 정보를 설정합니다.
//        binding.homeDoAnotherCalendar.


        val builder = AlertDialog.Builder(requireActivity())
            .setView(view)

        return builder.create()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}