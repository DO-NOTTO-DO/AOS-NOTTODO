package kr.co.nottodo.presentation.recommendation.action

import RecommendationActionListDTO
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import kr.co.nottodo.R
import kr.co.nottodo.databinding.ItemRecommendationActionTitleBinding


class RecommendationActionTitleAdapter(
    private val childItemClick: (
        view: View, childData: RecommendationActionListDTO.ActionList.CategoryList
    ) -> Unit
) : ListAdapter<RecommendationActionListDTO.ActionList, RecommendationActionTitleViewHolder>(
    ParentDiffUtilItemCallback
) {


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecommendationActionTitleViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding: ItemRecommendationActionTitleBinding =
            DataBindingUtil.inflate(
                layoutInflater,
                R.layout.item_recommendation_action_title,
                parent,
                false
            )
        return RecommendationActionTitleViewHolder(
            binding
        ) { view, childData ->
            childItemClick(view, childData)
        }
    }

    override fun onBindViewHolder(holder: RecommendationActionTitleViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}

object ParentDiffUtilItemCallback :
    DiffUtil.ItemCallback<RecommendationActionListDTO.ActionList>() {
    override fun areItemsTheSame(
        oldItem: RecommendationActionListDTO.ActionList,
        newItem: RecommendationActionListDTO.ActionList
    ): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(
        oldItem: RecommendationActionListDTO.ActionList,
        newItem: RecommendationActionListDTO.ActionList
    ): Boolean {
        return oldItem == newItem
    }
}