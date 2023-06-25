package kr.co.nottodo.presentation.recommendation.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kr.co.nottodo.data.remote.model.recommendation.mission.ResponseRecommendationMissionListDto.Mission
import kr.co.nottodo.databinding.ItemRecommendationMainBinding
import kr.co.nottodo.util.DiffUtilItemCallback

class RecommendationMissionAdapter :
    ListAdapter<Mission, RecommendationMissionAdapter.RecommendationMissionViewHolder>(
        diffUtil
    ) {

    private var itemClickListener: OnItemClickListener? = null

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): RecommendationMissionViewHolder {
        val binding = ItemRecommendationMainBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return RecommendationMissionViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecommendationMissionViewHolder, position: Int) {
        holder.onBind(getItem(position), itemClickListener)
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.itemClickListener = listener
    }

    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }

    fun getItemAtPosition(position: Int): Mission {
        return getItem(position)
    }

    class RecommendationMissionViewHolder(private val binding: ItemRecommendationMainBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun onBind(data: Mission, listener: OnItemClickListener?) {
            binding.tvWhen.text = data.situation
            binding.tvRecommendationCategory.text = data.title
            binding.tvRecommendationCategoryDescription.text = data.description

            Glide.with(binding.root).load(data.image).into(binding.ivRecommendationCategory)

            binding.layoutRecommendationCategory.setOnClickListener {
                listener?.onItemClick(adapterPosition)
            }
        }
    }

    companion object {
        val diffUtil =
            DiffUtilItemCallback<Mission>(onItemsTheSame = { old, new -> old === new },
                onContentsTheSame = { old, new -> old == new })
    }
}