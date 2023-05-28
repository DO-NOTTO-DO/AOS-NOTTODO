package kr.co.nottodo.presentation.recommendation.Action

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import kr.co.nottodo.R

class RecommendationAction : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recommendation_action)

        // 데이터 가져오기
        val situation = intent.getStringExtra("situation")
        val title = intent.getStringExtra("title")
        val description = intent.getStringExtra("description")

        // 데이터 화면에 표시
        findViewById<TextView>(R.id.tv_when_youtube).text = situation
        findViewById<TextView>(R.id.tv_recommendation_youtube).text = title

    }
}