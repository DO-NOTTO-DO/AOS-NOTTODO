package kr.co.nottodo.presentation.recommendation.action

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import kr.co.nottodo.R
import kr.co.nottodo.databinding.ItemRecommendationActionCategoryBinding

class RecommendationActionListAdapter :
    ListAdapter<RecommendationActionListDTO.ActionList, RecommendationActionListAdapter.RecommendationActionListViewHolder>(
        diffUtilCallback
    ) {

    private var itemClickListener: OnItemClickListener? = null
    private var selectedItemPosition: Int = RecyclerView.NO_POSITION

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecommendationActionListViewHolder {
        val binding = ItemRecommendationActionCategoryBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return RecommendationActionListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecommendationActionListViewHolder, position: Int) {
        holder.onBind(getItem(position), position, itemClickListener, selectedItemPosition)
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.itemClickListener = listener
    }

    fun setSelectedItem(position: Int) {
        selectedItemPosition = position
    }

    companion object {
        private val diffUtilCallback = object : DiffUtil.ItemCallback<RecommendationActionListDTO.ActionList>() {
            override fun areItemsTheSame(
                oldItem: RecommendationActionListDTO.ActionList,
                newItem: RecommendationActionListDTO.ActionList
            ): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(
                oldItem: RecommendationActionListDTO.ActionList,
                newItem: RecommendationActionListDTO.ActionList
            ): Boolean {
                return oldItem == newItem
            }
        }
    }

    class RecommendationActionListViewHolder(private val binding: ItemRecommendationActionCategoryBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun onBind(
            data: RecommendationActionListDTO.ActionList,
            position: Int,
            listener: OnItemClickListener?,
            selectedItemPosition: Int
        ) {
            binding.tvRecommendationActionCategory.text = data.title

            if (position == selectedItemPosition) {
                // 선택된 아이템인 경우의 처리
                val selectLayout = LayoutInflater.from(binding.root.context).inflate(R.layout.item_recommendation_action_select, null)
                binding.root.background = selectLayout.background
            } else {
                // 선택되지 않은 아이템인 경우의 처리
                val categoryLayout = LayoutInflater.from(binding.root.context).inflate(R.layout.item_recommendation_action_category, null)
                binding.root.background = categoryLayout.background
            }

            binding.root.setOnClickListener {
                listener?.onItemClick(adapterPosition)
            }
        }
    }

    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }
}
