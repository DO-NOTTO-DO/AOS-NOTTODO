package kr.co.nottodo.presentation.home.view

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import kr.co.nottodo.R
import kr.co.nottodo.data.model.ResponseHomeDaily
import kr.co.nottodo.databinding.ItemListHomeTodoBinding
import kr.co.nottodo.util.DiffUtilItemCallback

class HomeAdpater(
    private val menuItemClick: (Long) -> Unit,
    private val todoItemClick: (Long, Boolean) -> Unit,
) :
    ListAdapter<ResponseHomeDaily, HomeAdpater.HomeViewHolder>(diffUtil) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeViewHolder {
        val binding =
            ItemListHomeTodoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HomeViewHolder(binding, menuItemClick, todoItemClick)
    }

    override fun onBindViewHolder(holder: HomeViewHolder, position: Int) {
        holder.onBind(currentList[position])
    }

    class HomeViewHolder(
        private val binding: ItemListHomeTodoBinding,
        private val menuItemClick: (Long) -> Unit,
        private val todoItemClick: (Long, Boolean) -> Unit,
    ) : RecyclerView.ViewHolder(binding.root) {
        fun onBind(data: ResponseHomeDaily) {
            binding.ivHomeTodoCheck.isSelected = isCheckTodo(data.completionStatus)
            binding.tvHomeTodoSituation.text = data.situation
            binding.tvHomeTodo.text = data.title
            binding.ivHomeTodoCheck.setOnClickListener {
                todoItemClick(
                    data.id,
                    binding.ivHomeTodoCheck.isSelected
                )
            }
            binding.ivHomeMetalBall.setOnClickListener { menuItemClick(data.id) }
        }

        private fun isCheckTodo(isCheck: String): Boolean = when (isCheck) {
            CHECKED -> {
                setCompleteTodo()
                true
            }
            else -> {
                setUncompleteTodo()
                false
            }
        }

        private fun setCompleteTodo() {
            binding.clHomeCheckTodo.visibility = View.VISIBLE
            binding.tvHomeTodo!!.setPaintFlags(binding.tvHomeTodo!!.getPaintFlags() or Paint.STRIKE_THRU_TEXT_FLAG)
            binding.ivHomeTodoCheck.isChecked = true
            binding.tvHomeTodoSituation.setBackgroundResource(R.drawable.rectangle_border_gray6_50)
            binding.clHomeMain.setBackgroundResource(R.drawable.rectangle_border_grey5_10)
        }

        private fun setUncompleteTodo() {
            binding.clHomeCheckTodo.visibility = View.INVISIBLE
        }

    }

    companion object {
        val diffUtil = DiffUtilItemCallback<ResponseHomeDaily>(
            onItemsTheSame = { old, new -> old.id == new.id },
            onContentsTheSame = { old, new -> old == new }
        )
        const val CHECKED = "CHECKED"
    }
}