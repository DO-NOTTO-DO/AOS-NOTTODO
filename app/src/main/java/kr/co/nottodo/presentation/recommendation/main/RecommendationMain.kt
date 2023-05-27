package kr.co.nottodo.presentation.recommendation.main

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import kr.co.nottodo.R
import kr.co.nottodo.presentation.recommendation.Action.RecommedationAction

class RecommendationMain : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recommendation_main)


        val categoryYoutubeLayout = findViewById<View>(R.id.rv_recommendation)
        categoryYoutubeLayout.setOnClickListener {
            val intent = Intent(this, RecommedationAction::class.java)
            startActivity(intent)
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


    }


    override fun onStart() {
        super.onStart()

    }


}