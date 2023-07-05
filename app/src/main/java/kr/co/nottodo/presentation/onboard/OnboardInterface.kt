package kr.co.nottodo.presentation.onboard

import androidx.fragment.app.Fragment

interface OnboardInterface {
    fun changeFragment(fragment: Fragment)
    fun setIndicatorNext()
    fun showOnboardIndicator()
}