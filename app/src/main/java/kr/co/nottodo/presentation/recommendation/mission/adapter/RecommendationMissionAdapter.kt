package kr.co.nottodo.presentation.recommendation.mission.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import kr.co.nottodo.data.remote.model.recommendation.mission.ResponseRecommendMissionListDto.Mission
import kr.co.nottodo.databinding.ItemRecommendMissionBinding
import kr.co.nottodo.util.DiffUtilItemCallback

class RecommendationMissionAdapter :
    ListAdapter<Mission, RecommendationMissionAdapter.RecommendMissionViewHolder>(
        diffUtil
    ) {


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): RecommendMissionViewHolder {
        val binding =
            ItemRecommendMissionBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RecommendMissionViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecommendMissionViewHolder, position: Int) {
        holder.onBind(getItem(position))
    }

    class RecommendMissionViewHolder(private val binding: ItemRecommendMissionBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun onBind(data: Mission) {
            with(binding) {
                tvRecommendationMissionSituation.text = data.situation
                tvRecommendationMission.text = data.title
                tvRecommendationMissionDesc.text = data.description
                ivRecommendationMission.load(data.image)
            }
        }
    }

    companion object {
        val diffUtil = DiffUtilItemCallback<Mission>(onItemsTheSame = { old, new -> old === new },
            onContentsTheSame = { old, new -> old == new })
    }
}