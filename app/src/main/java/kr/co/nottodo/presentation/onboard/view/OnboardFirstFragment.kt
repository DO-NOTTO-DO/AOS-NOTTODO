package kr.co.nottodo.presentation.onboard.view

import android.animation.Animator
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import kr.co.nottodo.R
import kr.co.nottodo.databinding.FragmentOnboardFirstBinding
import kr.co.nottodo.presentation.base.fragment.ViewBindingFragment

class OnboardFirstFragment : ViewBindingFragment<FragmentOnboardFirstBinding>() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setViews()
    }

    private fun setViews() {
        setLottieAnimationView()
    }

    private fun setLottieAnimationView() {
        binding.lottieOnboardFirst.addAnimatorListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(p0: Animator) {}

            override fun onAnimationEnd(p0: Animator) {
                findNavController().navigate(R.id.action_onboardFirstFragment_to_onboardSecondFragment)
            }

            override fun onAnimationCancel(p0: Animator) {}
            override fun onAnimationRepeat(p0: Animator) {}
        })
    }

    override fun setBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
    ): FragmentOnboardFirstBinding = FragmentOnboardFirstBinding.inflate(inflater, container, false)
}