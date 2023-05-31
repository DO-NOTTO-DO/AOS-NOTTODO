package kr.co.nottodo.presentation.recommendation.main

import RecommendationMainListViewAdapter
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kr.co.nottodo.R
import kr.co.nottodo.presentation.recommendation.action.RecommendationAction

class RecommendationMain : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recommendation_main)

        // RecyclerView 가져오기
        val recyclerView = findViewById<RecyclerView>(R.id.rv_recommendation)

        // LinearLayoutManager 생성 및 설정
        val layoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager

        // 어댑터 인스턴스 생성
        val adapter = RecommendationMainListViewAdapter()

        // RecyclerView에 어댑터 설정
        recyclerView.adapter = adapter

        val exitButton = findViewById<View>(R.id.iv_recommendation_main_exit)
        exitButton.setOnClickListener { finish() }

        // RecyclerView 아이템 클릭 이벤트 처리
        // RecyclerView 아이템 클릭 이벤트 처리
        adapter.setOnItemClickListener(object : RecommendationMainListViewAdapter.OnItemClickListener {
            override fun onItemClick(position: Int) {
                val item = adapter.getItemAtPosition(position)
                val intent = Intent(this@RecommendationMain, RecommendationAction::class.java)
                intent.putExtra("situation", item.situation)
                intent.putExtra("title", item.title)
                startActivity(intent)
            }
        })

    }
}


//        val categoryCoffeeLayout = findViewById<View>(R.id.layout_recommendation_category_coffee)
//        categoryCoffeeLayout.setOnClickListener {
//            val intent = Intent(this, RecommedationAction::class.java)
//            startActivity(intent)
//        }
//
//        val categoryPhoneLayout = findViewById<View>(R.id.layout_recommendation_category_phone)
//        categoryPhoneLayout.setOnClickListener {
//            val intent = Intent(this, RecommedationAction::class.java)
//            startActivity(intent)
//        }

