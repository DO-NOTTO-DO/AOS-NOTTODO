package kr.co.nottodo.presentation.recommend.mission.adapter

import android.graphics.Rect
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration
import coil.load
import kr.co.nottodo.data.remote.model.recommendation.mission.ResponseRecommendMissionListDto.Mission
import kr.co.nottodo.databinding.ItemRecommendMissionBinding
import kr.co.nottodo.util.DiffUtilItemCallback
import kr.co.nottodo.util.dpToPx

class RecommendMissionAdapter(private val startRecommendActionActivity: (Int, String, String, String) -> Unit) :
    ListAdapter<Mission, RecommendMissionAdapter.RecommendMissionViewHolder>(
        diffUtil
    ) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): RecommendMissionViewHolder {
        val binding =
            ItemRecommendMissionBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RecommendMissionViewHolder(binding, startRecommendActionActivity)
    }

    override fun onBindViewHolder(holder: RecommendMissionViewHolder, position: Int) {
        holder.onBind(getItem(position))
    }

    class RecommendMissionViewHolder(
        private val binding: ItemRecommendMissionBinding,
        private val startRecommendActionActivity: (Int, String, String, String) -> Unit,
    ) : RecyclerView.ViewHolder(binding.root) {
        fun onBind(data: Mission) {
            with(binding) {
                tvRecommendationMissionSituation.text = data.situation
                tvRecommendationMission.text = data.title
                tvRecommendationMissionDesc.text = data.description
                ivRecommendationMission.load(data.image)

                layoutRecommendationMission.setOnClickListener {
                    startRecommendActionActivity.invoke(
                        data.id, data.title, data.situation, data.image
                    )
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
        val diffUtil = DiffUtilItemCallback<Mission>(onItemsTheSame = { old, new -> old === new },
            onContentsTheSame = { old, new -> old == new })
    }
}