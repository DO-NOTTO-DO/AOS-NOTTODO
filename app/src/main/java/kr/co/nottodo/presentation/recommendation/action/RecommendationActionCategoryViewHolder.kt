package kr.co.nottodo.presentation.recommendation.action
import RecommendationActionListDTO
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import kr.co.nottodo.databinding.ItemRecommendationActionCategoryBinding

class RecommendationActionCategoryViewHolder(
    private val binding: ItemRecommendationActionCategoryBinding,
    private val childClickListener: (view: View, childData: RecommendationActionListDTO.ActionList.CategoryList) -> Unit
) : RecyclerView.ViewHolder(binding.root), View.OnClickListener, View.OnLongClickListener {

    private lateinit var childData: RecommendationActionListDTO.ActionList.CategoryList

    init {
        binding.root.setOnClickListener(this)
        binding.root.setOnLongClickListener(this)
    }


    fun bind(data: RecommendationActionListDTO.ActionList.CategoryList) {
        childData = data
        binding.apply {
            tvRecommendationActionCategory.text = data.name
        }
    }

    override fun onClick(view: View) {
        childClickListener.invoke(view, childData)
    }

    override fun onLongClick(v: View?): Boolean = false
}
