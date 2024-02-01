package kr.co.nottodo

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.OnBackPressedCallback
import androidx.activity.viewModels
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
import kotlinx.serialization.json.Json
import kr.co.nottodo.data.local.SharedPreferences
import kr.co.nottodo.data.local.UpdateAppInfo
import kr.co.nottodo.databinding.ActivityMainBinding
import kr.co.nottodo.listeners.OnFragmentChangedListener
import kr.co.nottodo.listeners.OnWithdrawalDialogDismissListener
import kr.co.nottodo.presentation.achieve.AchieveFragment
import kr.co.nottodo.presentation.home.view.HomeFragment
import kr.co.nottodo.presentation.mypage.view.MyPageFragment
import kr.co.nottodo.util.PublicString
import kr.co.nottodo.util.showToast
import kr.co.nottodo.view.calendar.monthly.util.navigateToGooglePlayStore
import kr.co.nottodo.view.snackbar.NotTodoSnackbar
import timber.log.Timber
import java.util.Scanner

class MainActivity :
    AppCompatActivity(),
    OnFragmentChangedListener,
    OnWithdrawalDialogDismissListener {
    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainViewModel by viewModels()

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
                    handleUpdateAndCheckForce(updated.appVersion, updated.appForceUpdate)
                } else {
                    // todo fetch and activate 실패일 경우
                    NotTodoSnackbar(binding.root, getString(R.string.net_work_error_message)).show()
                }
            }
    }

    private val json = Json {
        prettyPrint = true
        ignoreUnknownKeys = true
    }

    private fun parseAppInfoJson(jsonFomServer: String): UpdateAppInfo {
        return json.decodeFromString<UpdateAppInfo>(jsonFomServer)
    }

    private fun handleUpdateAndCheckForce(fetchUpdateVersion: Int, force: Boolean) {
        val versionName = BuildConfig.VERSION_NAME
        val currentVersion = Scanner(versionName.replace("\\D+".toRegex(), "")).nextInt()
        //
        Timber.d("currentVersion${currentVersion + 1} fetch $fetchUpdateVersion")
        // 현재 버전이 왜 103이지
        if (fetchUpdateVersion > currentVersion) {
            checkForce(force, fetchUpdateVersion)
        }
    }

    private fun checkForce(force: Boolean, fetchUpdateVersion: Int) {
        when (force) {
            true -> { // 강제업데이트인 경우
                forceUpdateDialog(fetchUpdateVersion)
            }

            false -> {
                if (!SharedPreferences.getBoolean(CHECK_SHOW_UPDATE_DIALOG)) appUpdateDialog()
            }
        }
    }

    private fun forceUpdateDialog(fetchUpdateVersion: Int) {
        // 강제업데이트 인 경우 확인을 누르면 가고
        // 취소를 누르면 종료
        val title = formatData(fetchUpdateVersion)

        AlertDialog.Builder(this)
            .setTitle(title)
            .setPositiveButton(R.string.ok) { _, _ ->
                "ok"
                openUpdatePage()
                finishAffinity()
            }
            .setNegativeButton(R.string.cancel) { _, _ ->
                "cancel"
                finishAffinity()
            }
            .show()
    }

    private fun formatData(fetchUpdateVersion: Int): String {
        val versionCode = listOf(
            fetchUpdateVersion / 100,
            (fetchUpdateVersion / 10) % 10,
            fetchUpdateVersion % 10,
        ).joinToString(separator = ".")
        return resources.getString(R.string.app_version_force_update_title, versionCode)
    }

    private fun appUpdateDialog() {
        AlertDialog.Builder(this)
            .setTitle(R.string.app_version_update_title)
            .setPositiveButton(R.string.ok) { _, _ -> "ok" }
            .setNegativeButton(R.string.cancel) { _, _ ->
                "cancel"
                SharedPreferences.setBoolean(
                    CHECK_SHOW_UPDATE_DIALOG,
                    true,
                )
            }
            .show()
    }

    private fun openUpdatePage() {
        navigateToGooglePlayStore(this.packageName)
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
                if (destination.id == R.id.homeFragment && viewModel.isFirstEntryToHomeFragment) {
                    showCommonDialog()
                    viewModel.isFirstEntryToHomeFragment = false
                }
            }
        }
    }

    private fun showCommonDialog() {
        if (SharedPreferences.getBoolean(PublicString.STOP_WATCHING_COMMON_DIALOG)) return
        navController.navigate(R.id.action_homeFragment_to_commonDialogFragment)
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
        const val REQUEST_PHONE_STATE_OR_NUMBERS_CODE = 0
        const val CHECK_SHOW_UPDATE_DIALOG = "CHECK_SHOW_UPDATE_DIALOG"
        private const val REMOTE_KEY_APP_INFO = "app_info"
    }
}
