package kr.co.nottodo.presentation.recommendation.action

import RecommendationActionListDTO
import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import kr.co.nottodo.R
import kr.co.nottodo.databinding.ItemRecommendationActionCategoryBinding

class ActionCategoryAdapter (
    private val childClickListener: (view: View, childData: RecommendationActionListDTO.ActionList.CategoryList) -> Unit
) : RecyclerView.Adapter<ActionCategoryAdapter.RecommendationActionCategoryViewHolder>() {

    private val childItems = mutableListOf<RecommendationActionListDTO.ActionList.CategoryList>()

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecommendationActionCategoryViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding:ItemRecommendationActionCategoryBinding =
            DataBindingUtil.inflate(
                layoutInflater,
                R.layout.item_recommendation_action_category,
                parent,
                false
            )
        return RecommendationActionCategoryViewHolder(binding, childClickListener)
    }

    override fun onBindViewHolder(holder: RecommendationActionCategoryViewHolder, position: Int) {
        holder.bind(childItems[position])
    }

    override fun getItemCount(): Int = childItems.size

    @SuppressLint("NotifyDataSetChanged")
    fun submitList(list: List<RecommendationActionListDTO.ActionList.CategoryList>) {
        childItems.clear()
        childItems.addAll(list)
        notifyDataSetChanged()
    }

    inner class RecommendationActionCategoryViewHolder(
        private val binding: ItemRecommendationActionCategoryBinding,
        private val childClickListener: (view: View, childData: RecommendationActionListDTO.ActionList.CategoryList) -> Unit
    ) : RecyclerView.ViewHolder(binding.root), View.OnClickListener {

        private lateinit var childData: RecommendationActionListDTO.ActionList.CategoryList

        init {
            binding.root.setOnClickListener(this)
        }

        fun bind(data: RecommendationActionListDTO.ActionList.CategoryList) {
            childData = data
            // Bind data to the views in the binding
            // Example: binding.textView.text = data.title
        }

        override fun onClick(view: View) {
            childClickListener.invoke(view, childData)
        }
    }
}
