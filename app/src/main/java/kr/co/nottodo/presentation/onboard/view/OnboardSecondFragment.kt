package kr.co.nottodo.presentation.onboard.view

import android.animation.ValueAnimator
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.navigation.fragment.findNavController
import kr.co.nottodo.R
import kr.co.nottodo.databinding.FragmentOnboardSecondBinding
import kr.co.nottodo.presentation.base.fragment.ViewBindingFragment
import kr.co.nottodo.util.NotTodoAmplitude.trackEvent

class OnboardSecondFragment : ViewBindingFragment<FragmentOnboardSecondBinding>() {

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
            if (it.alpha == 1f) {
                trackEvent(getString(R.string.click_onboarding_start))
                findNavController().navigate(R.id.action_onboardSecondFragment_to_onboardThirdFragment)
            }
        }
    }

    private fun setViews() {
        startAnimation()
    }

    private fun startAnimation() {
        binding.layoutOnboardSecond.setTransitionListener(object : MotionLayout.TransitionListener {
            override fun onTransitionStarted(
                motionLayout: MotionLayout?,
                startId: Int,
                endId: Int,
            ) {
            }

            override fun onTransitionChange(
                motionLayout: MotionLayout?,
                startId: Int,
                endId: Int,
                progress: Float,
            ) {
            }

            override fun onTransitionCompleted(motionLayout: MotionLayout?, currentId: Int) {
                changeAlphaWithDuration(duration = 1000, startAlpha = 0f, endAlpha = 1f)
            }

            override fun onTransitionTrigger(
                motionLayout: MotionLayout?,
                triggerId: Int,
                positive: Boolean,
                progress: Float,
            ) {
            }
        })
    }

    private fun changeAlphaWithDuration(duration: Long, startAlpha: Float, endAlpha: Float) {
        ValueAnimator.ofFloat(startAlpha, endAlpha).apply {
            this.duration = duration
            addUpdateListener { animator ->
                val animatedValue = animator.animatedValue as Float
                binding.tvOnboardSecondDesc.alpha = animatedValue
                binding.btnOnboardSecondStart.alpha = animatedValue
            }
        }.start()
    }

    override fun setBinding(
        inflater: LayoutInflater, container: ViewGroup?,
    ): FragmentOnboardSecondBinding =
        FragmentOnboardSecondBinding.inflate(inflater, container, false)
}