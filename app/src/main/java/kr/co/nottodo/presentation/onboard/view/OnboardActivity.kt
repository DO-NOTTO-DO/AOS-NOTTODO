package kr.co.nottodo.presentation.onboard.view

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import kr.co.nottodo.R
import kr.co.nottodo.databinding.ActivityOnboardBinding
import kr.co.nottodo.presentation.onboard.OnboardInterface

class OnboardActivity : AppCompatActivity(), OnboardInterface {
    lateinit var binding: ActivityOnboardBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityOnboardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initFragments(savedInstanceState)
    }

    private fun initFragments(savedInstanceState: Bundle?) {
        if (savedInstanceState == null) {
            addFragment(OnboardSecondFragment())
        }
    }

    private fun addFragment(fragment: Fragment) {
        supportFragmentManager
            .beginTransaction()
            .add(R.id.fcv_onboard, fragment)
            .commit()
    }

    override fun changeFragment(fragment: Fragment) {
        supportFragmentManager.commit {
            setCustomAnimations(R.anim.fade_in, R.anim.fade_out)
            replace(R.id.fcv_onboard, fragment)
        }
    }

    override fun setIndicatorNext() {
        with(binding) {
            ivOnboardIndicator.setImageResource(R.drawable.ic_tutorial_second)
            tvOnboardIndicator.text = "사용 방법"
        }
    }

    override fun showOnboardIndicator() {
        binding.layoutOnboardIndicator.visibility = View.VISIBLE
    }
}