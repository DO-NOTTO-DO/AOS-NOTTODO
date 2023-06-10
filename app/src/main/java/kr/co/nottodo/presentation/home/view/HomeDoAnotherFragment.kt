package kr.co.nottodo.presentation.home.view

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.View
import androidx.fragment.app.DialogFragment
import kr.co.nottodo.databinding.FragmentHomeDoAnotherBinding


class HomeDoAnotherFragment : DialogFragment() {

    private var _binding: FragmentHomeDoAnotherBinding? = null
    private val binding get() = _binding!!

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        _binding = FragmentHomeDoAnotherBinding.inflate(layoutInflater)
        val view = binding.root
        val builder = AlertDialog.Builder(requireActivity())
            .setView(view)
        return builder.create()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        clickDone()
    }

    private fun clickDone() {
        binding.tvHomeCalendarSelectDone.setOnClickListener {
            binding.homeDoAnotherCalendar.selectedDays
        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

}