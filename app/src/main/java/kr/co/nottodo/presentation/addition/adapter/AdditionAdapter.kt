package kr.co.nottodo.presentation.addition.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kr.co.nottodo.databinding.ItemAdditionMissionHistoryBinding

class AdditionAdapter(
    context: Context, private val lambda: (String) -> Unit,
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val inflater by lazy { LayoutInflater.from(context) }
    private val missionHistory = listOf(
        "쓸데없는 계획 세우지 않기",
        "군것질 비율 줄이기",
        "유튜브 보지 않기",
        "핸드폰 작작 보고 끊어버리기",
        "쓸데없는 계획 세우지 않기",
        "군것질 비율 줄이기",
        "유튜브 보지 않기",
        "핸드폰 작작 보고 끊어버리기",
        "쓸데없는 계획 세우지 않기",
        "군것질 비율 줄이기",
        "유튜브 보지 않기",
        "핸드폰 작작 보고 끊어버리기"
    )

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding = ItemAdditionMissionHistoryBinding.inflate(inflater, parent, false)
        return AdditionViewHolder(binding, lambda)
    }

    class AdditionViewHolder(
        private val binding: ItemAdditionMissionHistoryBinding,
        private val lambda: (String) -> Unit,
    ) : RecyclerView.ViewHolder(binding.root) {
        fun onBind(missionHistory: List<String>, position: Int) {
            with(binding.tvAdditionMissionRvItem) {
                text = missionHistory[position]
                setOnClickListener {
                    lambda.invoke(missionHistory[position])
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return 12
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as AdditionViewHolder).onBind(missionHistory, position)
    }
}