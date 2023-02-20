package kr.co.nottodo.presentation.home.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import kr.co.nottodo.data.model.ResponseHomeDaily
import kr.co.nottodo.databinding.ItemListHomeTodoBinding
import kr.co.nottodo.util.DiffUtilItemCallback

class HomeAdpater() :
    ListAdapter<ResponseHomeDaily, HomeAdpater.HomeViewHolder>(diffUtil) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeViewHolder {
        val binding =
            ItemListHomeTodoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HomeViewHolder(binding)
    }

    override fun onBindViewHolder(holder: HomeViewHolder, position: Int) {
        holder.onBind(currentList[position])
    }

    class HomeViewHolder(
        private val binding: ItemListHomeTodoBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun onBind(data: ResponseHomeDaily) {
            binding.tvHomeTodoSituation.text = data.situation
            binding.tvHomeTodo.text = data.title
            binding.ivHomeTodoCheck.setOnClickListener { absoluteAdapterPosition }
        }
    }

    companion object {
        val diffUtil = DiffUtilItemCallback<ResponseHomeDaily>(
            onItemsTheSame = { old, new -> old.id == new.id },
            onContentsTheSame = { old, new -> old == new }
        )
    }
}