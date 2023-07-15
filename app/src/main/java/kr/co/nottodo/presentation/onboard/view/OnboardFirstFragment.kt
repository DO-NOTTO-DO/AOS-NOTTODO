package kr.co.nottodo.presentation.onboard.view

import android.animation.Animator
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import kr.co.nottodo.R
import kr.co.nottodo.databinding.FragmentOnboardFirstBinding
import kr.co.nottodo.presentation.onboard.OnboardInterface

class OnboardFirstFragment : Fragment() {
    private var _binding: FragmentOnboardFirstBinding? = null
    private val binding: FragmentOnboardFirstBinding
        get() = requireNotNull(_binding)
    private var onboardInterface: OnboardInterface? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        onboardInterface = context as? OnboardInterface
            ?: throw TypeCastException(
                context.getString(
                    R.string.context_can_not_cast_as, getString(R.string.onboard_interface)
                )
            )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentOnboardFirstBinding.inflate(inflater, container, false)
        return binding.root
    }

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
                onboardInterface?.changeFragment(OnboardSecondFragment())
            }

            override fun onAnimationCancel(p0: Animator) {}
            override fun onAnimationRepeat(p0: Animator) {}
        })
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