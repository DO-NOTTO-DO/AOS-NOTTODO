package kr.co.nottodo.presentation.onboard.view

import android.animation.ValueAnimator
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.fragment.app.Fragment
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
        onboardInterface = context as? OnboardInterface ?: throw TypeCastException(
            getString(
                R.string.context_can_not_cast_as, getString(R.string.onboard_interface)
            )
        )
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
        val alphaAnimator = ValueAnimator.ofFloat(0f, 1f)
        alphaAnimator.duration = duration
        alphaAnimator.addUpdateListener { animator ->
            val animatedValue = animator.animatedValue as Float
            binding.tvOnboardSecondDesc.alpha = animatedValue
            binding.btnOnboardSecondStart.alpha = animatedValue
        }
        alphaAnimator.start()
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