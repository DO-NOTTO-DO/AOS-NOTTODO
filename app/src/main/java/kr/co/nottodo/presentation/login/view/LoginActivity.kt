package kr.co.nottodo.presentation.login.view

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.messaging.FirebaseMessaging
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.model.ClientError
import com.kakao.sdk.common.model.ClientErrorCause
import com.kakao.sdk.user.UserApiClient
import kr.co.nottodo.MainActivity
import kr.co.nottodo.data.local.SharedPreferences
import kr.co.nottodo.databinding.ActivityLoginBinding
import kr.co.nottodo.presentation.login.viewmodel.LoginViewModel
import kr.co.nottodo.presentation.onboard.view.OnboardActivity
import kr.co.nottodo.util.showToast
import timber.log.Timber

class LoginActivity : AppCompatActivity() {
    lateinit var binding: ActivityLoginBinding
    private val viewModel by viewModels<LoginViewModel>()
    private var tokenId: String? = null  //Google Auth 인증에 성공하면 token 값으로 설정된다

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        showOnboardForFirstUser()
        setAutoLogin()
        setKakaoLogin()
        setFCMToken()
        observeGetTokenResult()
    }

    private fun setFCMToken() {
        if (SharedPreferences.getString(FCM_TOKEN).isNullOrBlank()) {
            FirebaseMessaging.getInstance().token.addOnSuccessListener { token ->
                SharedPreferences.setString(FCM_TOKEN, token)
            }
        }
    }

    private fun showOnboardForFirstUser() {
        if (!SharedPreferences.getBoolean(DID_USER_WATCHED_ONBOARD)) startActivity(
            Intent(
                this, OnboardActivity::class.java
            )
        )
    }

    private fun setAutoLogin() {
        if (!SharedPreferences.getString(USER_TOKEN).isNullOrBlank()) {
            startActivity(Intent(this, MainActivity::class.java))
            if (!isFinishing) finish()
        }
    }

    private fun observeGetTokenResult() {
        viewModel.getTokenResult.observe(this) {
            startActivity(Intent(this, MainActivity::class.java))
            SharedPreferences.setString(USER_TOKEN, it.data.accessToken)
            if (!isFinishing) finish()
        }
        viewModel.getErrorResult.observe(this) {
            UserApiClient.instance.logout { showToast("오류 발생, 다시 로그인 해주세요. 사유: ${it.toString()}") }
        }
    }

    private fun setKakaoLogin() {
        val kakaoLoginCallback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
            if (error != null) {
                Timber.e("로그인 실패 $error")
            } else if (token != null) {
                viewModel.setSocialToken(token.accessToken)
                viewModel.getToken()
            }
        }

        binding.layoutLoginKakao.setOnClickListener {
            if (UserApiClient.instance.isKakaoTalkLoginAvailable(this)) {
                // 카카오톡 로그인
                UserApiClient.instance.loginWithKakaoTalk(this) { token, error ->
                    // 로그인 실패 부분
                    if (error != null) {
                        Timber.e("로그인 실패 $error")
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
                        viewModel.setSocialToken(token.accessToken)
                        viewModel.getToken()
                    }
                }
            } else {
                Timber.d("카카오톡이 설치되어 있지 않습니다.")
                // 카카오 이메일 로그인
                UserApiClient.instance.loginWithKakaoAccount(
                    this, callback = kakaoLoginCallback
                )
            }
        }
    }

    companion object {
        const val KAKAO: String = "KAKAO"
        const val USER_TOKEN = "USER_TOKEN"
        const val FCM_TOKEN = "FCM_TOKEN"
        const val DID_USER_WATCHED_ONBOARD = "DID_USER_WATCHED_ONBOARD"
    }
}