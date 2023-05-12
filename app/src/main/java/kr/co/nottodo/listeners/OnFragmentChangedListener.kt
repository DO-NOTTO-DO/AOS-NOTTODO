package kr.co.nottodo.listeners

import androidx.fragment.app.Fragment

interface OnFragmentChangedListener {
    fun setActivityBackgroundColorBasedOnFragment(thisFragment: Fragment)
}