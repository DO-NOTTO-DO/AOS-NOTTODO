package kr.co.nottodo

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.shape.CornerFamily
import com.google.android.material.shape.MaterialShapeDrawable
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfigSettings
import kr.co.nottodo.data.local.SharedPreferences
import kr.co.nottodo.data.local.UpdateAppInfo
import kr.co.nottodo.databinding.ActivityMainBinding
import kr.co.nottodo.listeners.OnFragmentChangedListener
import kr.co.nottodo.listeners.OnWithdrawalDialogDismissListener
import kr.co.nottodo.presentation.achieve.AchieveFragment
import kr.co.nottodo.presentation.home.view.HomeFragment
import kr.co.nottodo.presentation.mypage.view.MyPageFragment
import kr.co.nottodo.util.showToast
import kr.co.nottodo.view.calendar.monthly.util.navigateToGooglePlayStore
import org.json.JSONObject
import timber.log.Timber
import java.util.Scanner

class MainActivity :
    AppCompatActivity(),
    OnFragmentChangedListener,
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
        initializeFirebaseRemoteConfig()
        checkAndShowAppUpdate()
        setBottomNavigationView()
        overrideBackPressed()
    }

    private fun initializeFirebaseRemoteConfig() {
        val remoteConfig: FirebaseRemoteConfig = Firebase.remoteConfig
        val configSettings = remoteConfigSettings {
            minimumFetchIntervalInSeconds = 3600
        }
        remoteConfig.setConfigSettingsAsync(configSettings)
        remoteConfig.setDefaultsAsync(R.xml.remote_config_defaults)
    }

    private fun checkAndShowAppUpdate() {
        Firebase.remoteConfig.fetchAndActivate()
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    val updated =
                        parseAppInfoJson(Firebase.remoteConfig.getString(REMOTE_KEY_APP_INFO))
                    showUpdatePopUp(updated.appVersion, updated.appForceUpdate)
                } else {
                    // todo fetch and activate 실패일 경우
//                    Timber.tag("appInfoUpdate 실패에요").d("$appInfo")
                }
            }
    }

    private fun parseAppInfoJson(json: String): UpdateAppInfo {
        val jsonObject = JSONObject(json)
        return UpdateAppInfo(
            appVersion = jsonObject.getInt("app_version"),
            appForceUpdate = jsonObject.getBoolean("force_update"),
        )
    }

    private fun showUpdatePopUp(fetchUpdateVersion: Int, force: Boolean) {
        val versionName = BuildConfig.VERSION_NAME
        val currentVersion = Scanner(versionName.replace("\\D+".toRegex(), "")).nextInt()
        if (fetchUpdateVersion > currentVersion) {
            AlertDialog.Builder(this)
                .setTitle(R.string.app_version_update_title)
                .setPositiveButton("확인") { _, _ -> openUpdatePage() }
                .setNegativeButton("취소") { _, _ -> }
                .show()
        }
    }

    private fun openUpdatePage() {
        navigateToGooglePlayStore("kr.co.nottodo")
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
                R.id.homeFragment,
                R.id.achieveFragment,
                R.id.myPageFragment,
            ),
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
        const val FORCE_UPDATE = "force_update"
        private const val REMOTE_KEY_APP_INFO = "app_info"
    }
}
