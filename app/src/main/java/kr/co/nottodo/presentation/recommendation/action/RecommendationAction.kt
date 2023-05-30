package kr.co.nottodo.presentation.recommendation.action

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import kr.co.nottodo.R

class RecommendationAction : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_recommendation_action_title)

        // 데이터 가져오기
        val situation = intent.getStringExtra("situation")
        val title = intent.getStringExtra("title")

        // 데이터 화면에 표시
        findViewById<TextView>(R.id.tv_recommendation_action_when).text = situation
        findViewById<TextView>(R.id.tv_recommendation_action).text = title

    }

}