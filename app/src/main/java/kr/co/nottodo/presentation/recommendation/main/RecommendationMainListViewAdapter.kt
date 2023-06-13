import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import kr.co.nottodo.databinding.ItemRecommendationMainBinding

class RecommendationMainListViewAdapter :
    ListAdapter<RecommendationMainListDTO.MainList, RecommendationMainListViewAdapter.RecommendationMainListViewHolder>(
        diffUtil
    ) {

    private var itemClickListener: OnItemClickListener? = null

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecommendationMainListViewHolder {
        // 아이템 뷰 바인딩을 위해 LayoutInflater 사용
        val binding = ItemRecommendationMainBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return RecommendationMainListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecommendationMainListViewHolder, position: Int) {
        // ViewHolder에 데이터를 바인딩
        holder.onBind(currentList[position], itemClickListener)
    }

    // 아이템 클릭 리스너 설정
    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.itemClickListener = listener
    }

    // 아이템 클릭 리스너 인터페이스
    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }

    // 지정된 위치의 아이템 반환
    fun getItemAtPosition(position: Int): RecommendationMainListDTO.MainList {
        return currentList[position]
    }

    // ViewHolder 클래스
    class RecommendationMainListViewHolder(private val binding: ItemRecommendationMainBinding) :
        RecyclerView.ViewHolder(binding.root) {

        // ViewHolder에 데이터를 바인딩
        fun onBind(data: RecommendationMainListDTO.MainList, listener: OnItemClickListener?) {
            binding.tvWhen.text = data.situation
            binding.tvRecommendationCategory.text = data.title
            binding.tvRecommendationCategoryDescription.text = data.description

            // 아이템 뷰 클릭 리스너 설정
            binding.layoutRecommendationCategory.setOnClickListener {
                listener?.onItemClick(adapterPosition)
            }
        }
    }

    companion object {
        // DiffUtil을 사용하여 아이템 갱신을 위한 콜백 클래스 정의
        private val diffUtil =
            object : DiffUtil.ItemCallback<RecommendationMainListDTO.MainList>() {
                override fun areItemsTheSame(
                    oldItem: RecommendationMainListDTO.MainList,
                    newItem: RecommendationMainListDTO.MainList
                ): Boolean {
                    // 아이템이 동일한지 확인하기 위해 고유 식별자를 비교
                    return oldItem.id == newItem.id
                }

                override fun areContentsTheSame(
                    oldItem: RecommendationMainListDTO.MainList,
                    newItem: RecommendationMainListDTO.MainList
                ): Boolean {
                    // 아이템 내용이 동일한지 확인
                    return oldItem == newItem
                }
            }
    }
}
