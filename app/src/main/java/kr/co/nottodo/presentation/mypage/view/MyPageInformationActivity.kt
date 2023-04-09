package kr.co.nottodo.presentation.mypage.view

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import kr.co.nottodo.MainActivity
import kr.co.nottodo.R

class MyPageInformationActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_page_information)


        val arrow =
            findViewById<ImageView>(R.id.iv_my_page_information_arrow)
        arrow.setOnClickListener {
            val intent = Intent(this, MyPageFragment::class.java).apply {
                putExtra("mypage","fragment")
            }
            startActivity(intent)
        }
    }
}
