package kr.co.nottodo.presentation.onboard.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kr.co.nottodo.databinding.ItemOnboardFifthBinding
import kr.co.nottodo.presentation.onboard.OnboardInterface
import kr.co.nottodo.presentation.onboard.viewmodel.OnboardViewModel.Mission

class OnboardMissionAdapter(
    private val context: Context,
    private val itemList: List<Mission>,
) :
    RecyclerView.Adapter<OnboardMissionAdapter.OnboardMissionViewHolder>() {
    lateinit var binding: ItemOnboardFifthBinding
    private val inflater by lazy { LayoutInflater.from(context) }
    lateinit var onboardInterface: OnboardInterface

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): OnboardMissionViewHolder {
        binding = ItemOnboardFifthBinding.inflate(inflater, parent, false)
        if (context is OnboardInterface) {
            onboardInterface = context
        }
        return OnboardMissionViewHolder(binding, itemList)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun onBindViewHolder(holder: OnboardMissionViewHolder, position: Int) {
        holder.onBind(binding, position)
    }

    class OnboardMissionViewHolder(
        binding: ItemOnboardFifthBinding,
        private val itemList: List<Mission>,
    ) : RecyclerView.ViewHolder(binding.root) {

        fun onBind(
            binding: ItemOnboardFifthBinding,
            position: Int,
        ) {
            with(binding) {
                ivOnboardFifthItem.setImageResource(itemList[position].image)
                tvOnboardFifthSituationItem.text = itemList[position].situation
                tvOnboardFifthMissionItem.text = itemList[position].mission
            }
        }
    }
}
