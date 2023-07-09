package kr.co.nottodo.presentation.addition.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import kr.co.nottodo.databinding.ItemAdditionMissionHistoryBinding
import kr.co.nottodo.util.DiffUtilItemCallback

class MissionHistoryAdapter(
    private val context: Context,
    private val setMissionName: (String) -> Unit,
) : ListAdapter<String, MissionHistoryAdapter.AdditionViewHolder>(diffUtil) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdditionViewHolder {
        val binding =
            ItemAdditionMissionHistoryBinding.inflate(LayoutInflater.from(context), parent, false)
        return AdditionViewHolder(binding, setMissionName)
    }

    override fun onBindViewHolder(holder: AdditionViewHolder, position: Int) {
        holder.onBind(currentList, position)
    }

    class AdditionViewHolder(
        private val binding: ItemAdditionMissionHistoryBinding,
        private val setMissionName: (String) -> Unit,
    ) : RecyclerView.ViewHolder(binding.root) {
        fun onBind(missionHistory: List<String>, position: Int) {
            with(binding.tvAdditionMissionRvItem) {
                text = missionHistory[position]
                setOnClickListener {
                    setMissionName.invoke(missionHistory[position])
                }
            }
        }
    }


    companion object {
        val diffUtil =
            DiffUtilItemCallback<String>(onItemsTheSame = { oldItem, newItem -> oldItem === newItem },
                onContentsTheSame = { oldItem, newItem -> oldItem == newItem })
    }
}