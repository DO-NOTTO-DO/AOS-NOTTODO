package kr.co.nottodo.presentation.mypage.view

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout

import kr.co.nottodo.R
import kr.co.nottodo.databinding.ActivityMyPageInformationBinding

class MyPageInformationActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMyPageInformationBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMyPageInformationBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        // tv_member_withdrawal 텍스트뷰 클릭 시 WithdrawalDialogFragment 호출
        binding.tvMemberWithdrawal.setOnClickListener {
            val withdrawalDialogFragment = WithdrawalDialogFragment()
            withdrawalDialogFragment.show(supportFragmentManager, "WithdrawalDialog")
        }
        val arrow =
            findViewById<ImageView>(R.id.iv_my_page_information_arrow)
        arrow.setOnClickListener {
            val intent = Intent(this, MyPageFragment::class.java).apply {
                putExtra("mypage", "fragment")
            }
            startActivity(intent)
        }
    }
}












