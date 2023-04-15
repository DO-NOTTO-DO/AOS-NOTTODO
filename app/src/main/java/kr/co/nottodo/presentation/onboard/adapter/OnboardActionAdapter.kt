package kr.co.nottodo.presentation.onboard.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kr.co.nottodo.databinding.ItemOnboardSixthBinding

class OnboardActionAdapter(
    private val context: Context,
    private val itemList: List<String>,
) :
    RecyclerView.Adapter<OnboardActionAdapter.OnboardActionViewHolder>() {
    lateinit var binding: ItemOnboardSixthBinding
    private val inflater by lazy { LayoutInflater.from(context) }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): OnboardActionViewHolder {
        binding = ItemOnboardSixthBinding.inflate(inflater, parent, false)
        return OnboardActionViewHolder(binding, itemList)
    }

    override fun onBindViewHolder(holder: OnboardActionViewHolder, position: Int) {
        holder.onBind(binding, position)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    class OnboardActionViewHolder(
        binding: ItemOnboardSixthBinding,
        private val itemList: List<String>,
    ) : RecyclerView.ViewHolder(binding.root) {

        fun onBind(
            binding: ItemOnboardSixthBinding,
            position: Int,
        ) {
            binding.tvItemOnboardSixth.text = itemList[position]
        }
    }
}
