package kr.co.nottodo.presentation.onboard.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kr.co.nottodo.databinding.ItemOnboardFourthBinding

class OnboardSituationAdapter(
    private val context: Context,
    private val itemList: List<String>,
    private val addSituation: (String) -> Unit,
    private val removeSituation: (String) -> Unit,
) : RecyclerView.Adapter<OnboardSituationAdapter.OnboardSituationViewHolder>() {
    lateinit var binding: ItemOnboardFourthBinding
    private val inflater by lazy { LayoutInflater.from(context) }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OnboardSituationViewHolder {
        binding = ItemOnboardFourthBinding.inflate(inflater, parent, false)
        return OnboardSituationViewHolder(binding, itemList)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun onBindViewHolder(holder: OnboardSituationViewHolder, position: Int) {
        holder.onBind(binding, position, addSituation, removeSituation)
    }

    class OnboardSituationViewHolder(
        binding: ItemOnboardFourthBinding,
        private val itemList: List<String>,
    ) : RecyclerView.ViewHolder(binding.root) {
        fun onBind(
            binding: ItemOnboardFourthBinding,
            position: Int,
            addSituation: (String) -> Unit,
            removeSituation: (String) -> Unit,
        ) {
            with(binding) {
                tvItemOnboardFourth.text = itemList[position]
                tvItemOnboardFourth.setOnClickListener {
                    if (layoutItemOnboardFourth.isSelected) {
                        layoutItemOnboardFourth.isSelected = false
                        removeSituation.invoke(tvItemOnboardFourth.text.toString())
                    } else {
                        layoutItemOnboardFourth.isSelected = true
                        addSituation.invoke(tvItemOnboardFourth.text.toString())
                    }
                }
            }
        }
    }
}
