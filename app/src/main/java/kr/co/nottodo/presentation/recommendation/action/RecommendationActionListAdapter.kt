import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import kr.co.nottodo.databinding.ItemRecommendationActionCategoryBinding
import kr.co.nottodo.databinding.ItemRecommendationActionSelectBinding

class RecommendationActionListAdapter :
    ListAdapter<RecommendationActionListDTO.ActionList.CategoryList, RecommendationActionListAdapter.RecommendationActionListViewHolder>(
        diffUtilCallback
    ) {

    private var itemClickListener: OnItemClickListener? = null
    private var selectedItemPosition: Int = RecyclerView.NO_POSITION


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
        private val diffUtilCallback = object :
            DiffUtil.ItemCallback<RecommendationActionListDTO.ActionList.CategoryList>() {
            override fun areItemsTheSame(
                oldItem: RecommendationActionListDTO.ActionList.CategoryList,
                newItem: RecommendationActionListDTO.ActionList.CategoryList
            ): Boolean {
                return oldItem.name == newItem.name
            }

            override fun areContentsTheSame(
                oldItem: RecommendationActionListDTO.ActionList.CategoryList,
                newItem: RecommendationActionListDTO.ActionList.CategoryList
            ): Boolean {
                return oldItem == newItem
            }
        }
    }

    class RecommendationActionListViewHolder(private val binding: ItemRecommendationActionCategoryBinding) :
        RecyclerView.ViewHolder(binding.root) {

        private val selectBinding = ItemRecommendationActionSelectBinding.bind(binding.root)

        fun onBind(
            data: RecommendationActionListDTO.ActionList.CategoryList,
            position: Int,
            listener: OnItemClickListener?,
            selectedItemPosition: Int
        ) {
            binding.tvRecommendationActionCategory.text = data.name

            if (position == selectedItemPosition) {
                // 선택된 아이템인 경우
                selectBinding.root.visibility = View.VISIBLE
                binding.tvRecommendationActionCategory.setTextColor(Color.WHITE)
            } else {
                // 선택되지 않은 아이템인 경우
                selectBinding.root.visibility = View.GONE
                binding.tvRecommendationActionCategory.setTextColor(Color.parseColor("#9398aa"))
            }

            binding.root.setOnClickListener {
                listener?.onItemClick(adapterPosition)
            }
        }
    }

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


    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }
}
