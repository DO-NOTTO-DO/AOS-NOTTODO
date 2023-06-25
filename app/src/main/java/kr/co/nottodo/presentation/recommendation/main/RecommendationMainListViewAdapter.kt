import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kr.co.nottodo.databinding.ItemRecommendationMainBinding
import kr.co.nottodo.presentation.home.view.HomeAdpater.Companion.diffUtil
class RecommendationMainListViewAdapter :
    ListAdapter<RecommendationMainListDTO.MainList, RecommendationMainListViewAdapter.RecommendationMainListViewHolder>(
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
        holder.onBind(getItem(position), itemClickListener)
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.itemClickListener = listener
    }

    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }

    fun getItemAtPosition(position: Int): RecommendationMainListDTO.MainList {
        return getItem(position)
    }

    class RecommendationMainListViewHolder(private val binding: ItemRecommendationMainBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun onBind(data: RecommendationMainListDTO.MainList, listener: OnItemClickListener?) {
            binding.tvWhen.text = data.situation
            binding.tvRecommendationCategory.text = data.title
            binding.tvRecommendationCategoryDescription.text = data.description

            Glide.with(binding.root)
                .load(data.image)
                .into(binding.ivRecommendationCategory)

            binding.layoutRecommendationCategory.setOnClickListener {
                listener?.onItemClick(adapterPosition)
            }
        }
    }
}