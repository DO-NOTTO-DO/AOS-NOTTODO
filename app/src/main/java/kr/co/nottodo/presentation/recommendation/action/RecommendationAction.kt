import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import kr.co.nottodo.R
import kr.co.nottodo.presentation.recommendation.action.RecommendationActionListAdapter
class RecommendationAction : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: RecommendationActionListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recommendation_action_title)

        // 리사이클러뷰 초기화
        recyclerView = findViewById(R.id.rv_recommendation_action_category)
        adapter = RecommendationActionListAdapter()
        recyclerView.adapter = adapter

        // 아이템 클릭 리스너 설정
        adapter.setOnItemClickListener(object :
            RecommendationActionListAdapter.OnItemClickListener {
            override fun onItemClick(position: Int) {
                // 클릭된 아이템의 상태 변경
                adapter.setSelectedItem(position)

                // 변경된 상태를 반영하여 아이템 갱신
                adapter.notifyItemRangeChanged(0, adapter.itemCount)
            }
        })

        // ...
    }

    // ...
}
