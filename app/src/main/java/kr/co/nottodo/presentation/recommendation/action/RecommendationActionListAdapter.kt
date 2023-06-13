import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import kr.co.nottodo.databinding.ItemRecommendationActionCategoryBinding
import kr.co.nottodo.databinding.ItemRecommendationActionSelectBinding

class RecommendationActionListAdapter :
    ListAdapter<RecommendationActionListDTO.ActionList.CategoryList, RecyclerView.ViewHolder>(
        diffUtilCallback
    ) {

    private var itemClickListener: OnItemClickListener? = null
    private val selectedItems = mutableListOf<Int>()

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return if (viewType == ITEM_TYPE_SELECTED) {
            val binding = ItemRecommendationActionSelectBinding.inflate(inflater, parent, false)
            RecommendationActionSelectViewHolder(binding)
        } else {
            val binding = ItemRecommendationActionCategoryBinding.inflate(inflater, parent, false)
            RecommendationActionCategoryViewHolder(binding)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val viewType = getItemViewType(position)
        if (viewType == ITEM_TYPE_SELECTED && holder is RecommendationActionSelectViewHolder) {
            holder.bind(getItem(position))
        } else if (viewType == ITEM_TYPE_NORMAL && holder is RecommendationActionCategoryViewHolder) {
            holder.bind(getItem(position))
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (selectedItems.contains(position)) {
            ITEM_TYPE_SELECTED
        } else {
            ITEM_TYPE_NORMAL
        }
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.itemClickListener = listener
    }

    fun setSelectedItem(position: Int) {
        if (selectedItems.contains(position)) {
            selectedItems.remove(position)
        } else {
            selectedItems.add(position)
        }
        notifyDataSetChanged()
    }

    companion object {
        private const val ITEM_TYPE_NORMAL = 0
        private const val ITEM_TYPE_SELECTED = 1

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

    inner class RecommendationActionCategoryViewHolder(private val binding: ItemRecommendationActionCategoryBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(data: RecommendationActionListDTO.ActionList.CategoryList) {
            binding.tvRecommendationActionCategory.text = data.name

            // Handle item click
            binding.root.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    itemClickListener?.onItemClick(position)
                }
            }

            // Update UI based on selection
            val isSelected = selectedItems.contains(adapterPosition)
            binding.root.isSelected = isSelected
        }
    }

    inner class RecommendationActionSelectViewHolder(private val binding: ItemRecommendationActionSelectBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(data: RecommendationActionListDTO.ActionList.CategoryList) {
            binding.tvCategory.text = data.name

            // Handle item click
            binding.root.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    itemClickListener?.onItemClick(position)
                }
            }

            // Update UI based on selection
            val isSelected = selectedItems.contains(adapterPosition)
            binding.root.isSelected = isSelected
        }
    }

    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }
}
