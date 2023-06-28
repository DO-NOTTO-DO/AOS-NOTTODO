package kr.co.nottodo.presentation.recommendation.action.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import kr.co.nottodo.data.remote.model.recommendation.action.ResponseRecommendActionListDTO.Action.Action
import kr.co.nottodo.databinding.ItemRecommendActionBinding
import kr.co.nottodo.presentation.recommendation.action.adapter.RecommendActionAdapter.RecommendActionViewHolder
import kr.co.nottodo.util.DiffUtilItemCallback

class RecommendActionAdapter : ListAdapter<Action, RecommendActionViewHolder>(diffUtil) {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): RecommendActionViewHolder {
        val binding =
            ItemRecommendActionBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RecommendActionViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecommendActionViewHolder, position: Int) {
        holder.onBind(currentList[position])
    }

    companion object {
        val diffUtil = DiffUtilItemCallback<Action>(onItemsTheSame = { old, new -> old === new },
            onContentsTheSame = { old, new -> old == new })
    }

    class RecommendActionViewHolder(private val binding: ItemRecommendActionBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun onBind(item: Action) {
            with(binding) {
                tvRecommendActionTitle.text = item.name
                tvRecommendActionDesc.text = item.description
            }
        }
    }
}