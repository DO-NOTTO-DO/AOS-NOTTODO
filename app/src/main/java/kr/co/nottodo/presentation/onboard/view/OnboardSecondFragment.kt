package kr.co.nottodo.presentation.onboard.view

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kr.co.nottodo.R
import kr.co.nottodo.databinding.FragmentOnboardSecondBinding
import kr.co.nottodo.presentation.onboard.OnboardInterface
import kr.co.nottodo.util.NotTodoAmplitude.trackEvent

class OnboardSecondFragment : Fragment() {
    private var _binding: FragmentOnboardSecondBinding? = null
    private val binding: FragmentOnboardSecondBinding
        get() = requireNotNull(_binding)
    private var onboardInterface: OnboardInterface? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        onboardInterface = context as? OnboardInterface
            ?: throw TypeCastException("context can not cast as an OnboardInterface")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentOnboardSecondBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setViews()
        setClickEvents()
    }

    private fun setClickEvents() {
        setBtnClickEvent()
    }

    private fun setBtnClickEvent() {
        binding.btnOnboardSecondStart.setOnClickListener {
            trackEvent(getString(R.string.click_onboarding_start))
            onboardInterface?.changeFragment(OnboardThirdFragment())
            onboardInterface?.showOnboardIndicator()
        }
    }

    private fun setViews() {
        setVideoView()
    }

    private fun setVideoView() {
        val videoPath =
            "android.resource://" + requireContext().packageName + "/" + R.raw.video_logo_onboard_two
        with(binding.vvOnboardSecond) {
            setVideoPath(videoPath)
            setOnPreparedListener {
                startVideoAfter300Millis()
            }
            setOnCompletionListener {
                showBtn()
            }
        }
    }

    private fun showBtn() {
        binding.btnOnboardSecondStart.visibility = View.VISIBLE
    }

    private fun startVideoAfter300Millis() {
        lifecycleScope.launch {
            binding.vvOnboardSecond.start()
            delay(300)
            binding.vvOnboardSecond.alpha = 1F
        }
    }

    override fun onDetach() {
        super.onDetach()
        onboardInterface = null
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}