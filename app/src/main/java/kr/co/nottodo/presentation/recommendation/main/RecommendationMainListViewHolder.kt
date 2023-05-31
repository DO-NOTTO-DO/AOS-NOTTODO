import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kr.co.nottodo.R
import kr.co.nottodo.databinding.ItemRecommendationMainBinding

class RecommendationMainListViewHolder(private val binding: ItemRecommendationMainBinding) :
    RecyclerView.ViewHolder(binding.root) {

    // 데이터를 아이템 뷰에 바인딩하는 메소드
    fun onBind(data: RecommendationMainListDTO.MainList, listener: RecommendationMainListViewAdapter.OnItemClickListener) {
        binding.tvWhen.text = data.situation
        binding.tvRecommendationCategory.text = data.title
        binding.tvRecommendationCategoryDescription.text = data.description

        // layout_recommendation_category 클릭 이벤트 처리
        binding.layoutRecommendationCategory.setOnClickListener {
            listener.onItemClick(adapterPosition)
        }
    }
}


//class RecommendationMainListViewHolder(item_recommendation_category: View) :
//    RecyclerView.ViewHolder(item_recommendation_category) {
//    // 아이템 뷰의 구성 요소에 대한 참조 변수 선언
//    private val whenTextView: TextView = item_recommendation_category.findViewById(R.id.tv_when)
//    private val titleTextView: TextView =
//        item_recommendation_category.findViewById(R.id.tv_recommendation_category)
//    private val descriptionTextView: TextView =
//        item_recommendation_category.findViewById(R.id.tv_recommendation_category_description)
//    private val imageView: ImageView =
//        item_recommendation_category.findViewById(R.id.iv_recommendation_category)
//
//    // 데이터를 아이템 뷰에 바인딩하는 메소드
//    fun bindData(data: RecommendationMainListDTO.MainList) {
//        whenTextView.text = data.situation
//        titleTextView.text = data.title
//        descriptionTextView.text = data.description
//
//    }
//    // 데이터를 아이템 뷰에 바인딩하는 메소드
//    fun onBind(data: RecommendationMainListDTO.MainList, listener: RecommendationMainListViewAdapter.OnItemClickListener) {
//        binding.tvWhen.text = data.situation
//        binding.tvRecommendationCategory.text = data.title
//        binding.tvRecommendationCategoryDescription.text = data.description
//
//        // layout_recommendation_category 클릭 이벤트 처리
//        binding.layoutRecommendationCategory.setOnClickListener {
//            listener.onItemClick(adapterPosition)
//        }
//    }
//
//
//}
