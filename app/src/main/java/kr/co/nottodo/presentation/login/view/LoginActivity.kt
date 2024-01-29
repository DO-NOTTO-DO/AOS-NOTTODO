package kr.co.nottodo.presentation.login.view

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.google.firebase.messaging.FirebaseMessaging
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.model.ClientError
import com.kakao.sdk.common.model.ClientErrorCause
import com.kakao.sdk.user.UserApiClient
import kotlinx.coroutines.launch
import kr.co.nottodo.MainActivity
import kr.co.nottodo.R
import kr.co.nottodo.data.local.SharedPreferences
import kr.co.nottodo.databinding.ActivityLoginBinding
import kr.co.nottodo.presentation.login.viewmodel.LoginViewModel
import kr.co.nottodo.presentation.onboard.view.OnboardActivity
import kr.co.nottodo.util.NotTodoAmplitude.setAmplitudeUserId
import kr.co.nottodo.util.NotTodoAmplitude.trackEvent
import kr.co.nottodo.util.NotTodoAmplitude.trackEventWithProperty
import kr.co.nottodo.util.showToast
import timber.log.Timber

class LoginActivity : AppCompatActivity() {
    private val binding by lazy { ActivityLoginBinding.inflate(layoutInflater) }
    private val viewModel by viewModels<LoginViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setViews()
        showOnboardForFirstUser()
        setAutoLogin()
        observeGetTokenResult()
        setClickEvents()
    }

    private fun setViews() {
        setContentView(binding.root)
    }

    private fun setClickEvents() {
        setGoogleLoginBtnClickEvent()
        setKakaoLoginBtnClickEvent()
        setTermsOfUseTvClickEvent()
        setPrivacyPolicyTvClickEvent()
    }

    private fun setTermsOfUseTvClickEvent() {
        binding.tvLoginTermsOfUse.setOnClickListener {
            val intent = Intent(
                Intent.ACTION_VIEW, Uri.parse(getString(R.string.url_terms_of_use))
            )
            startActivity(intent)
        }
    }

    private fun setPrivacyPolicyTvClickEvent() {
        binding.tvLoginPrivacyPolicy.setOnClickListener {
            val intent = Intent(
                Intent.ACTION_VIEW, Uri.parse(getString(R.string.url_privacy_policy))
            )
            startActivity(intent)
        }
    }


    private fun setGoogleLoginBtnClickEvent() {
        binding.layoutLoginGoogle.setOnClickListener {
            showToast(getString(R.string.google_sign_in_later))
        }
    }

    private fun setKakaoLoginBtnClickEvent() {
        val kakaoLoginCallback: (OAuthToken?, Throwable?) -> Unit = { socialToken, error ->
            if (error != null) {
                Timber.e(getString(R.string.failure_kakao_login, error))
            } else if (socialToken != null) {
                FirebaseMessaging.getInstance().token.addOnSuccessListener { fcmToken ->
                    viewModel.login(socialToken = socialToken.accessToken, fcmToken = fcmToken)
                }
            }
        }

        binding.layoutLoginKakao.setOnClickListener {
            trackEventWithProperty(
                getString(R.string.click_signin), getString(R.string.provider), getString(
                    R.string.kakao
                )
            )
            if (UserApiClient.instance.isKakaoTalkLoginAvailable(this)) {
                // 카카오톡 로그인
                UserApiClient.instance.loginWithKakaoTalk(this) { token, error ->
                    // 로그인 실패 부분
                    if (error != null) {
                        Timber.e(getString(R.string.failure_kakao_login, error))
                        // 사용자 취소
                        if (error is ClientError && error.reason == ClientErrorCause.Cancelled) {
                            return@loginWithKakaoTalk
                        }
                        // 다른 오류 - 카톡으로 안 될 경우
                        else {
                            UserApiClient.instance.loginWithKakaoAccount(
                                this, callback = kakaoLoginCallback
                            ) // 카카오 이메일 로그인
                        }
                    }
                    // 로그인 성공 부분
                    else if (token != null) {
                        kakaoLoginCallback.invoke(token, error)
                    }
                }
            } else {
                Timber.d(getString(R.string.kakao_talk_not_installed))
                // 카카오 이메일 로그인
                UserApiClient.instance.loginWithKakaoAccount(
                    this, callback = kakaoLoginCallback
                )
            }
        }
    }

    private fun showOnboardForFirstUser() {
        if (!SharedPreferences.getBoolean(DID_USER_WATCHED_ONBOARD)) {
            startActivity(
                Intent(
                    this, OnboardActivity::class.java
                )
            )
            if (!isFinishing) finish()
        }
    }

    private fun setAutoLogin() {
        if (!SharedPreferences.getString(USER_TOKEN).isNullOrBlank()) {
            startActivity(Intent(this, MainActivity::class.java))
            if (!isFinishing) finish()
        } else {
            trackEvent(getString(R.string.view_signin))
        }
    }

    private fun observeGetTokenResult() {

        lifecycleScope.launch {
            viewModel.getTokenResult.collect { response ->
                trackEventWithProperty(
                    getString(R.string.complete_signin), getString(R.string.provider), getString(
                        R.string.kakao
                    )
                )
                setAmplitudeUserId(response.data.userId)
                startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                setUserInfo(response.data.accessToken)
                if (!isFinishing) finish()
            }
        }
        viewModel.getErrorResult.observe(this) {
            UserApiClient.instance.logout { showToast(getString(R.string.error_login_again_please)) }
        }
    }

    private fun setUserInfo(token: String) {
        SharedPreferences.apply {
            setString(USER_TOKEN, token)
            UserApiClient.instance.me { user, error ->
                if (error == null && user != null) {
                    setString(USER_EMAIL, user.kakaoAccount?.email)
                    setString(USER_NAME, user.kakaoAccount?.profile?.nickname)
                }
            }
        }
    }

    companion object {
        const val KAKAO: String = "KAKAO"
        const val USER_TOKEN = "USER_TOKEN"
        const val DID_USER_WATCHED_ONBOARD = "DID_USER_WATCHED_ONBOARD"
        const val USER_NAME = "USER_NAME"
        const val USER_EMAIL = "USER_EMAIL"
        const val DID_USER_CHOOSE_TO_BE_NOTIFIED = "DID_USER_CHOOSE_TO_BE_NOTIFIED"
    }
}