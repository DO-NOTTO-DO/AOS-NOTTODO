import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import kr.co.nottodo.databinding.ItemRecommendationMainBinding
import kr.co.nottodo.presentation.recommendation.action.RecommendationAction


class RecommendationMainListViewAdapter :
    ListAdapter<RecommendationMainListDTO.MainList, RecommendationMainListViewAdapter.RecommendationMainListViewHolder>(
        diffUtil
    ) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecommendationMainListViewHolder {
        val binding = ItemRecommendationMainBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return RecommendationMainListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecommendationMainListViewHolder, position: Int) {
        holder.onBind(currentList[position])
    }


    class RecommendationMainListViewHolder(private val binding: ItemRecommendationMainBinding) :
        RecyclerView.ViewHolder(binding.root) {

        // 데이터를 아이템 뷰에 바인딩하는 메소드
        fun onBind(data: RecommendationMainListDTO.MainList) {
            binding.tvWhen.text = data.situation
            binding.tvRecommendationCategory.text = data.title
            binding.tvRecommendationCategoryDescription.text = data.description

            // layout_recommendation_category 클릭 이벤트 처리
            binding.layoutRecommendationCategory.setOnClickListener {
                val intent =
                    Intent(binding.root.context, RecommendationAction::class.java)
                intent.putExtra("situation", data.situation)
                intent.putExtra("title", data.title)
                binding.root.context.startActivity(intent)
            }
        }
    }


    companion object {
        private val diffUtil =
            object : DiffUtil.ItemCallback<RecommendationMainListDTO.MainList>() {
                override fun areItemsTheSame(
                    oldItem: RecommendationMainListDTO.MainList,
                    newItem: RecommendationMainListDTO.MainList
                ): Boolean {
                    return oldItem.id == newItem.id
                }

                override fun areContentsTheSame(
                    oldItem: RecommendationMainListDTO.MainList,
                    newItem: RecommendationMainListDTO.MainList
                ): Boolean {
                    return oldItem == newItem
                }
            }
    }
}