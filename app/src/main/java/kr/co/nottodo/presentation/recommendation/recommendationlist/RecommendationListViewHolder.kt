import androidx.recyclerview.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.gson.Gson
import kr.co.nottodo.R
import kr.co.nottodo.presentation.recommendation.recommendationlist.RecommendationListDTO
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RecommendationListViewHolder(FragmentItemRecommendationCategoryYoutube: View) : RecyclerView.ViewHolder(FragmentItemRecommendationCategoryYoutube) {
    private val whenTextView: TextView = FragmentItemRecommendationCategoryYoutube.findViewById(R.id.iv_when_youtube)
    private val titleTextView: TextView = FragmentItemRecommendationCategoryYoutube.findViewById(R.id.tv_recommendation_category_youtube)
    private val imageView: ImageView = FragmentItemRecommendationCategoryYoutube.findViewById(R.id.iv_recommendation_category_youtube)
    private val descriptionTextView: TextView = FragmentItemRecommendationCategoryYoutube.findViewById(R.id.tv_recommendation_category_youtube_explain)

    fun bindData(item: RecommendationListDTO) {
        whenTextView.text = item.data.first().situation
        titleTextView.text = item.data.first().title
        descriptionTextView.text = item.data.first().description

    }
}