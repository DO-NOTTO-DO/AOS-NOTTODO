package kr.co.nottodo.presentation.recommendation.action

import RecommendationActionListDTO
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kr.co.nottodo.databinding.ItemRecommendationActionTitleBinding

class RecommendationActionTitleViewHolder (
    private val binding: ItemRecommendationActionTitleBinding,
    private val testChildItemViewClickBlock : (view: View, childData: RecommendationActionListDTO.ActionList.CategoryList) -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    private lateinit var parentData: RecommendationActionListDTO.ActionList

    private val childAdapter: ActionCategoryAdapter = ActionCategoryAdapter(
        childClickListener = { view, childData ->
            testChildItemViewClickBlock.invoke(view,childData)
        }
    )

    init {
        initRecyclerView()
    }

    private fun initRecyclerView() {
        binding.rvRecommendationActionCategory.apply {
            adapter = childAdapter
            layoutManager = LinearLayoutManager(context)
        }
    }

    fun bind(data:RecommendationActionListDTO.ActionList) {
        parentData = data
        childAdapter.submitList(data.recommendActions)
        binding.apply {
            this.tvRecommendationActionTitle.text = data.title
            executePendingBindings()
        }
    }
}