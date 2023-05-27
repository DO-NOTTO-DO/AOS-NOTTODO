package kr.co.nottodo.presentation.recommendation.main

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kr.co.nottodo.R

class RecommendationMainListViewHolder (item_recommendation_category: View) : RecyclerView.ViewHolder(item_recommendation_category) {
    // 아이템 뷰의 구성 요소에 대한 참조 변수 선언
    private val whenTextView: TextView = item_recommendation_category.findViewById(R.id.tv_when)
    private val titleTextView: TextView = item_recommendation_category.findViewById(R.id.tv_recommendation_category)
    private val descriptionTextView: TextView = item_recommendation_category.findViewById(R.id.tv_recommendation_category_description)
    private val imageView: ImageView = item_recommendation_category.findViewById(R.id.iv_recommendation_category)

    // 데이터를 아이템 뷰에 바인딩하는 메소드
//    fun bindData(data: RecommendationMainListDTO) {
//        whenTextView.text = recommendation.title
//        titleTextView.text = recommendation.title
//        descriptionTextView.text = recommendation.description
//        imageView.setImageResource(recommendation.imageResId)
//    }
}