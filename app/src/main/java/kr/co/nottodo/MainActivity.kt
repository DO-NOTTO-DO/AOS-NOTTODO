package kr.co.nottodo

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.shape.CornerFamily
import com.google.android.material.shape.MaterialShapeDrawable
import kr.co.nottodo.data.local.SharedPreferences
import kr.co.nottodo.databinding.ActivityMainBinding
import kr.co.nottodo.listeners.OnFragmentChangedListener
import kr.co.nottodo.listeners.OnWithdrawalDialogDismissListener
import kr.co.nottodo.presentation.achieve.AchieveFragment
import kr.co.nottodo.presentation.home.view.HomeFragment
import kr.co.nottodo.presentation.mypage.view.MyPageFragment
import kr.co.nottodo.util.showToast

class MainActivity : AppCompatActivity(), OnFragmentChangedListener,
    OnWithdrawalDialogDismissListener {
    private lateinit var binding: ActivityMainBinding
    private val navController by lazy {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fcv_main) as NavHostFragment
        navHostFragment.navController
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setBottomNavigationView()
        overrideBackPressed()
    }

    private fun setBottomNavigationView() {
        binding.bnvMain.itemIconTintList = null

        val radius = resources.getDimension(R.dimen.bnv_radius)
        val bottomNavigationViewBackground = binding.bnvMain.background as MaterialShapeDrawable
        bottomNavigationViewBackground.shapeAppearanceModel =
            bottomNavigationViewBackground.shapeAppearanceModel.toBuilder()
                .setTopRightCorner(CornerFamily.ROUNDED, radius)
                .setTopLeftCorner(CornerFamily.ROUNDED, radius).build()

        setBottomNavigationViewWithNavController()
        setBottomNavigationViewVisibility()
    }

    private fun setBottomNavigationViewVisibility() {
        AppBarConfiguration(
            setOf(
                R.id.homeFragment, R.id.achieveFragment, R.id.myPageFragment
            )
        ).also {
            navController.addOnDestinationChangedListener { _, destination, _ ->
                binding.bnvMain.isVisible = it.topLevelDestinations.contains(destination.id)
            }
        }
    }

    private fun setBottomNavigationViewWithNavController() {
        binding.bnvMain.setupWithNavController(navController)
    }

    override fun setActivityBackgroundColorBasedOnFragment(thisFragment: Fragment) {
        when (thisFragment) {
            is HomeFragment -> binding.root.setBackgroundColor(getColor(R.color.bg_f2f2f7))
            is AchieveFragment -> binding.root.setBackgroundColor(getColor(R.color.black))
            is MyPageFragment -> binding.root.setBackgroundColor(getColor(R.color.black))
        }
    }

    private var doubleBackToExitPressedOnce = false
    private fun overrideBackPressed() {
        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                when (navController.currentDestination?.id) {
                    R.id.loginFragment -> {
                        if (!isFinishing) finish()
                        return
                    }

                    R.id.onboardFirstFragment, R.id.onboardSecondFragment, R.id.onboardThirdFragment, R.id.onboardFourthFragment, R.id.onboardFifthFragment, R.id.onboardSixthFragment -> {
                        finishAppWithBackButtonDoubleClick()
                        return
                    }
                }

                if (!binding.bnvMain.isVisible) {
                    navController.popBackStack()
                    return
                }

                finishAppWithBackButtonDoubleClick()
            }
        }
        onBackPressedDispatcher.addCallback(this, callback)
    }

    private fun finishAppWithBackButtonDoubleClick() {
        if (doubleBackToExitPressedOnce) {
            if (!isFinishing) finish()
            return
        }

        doubleBackToExitPressedOnce = true
        showToast("'뒤로'버튼 한번 더 누르시면 종료됩니다.")

        Handler(Looper.getMainLooper()).postDelayed({
            doubleBackToExitPressedOnce = false
        }, 2000)
    }

    override fun onWithdrawalDialogDismiss() {
        logout()
    }

    private fun logout() {
        SharedPreferences.clearForLogout()
        navigateToLogin()
    }

    private fun navigateToLogin() {
        navController.navigate(R.id.loginFragment)
    }

    companion object {
        const val BLANK = ""
        const val REQUEST_PHONE_STATE_OR_NUMBERS_CODE = 0
    }
}
