package kr.co.nottodo.presentation.recommendation.main
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kr.co.nottodo.presentation.recommendation.main.RecommendationMainListViewHolder


class RecommendationMainListViewAdapter : RecyclerView.Adapter<RecommendationMainListViewHolder>() {

    private val recommendationList = listOf("Item 1", "Item 2", "Item 3") // 아이템 리스트를 고정된 순서로 초기화

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecommendationMainListViewHolder {
        // ViewHolder 객체 생성 로직 추가
        return TODO("Provide the return value")
    }

    override fun onBindViewHolder(holder: RecommendationMainListViewHolder, position: Int) {
        val recommendationItem = recommendationList[position] // 고정된 순서의 아이템을 가져옴

        // 아이템을 화면에 표시하는 로직 추가
    }

    override fun getItemCount(): Int {
        return recommendationList.size
    }

    // 기타 어댑터 클래스 구현 내용...
}

