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
        setContentView(binding.root)

        setClickEvents()
    }

    private fun setClickEvents() {
        setLogoutTvClickEvent()
        setBackIvClickEvent()
        setMemberWithdrawalTvClickEvent()
    }

    private fun setMemberWithdrawalTvClickEvent() {
        binding.tvMyPageInformationMemberWithdrawal.setOnClickListener {
            WithdrawalDialogFragment().show(supportFragmentManager, "WithdrawalDialog")
        }
    }

    private fun setBackIvClickEvent() {
        binding.ivMyPageInformationBackArrow.setOnClickListener {
            if (!isFinishing) finish()
        }
    }

    private fun setLogoutTvClickEvent() {
        binding.tvMyPageInformationLogout.setOnClickListener {
            logout()
        }
    }

    private fun logout() {
        SharedPreferences.clearForLogout()
        startActivity(Intent(this, LoginActivity::class.java))
        if (!isFinishing) finish()
    }
}












