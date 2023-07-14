package kr.co.nottodo.presentation.recommendation.action.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import kr.co.nottodo.R
import kr.co.nottodo.data.remote.model.recommendation.action.ResponseRecommendActionListDTO.Mission.Action
import kr.co.nottodo.databinding.ItemRecommendActionBinding
import kr.co.nottodo.presentation.recommendation.action.adapter.RecommendActionAdapter.RecommendActionViewHolder
import kr.co.nottodo.util.DiffUtilItemCallback
import kr.co.nottodo.util.showNotTodoSnackBar

class RecommendActionAdapter(
    private val plusSelectedActionsCount: () -> Unit,
    private val minusSelectedActionsCount: () -> Unit,
    private val isSelectedActionsCountThree: () -> Boolean,
) : ListAdapter<Action, RecommendActionViewHolder>(diffUtil) {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): RecommendActionViewHolder {
        val binding =
            ItemRecommendActionBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RecommendActionViewHolder(
            binding,
            plusSelectedActionsCount = plusSelectedActionsCount,
            minusSelectedActionsCount = minusSelectedActionsCount,
            isSelectedActionsCountThree = isSelectedActionsCountThree
        )
    }

    override fun onBindViewHolder(holder: RecommendActionViewHolder, position: Int) {
        holder.onBind(currentList[position])
    }

    fun getSelectedActionList(): List<String> {
        return currentList.filter { it.isSelected }.map { it.name }.toList()
    }

    class RecommendActionViewHolder(
        private val binding: ItemRecommendActionBinding,
        private val plusSelectedActionsCount: () -> Unit,
        private val minusSelectedActionsCount: () -> Unit,
        private val isSelectedActionsCountThree: () -> Boolean,
    ) : RecyclerView.ViewHolder(binding.root) {
        fun onBind(action: Action) {
            setData(action)
            setClickEvents(action)
        }

        private fun setClickEvents(action: Action) {
            setItemClickEvent(action)
        }

        private fun setItemClickEvent(action: Action) {
            binding.root.setOnClickListener {
                if (!it.isSelected) {
                    if (isSelectedActionsCountThree.invoke()) {
                        binding.root.context.showNotTodoSnackBar(
                            binding.root, binding.root.context.getString(R.string.action_max_3)
                        )
                        return@setOnClickListener
                    }
                    plusSelectedActionsCount.invoke()
                    binding.ivRecommendActionCheck.visibility = View.VISIBLE
                    it.isSelected = !it.isSelected
                } else {
                    minusSelectedActionsCount.invoke()
                    binding.ivRecommendActionCheck.visibility = View.INVISIBLE
                    it.isSelected = !it.isSelected
                }
                action.isSelected = it.isSelected
            }
        }

        private fun setData(action: Action) {
            setActionTitle(action)
            setActionDesc(action)
        }

        private fun setActionDesc(action: Action) {
            if (action.description.isNullOrBlank()) {
                binding.tvRecommendActionDesc.visibility = View.GONE
                return
            }
            binding.tvRecommendActionDesc.text = action.description
        }

        private fun setActionTitle(action: Action) {
            binding.tvRecommendActionTitle.text = action.name
        }
    }

    companion object {
        val diffUtil = DiffUtilItemCallback<Action>(onItemsTheSame = { old, new -> old === new },
            onContentsTheSame = { old, new -> old == new })
    }
}