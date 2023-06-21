package kr.co.nottodo

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import com.google.android.material.shape.CornerFamily
import com.google.android.material.shape.MaterialShapeDrawable
import kr.co.nottodo.data.local.SharedPreferences
import kr.co.nottodo.databinding.ActivityMainBinding
import kr.co.nottodo.listeners.OnFragmentChangedListener
import kr.co.nottodo.presentation.achieve.AchieveFragment
import kr.co.nottodo.presentation.home.view.HomeFragment
import kr.co.nottodo.presentation.login.view.LoginActivity.Companion.DID_USER_CHOOSE_TO_BE_NOTIFIED
import kr.co.nottodo.presentation.mypage.view.MyPageFragment

class MainActivity : AppCompatActivity(), OnFragmentChangedListener {
    private lateinit var binding: ActivityMainBinding
    private lateinit var requestPermissionLauncher: ActivityResultLauncher<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setResultLaunchers()
        initBottomNavigationView(savedInstanceState)
        askNotificationPermission()
    }

    private fun setResultLaunchers() {
        requestPermissionLauncher = registerForActivityResult(
            ActivityResultContracts.RequestPermission(),
        ) { isGranted: Boolean ->
            SharedPreferences.setBoolean(DID_USER_CHOOSE_TO_BE_NOTIFIED, isGranted)
        }
    }

    private fun askNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(
                    this, Manifest.permission.POST_NOTIFICATIONS
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

    private fun initBottomNavigationView(savedInstanceState: Bundle?) {
        binding.bnvMain.itemIconTintList = null

        val radius = resources.getDimension(R.dimen.bnv_radius)
        val bottomNavigationViewBackground = binding.bnvMain.background as MaterialShapeDrawable
        bottomNavigationViewBackground.shapeAppearanceModel =
            bottomNavigationViewBackground.shapeAppearanceModel.toBuilder()
                .setTopRightCorner(CornerFamily.ROUNDED, radius)
                .setTopLeftCorner(CornerFamily.ROUNDED, radius).build()

        if (savedInstanceState == null) {
            changeFragment(HomeFragment())
        }

        binding.bnvMain.setOnItemSelectedListener {
            changeFragment(
                when (it.itemId) {
                    R.id.menu_home -> HomeFragment()
                    R.id.menu_calendar -> AchieveFragment()
                    R.id.menu_my_page -> MyPageFragment()
                    else -> MyPageFragment()
                }
            )
            true
        }
    }

    private fun changeFragment(fragment: Fragment) {
        supportFragmentManager.commit {
            replace(R.id.fcv_main, fragment)
        }
    }

    override fun setActivityBackgroundColorBasedOnFragment(thisFragment: Fragment) {
        when (thisFragment) {
            is HomeFragment -> binding.root.setBackgroundColor(getColor(R.color.bg_f2f2f7))
            is AchieveFragment -> binding.root.setBackgroundColor(getColor(R.color.black))
            is MyPageFragment -> binding.root.setBackgroundColor(getColor(R.color.black))
        }
    }

    companion object {
        const val BLANK = ""
    }
}