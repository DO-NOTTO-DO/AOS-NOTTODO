package kr.co.nottodo.presentation.recommend.mission.adapter

import android.graphics.Rect
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration
import coil.load
import kr.co.nottodo.databinding.ItemRecommendMissionBinding
import kr.co.nottodo.domain.entity.recommend.RecommendMissionDomainModel
import kr.co.nottodo.util.DiffUtilItemCallback
import kr.co.nottodo.util.dpToPx

class RecommendMissionAdapter(private val navigateToRecommendActionFragment: (RecommendMissionDomainModel.Mission) -> Unit) :
    ListAdapter<RecommendMissionDomainModel.Mission, RecommendMissionAdapter.RecommendMissionViewHolder>(
        diffUtil
    ) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): RecommendMissionViewHolder {
        val binding =
            ItemRecommendMissionBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RecommendMissionViewHolder(binding, navigateToRecommendActionFragment)
    }

    override fun onBindViewHolder(holder: RecommendMissionViewHolder, position: Int) {
        holder.onBind(getItem(position))
    }

    class RecommendMissionViewHolder(
        private val binding: ItemRecommendMissionBinding,
        private val navigateToRecommendActionFragment: (RecommendMissionDomainModel.Mission) -> Unit,
    ) : RecyclerView.ViewHolder(binding.root) {
        fun onBind(data: RecommendMissionDomainModel.Mission) {
            with(binding) {
                tvRecommendationMission.text = data.missionTitle
                tvRecommendationMissionDesc.text = data.missionDesc
                tvRecommendationMissionSituation.text = data.situation
                ivRecommendationMission.load(data.imageUrl)

                layoutRecommendationMission.setOnClickListener {
                    navigateToRecommendActionFragment.invoke(data)
                }
            }
        }
    }

    class RecommendMissionItemDecoration() : ItemDecoration() {
        override fun getItemOffsets(
            outRect: Rect,
            view: View,
            parent: RecyclerView,
            state: RecyclerView.State,
        ) {
            super.getItemOffsets(outRect, view, parent, state)

            val position = parent.getChildAdapterPosition(view)
            val itemCount = parent.adapter?.itemCount ?: 0

            if (position == 0) {
                outRect.top = view.context.dpToPx(6)
            }
            if (position == itemCount - 1) {
                outRect.bottom = view.context.dpToPx(82)
            }
        }
    }

    companion object {
        val diffUtil =
            DiffUtilItemCallback<RecommendMissionDomainModel.Mission>(onItemsTheSame = { old, new -> old === new },
                onContentsTheSame = { old, new -> old == new })
    }
}