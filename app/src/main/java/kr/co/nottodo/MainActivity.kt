package kr.co.nottodo

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.shape.CornerFamily
import com.google.android.material.shape.MaterialShapeDrawable
import kr.co.nottodo.data.local.SharedPreferences
import kr.co.nottodo.databinding.ActivityMainBinding
import kr.co.nottodo.listeners.OnFragmentChangedListener
import kr.co.nottodo.presentation.achieve.AchieveFragment
import kr.co.nottodo.presentation.home.view.HomeFragment
import kr.co.nottodo.presentation.login.view.LoginActivity.Companion.DID_USER_CHOOSE_TO_BE_NOTIFIED
import kr.co.nottodo.presentation.mypage.view.MyPageFragment
import kr.co.nottodo.util.showToast

class MainActivity : AppCompatActivity(), OnFragmentChangedListener {
    private lateinit var binding: ActivityMainBinding
    private lateinit var requestPermissionLauncher: ActivityResultLauncher<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setResultLaunchers()
        initBottomNavigationView()
        requestPermissions()
        overrideBackPressed()
    }

    private fun requestPermissions() {
        requestNotificationPermission()
        requestPhoneStateOrNumbers()
    }

    private fun requestPhoneStateOrNumbers() {
        if (ContextCompat.checkSelfPermission(
                this,
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) Manifest.permission.READ_PHONE_NUMBERS else Manifest.permission.READ_PHONE_STATE
            ) == PackageManager.PERMISSION_GRANTED
        ) return

        val permissions: Array<String> = arrayOf(
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) Manifest.permission.READ_PHONE_NUMBERS else Manifest.permission.READ_PHONE_STATE
        )
        requestPermissions(permissions, REQUEST_PHONE_STATE_OR_NUMBERS_CODE)
    }

    private fun setResultLaunchers() {
        requestPermissionLauncher = registerForActivityResult(
            ActivityResultContracts.RequestPermission(),
        ) { isGranted: Boolean ->
            SharedPreferences.setBoolean(DID_USER_CHOOSE_TO_BE_NOTIFIED, isGranted)
        }
    }

    private fun requestNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.POST_NOTIFICATIONS,
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                // FCM SDK (and your app) can post notifications.
            } else if (shouldShowRequestPermissionRationale(Manifest.permission.POST_NOTIFICATIONS)) {
                // TODO: display an educational UI explaining to the user the features that will be enabled
                //       by them granting the POST_NOTIFICATION permission. This UI should provide the user
                //       "OK" and "No thanks" buttons. If the user selects "OK," directly request the permission.
                //       If the user selects "No thanks," allow the user to continue without notifications.
            } else {
                // Directly ask for the permission (first time)
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        } else {
            SharedPreferences.setBoolean(DID_USER_CHOOSE_TO_BE_NOTIFIED, true)
        }
    }

    private fun initBottomNavigationView() {
        binding.bnvMain.itemIconTintList = null

        val radius = resources.getDimension(R.dimen.bnv_radius)
        val bottomNavigationViewBackground = binding.bnvMain.background as MaterialShapeDrawable
        bottomNavigationViewBackground.shapeAppearanceModel =
            bottomNavigationViewBackground.shapeAppearanceModel.toBuilder()
                .setTopRightCorner(CornerFamily.ROUNDED, radius)
                .setTopLeftCorner(CornerFamily.ROUNDED, radius).build()

        setBottomNavigationViewWithNavController()
    }

    private fun setBottomNavigationViewWithNavController() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fcv_main) as NavHostFragment
        val navController = navHostFragment.navController
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
        }
        onBackPressedDispatcher.addCallback(this, callback)
    }

    companion object {
        const val BLANK = ""
        const val REQUEST_PHONE_STATE_OR_NUMBERS_CODE = 0
    }
}
