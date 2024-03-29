package kr.co.nottodo.presentation.home.view

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import kr.co.nottodo.R
import kr.co.nottodo.data.model.Home.HomeDailyResponse
import kr.co.nottodo.databinding.ItemListHomeTodoBinding
import kr.co.nottodo.util.DiffUtilItemCallback
import timber.log.Timber

class HomeAdapter(
    private val menuItemClick: (Long) -> Unit,
    private val todoItemClick: (Long, String) -> Unit,
) :
    ListAdapter<HomeDailyResponse.HomeDaily, HomeAdapter.HomeViewHolder>(diffUtil) {

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
        private val todoItemClick: (Long, String) -> Unit,
    ) : RecyclerView.ViewHolder(binding.root) {
        fun onBind(data: HomeDailyResponse.HomeDaily) {
            with(binding) {
                ivHomeTodoCheck.isChecked = isCheckTodo(data.completionStatus)
                tvHomeTodoSituation.text = data.situationName
                tvHomeTodo.text = data.title
                ivHomeTodoCheck.setOnClickListener {
                    todoItemClick(
                        data.id,
                        parseCheckTodo(ivHomeTodoCheck.isChecked),
                    )
                }
                clHomeClick.setOnClickListener { menuItemClick(data.id) }
            }
        }

        private fun isCheckTodo(isCheck: String): Boolean = when (isCheck) {
            CHECKED -> {
                showCompleteTodoView()
                true
            }

            else -> {
                setUncompleteTodo()
                false
            }
        }

        private fun showCompleteTodoView() {
            with(binding) {
                clHomeCheckTodo.visibility = View.VISIBLE
                vHomeCompleteTodo.visibility = View.VISIBLE
                tvHomeTodoSituation.setTextColor(Color.parseColor("#9398aa"))
                tvHomeTodo.setTextColor(Color.parseColor("#9398aa"))
                tvHomeTodoSituation.setBackgroundResource(R.drawable.rectangle_border_gray6_50)
                clHomeMain.setBackgroundResource(R.drawable.rectangle_border_grey5_10)
            }
        }

        private fun setUncompleteTodo() {
            binding.clHomeMain.setBackgroundResource(R.drawable.rectangle_border_white_10)
            binding.clHomeCheckTodo.visibility = View.INVISIBLE // 회색
            binding.vHomeCompleteTodo.visibility = View.GONE
            binding.tvHomeTodoSituation.setTextColor(Color.parseColor("#34343a"))
            binding.tvHomeTodo.setTextColor(Color.parseColor("#34343a"))
        }

        private fun parseCheckTodo(bindingCheck: Boolean): String {
            Timber.tag("todo 잘 보내지니?$bindingCheck")
            val check = if (bindingCheck) {
                CHECKED
            } else {
                UNCHECKED
            }
            return check
        }
    }

    companion object {
        val diffUtil = DiffUtilItemCallback<HomeDailyResponse.HomeDaily>(
            onItemsTheSame = { old, new -> old.id == new.id },
            onContentsTheSame = { old, new -> old == new },
        )
        const val CHECKED = "CHECKED"
        const val UNCHECKED = "UNCHECKED"
    }
}
