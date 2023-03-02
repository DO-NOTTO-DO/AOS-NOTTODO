package kr.co.nottodo.presentation.login.view

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.model.ClientError
import com.kakao.sdk.common.model.ClientErrorCause
import com.kakao.sdk.user.UserApiClient
import kr.co.nottodo.MainActivity
import kr.co.nottodo.databinding.ActivityLoginBinding
import kr.co.nottodo.presentation.login.viewmodel.LoginViewModel
import timber.log.Timber

class LoginActivity : AppCompatActivity() {
    lateinit var binding: ActivityLoginBinding
    private val viewModel by viewModels<LoginViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.ivLoginLabelKakao.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }

        setKakaoLogin()
    }

    private fun setKakaoLogin() {
        val kakaoLoginCallback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
            if (error != null) {
                Timber.e("로그인 실패", error)
            } else if (token != null) {
                Timber.i("로그인 성공 ${token.accessToken}")
                Timber.i(token.accessToken)
                // 서버에 토큰 달라고 요청
            }
        }
        binding.btnLoginKakao.setOnClickListener {
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
                        Timber.e("로그인 성공 ${token.accessToken}")
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
}