package kr.co.nottodo.presentation.onboard.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kr.co.nottodo.R
import kr.co.nottodo.databinding.ItemOnboardFourthBinding

class OnboardSituationAdapter(
    private val context: Context,
    private val itemList: List<String>,
    private val plusSituationCount: () -> Unit,
    private val minusSituationCount: () -> Unit,
) :
    RecyclerView.Adapter<OnboardSituationAdapter.OnboardSituationViewHolder>() {
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
        holder.onBind(binding, position, plusSituationCount, minusSituationCount)
    }

    class OnboardSituationViewHolder(
        binding: ItemOnboardFourthBinding,
        private val itemList: List<String>,
    ) : RecyclerView.ViewHolder(binding.root) {

        fun onBind(
            binding: ItemOnboardFourthBinding,
            position: Int,
            plusSituationCount: () -> Unit,
            minusSituationCount: () -> Unit,
        ) {
            with(binding) {
                tvItemOnboardFourth.text = itemList[position]
                tvItemOnboardFourth.setOnClickListener {
                    if (isClicked(tvItemOnboardFourth)) {
                        tvItemOnboardFourth.tooltipText = "unclicked"
                        layoutItemOnboardFourth.setBackgroundResource(R.drawable.rectangle_solid_gray_1_radius_10)
                        minusSituationCount.invoke()
                    } else {
                        tvItemOnboardFourth.tooltipText = "clicked"
                        layoutItemOnboardFourth.setBackgroundResource(R.drawable.rectangle_solid_gray_1_stroke_green1_1_radius_10)
                        plusSituationCount.invoke()
                    }
                }
            }
        }

        private fun isClicked(view: View): Boolean {
            return view.tooltipText != "unclicked"
        }
    }
}
