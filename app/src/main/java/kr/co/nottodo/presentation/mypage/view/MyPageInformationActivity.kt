package kr.co.nottodo.presentation.mypage.view

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kr.co.nottodo.data.local.SharedPreferences
import kr.co.nottodo.databinding.ActivityMyPageInformationBinding
import kr.co.nottodo.presentation.login.view.LoginActivity

class MyPageInformationActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMyPageInformationBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMyPageInformationBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        // tv_member_withdrawal 텍스트뷰 클릭 시 WithdrawalDialogFragment 호출
        binding.tvMemberWithdrawal.setOnClickListener {
            WithdrawalDialogFragment().show(supportFragmentManager, "WithdrawalDialog")
        }

        binding.ivMyPageInformationArrow.setOnClickListener {
            if (!isFinishing) finish()
        }

        binding.layoutLogout.setOnClickListener {
            logout()
        }
    }

    private fun logout() {
        SharedPreferences.clearForLogout()
        startActivity(Intent(this, LoginActivity::class.java))
        if (!isFinishing) finish()
    }
}












