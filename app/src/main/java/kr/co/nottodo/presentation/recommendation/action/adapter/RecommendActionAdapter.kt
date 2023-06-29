package kr.co.nottodo.presentation.recommendation.action.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import kr.co.nottodo.data.remote.model.recommendation.action.ResponseRecommendActionListDTO.Mission.Action
import kr.co.nottodo.databinding.ItemRecommendActionBinding
import kr.co.nottodo.presentation.recommendation.action.adapter.RecommendActionAdapter.RecommendActionViewHolder
import kr.co.nottodo.util.DiffUtilItemCallback
import kr.co.nottodo.util.showSnackBar

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

    companion object {
        val diffUtil = DiffUtilItemCallback<Action>(onItemsTheSame = { old, new -> old === new },
            onContentsTheSame = { old, new -> old == new })
    }

    class RecommendActionViewHolder(
        private val binding: ItemRecommendActionBinding,
        private val plusSelectedActionsCount: () -> Unit,
        private val minusSelectedActionsCount: () -> Unit,
        private val isSelectedActionsCountThree: () -> Boolean,
    ) : RecyclerView.ViewHolder(binding.root) {
        fun onBind(action: Action) {
            setData(action)
            setClickEvents()
        }

        private fun setClickEvents() {
            setItemClickEvent()
        }

        private fun setItemClickEvent() {
            binding.root.setOnClickListener {
                if (!it.isSelected) {
                    if (isSelectedActionsCountThree.invoke()) {
                        binding.root.context.showSnackBar(binding.root, "실천방법은 최대 3개만 추가할 수 있어요.")
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
}