package kr.co.nottodo.presentation.recommendation.Action

import androidx.recyclerview.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import kr.co.nottodo.R
import kr.co.nottodo.presentation.recommendation.DTO.RecommendationActionListDTO

class RecommendationActionListViewHolder(item_recommendation_action_category: View) : RecyclerView.ViewHolder(item_recommendation_action_category) {
    private val titleTextView: TextView = item_recommendation_action_category.findViewById(R.id.tv_category)


    fun bindData(item: RecommendationActionListDTO) {

        titleTextView.text = item.data.first().title

    }
}