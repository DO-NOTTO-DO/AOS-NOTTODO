package kr.co.nottodo.presentation.login.view

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.messaging.FirebaseMessaging
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.model.ClientError
import com.kakao.sdk.common.model.ClientErrorCause
import com.kakao.sdk.user.UserApiClient
import kr.co.nottodo.MainActivity
import kr.co.nottodo.R
import kr.co.nottodo.data.local.SharedPreferences
import kr.co.nottodo.databinding.ActivityLoginBinding
import kr.co.nottodo.presentation.login.viewmodel.LoginViewModel
import kr.co.nottodo.presentation.onboard.view.OnboardActivity
import kr.co.nottodo.util.showToast
import timber.log.Timber

class LoginActivity : AppCompatActivity() {
    private val binding by lazy { ActivityLoginBinding.inflate(layoutInflater) }
    private val viewModel by viewModels<LoginViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        showOnboardForFirstUser()
        setAutoLogin()
        observeGetTokenResult()
        setClickEvents()
    }

    private fun setClickEvents() {
        setGoogleLoginBtnClickEvent()
        setKakaoLoginBtnClickEvent()
        setTermsOfUseTVClickEvent()
        setPrivacyPolicyTVClickEvent()
    }

    private fun setTermsOfUseTVClickEvent() {
        binding.tvLoginTermsOfUse.setOnClickListener {
            val intent = Intent(
                Intent.ACTION_VIEW, Uri.parse(getString(R.string.url_terms_of_use))
            )
            startActivity(intent)
        }
    }

    private fun setPrivacyPolicyTVClickEvent() {
        binding.tvLoginPrivacyPolicy.setOnClickListener {
            val intent = Intent(
                Intent.ACTION_VIEW, Uri.parse(getString(R.string.url_privacy_policy))
            )
            startActivity(intent)
        }
    }


    private fun setGoogleLoginBtnClickEvent() {
        binding.layoutLoginGoogle.setOnClickListener {
            showToast("구글 로그인은 추후 업데이트 예정입니다")
        }
    }

    private fun setKakaoLoginBtnClickEvent() {
        val kakaoLoginCallback: (OAuthToken?, Throwable?) -> Unit = { socialToken, error ->
            if (error != null) {
                Timber.e("로그인 실패 $error")
            } else if (socialToken != null) {
                FirebaseMessaging.getInstance().token.addOnSuccessListener { fcmToken ->
                    viewModel.login(socialToken = socialToken.accessToken, fcmToken = fcmToken)
                }
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
                        kakaoLoginCallback.invoke(token, error)
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
        viewModel.getTokenResult.observe(this) { response ->
            startActivity(Intent(this, MainActivity::class.java))
            setUserInfo(response.data.accessToken)
            if (!isFinishing) finish()
        }
        viewModel.getErrorResult.observe(this) {
            UserApiClient.instance.logout { showToast("오류 발생, 다시 로그인 해주세요.") }
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