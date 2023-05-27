package kr.co.nottodo.presentation.recommendation.main

import RecommendationMainListViewHolder
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kr.co.nottodo.R

class RecommendationMainListViewAdapter : RecyclerView.Adapter<RecommendationMainListViewHolder>() {
    private var dataList: List<RecommendationMainListDTO.MainList> = emptyList()

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecommendationMainListViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_recommendation_category, parent, false)
        return RecommendationMainListViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecommendationMainListViewHolder, position: Int) {
        val data = dataList[position]
        holder.bindData(data)
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    fun setData(dataList: List<RecommendationMainListDTO.MainList>) {
        this.dataList = dataList
        notifyDataSetChanged()
    }
}

class RecommendationMainListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    // ViewHolder 구현
    // ...
}
