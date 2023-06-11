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
        val binding = ItemRecommendationMainBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return RecommendationMainListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecommendationMainListViewHolder, position: Int) {
        holder.onBind(currentList[position], itemClickListener)
    }


    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.itemClickListener = listener
    }

    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }

    fun getItemAtPosition(position: Int): RecommendationMainListDTO.MainList {
        return currentList[position]
    }

    class RecommendationMainListViewHolder(private val binding: ItemRecommendationMainBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun onBind(data: RecommendationMainListDTO.MainList, listener: OnItemClickListener?) {
            binding.tvWhen.text = data.situation
            binding.tvRecommendationCategory.text = data.title
            binding.tvRecommendationCategoryDescription.text = data.description

            binding.layoutRecommendationCategory.setOnClickListener {
                listener?.onItemClick(adapterPosition)
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
