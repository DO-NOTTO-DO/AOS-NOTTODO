package kr.co.nottodo.presentation.onboard.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kr.co.nottodo.R
import kr.co.nottodo.databinding.ItemOnboardThirdBinding
import kr.co.nottodo.presentation.onboard.OnboardInterface
import kr.co.nottodo.presentation.onboard.view.OnboardFourthFragment
import java.util.*
import kotlin.concurrent.schedule

class OnboardSituationAdapter(
    private val context: Context,
    private val itemList: List<String>,
) :
    RecyclerView.Adapter<OnboardSituationAdapter.OnboardSituationViewHolder>() {
    lateinit var binding: ItemOnboardThirdBinding
    private val inflater by lazy { LayoutInflater.from(context) }
    lateinit var onboardInterface: OnboardInterface

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OnboardSituationViewHolder {
        binding = ItemOnboardThirdBinding.inflate(inflater, parent, false)
        if (context is OnboardInterface) {
            onboardInterface = context
        }
        return OnboardSituationViewHolder(binding, itemList)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun onBindViewHolder(holder: OnboardSituationViewHolder, position: Int) {
        holder.onBind(binding, position, onboardInterface)
    }

    class OnboardSituationViewHolder(
        binding: ItemOnboardThirdBinding,
        private val itemList: List<String>,
    ) : RecyclerView.ViewHolder(binding.root) {

        fun onBind(
            binding: ItemOnboardThirdBinding,
            position: Int,
            onboardInterface: OnboardInterface,
        ) {
            binding.tvItemOnboard.text = itemList[position]
            binding.layoutItemOnboard.setOnClickListener {
                it.setBackgroundResource(R.drawable.rectangle_solid_gray_1_stroke_green1_1_radius_10)
                Timer().schedule(1000) {
                    onboardInterface.changeFragment(OnboardFourthFragment())
                }
            }
        }
    }
}
