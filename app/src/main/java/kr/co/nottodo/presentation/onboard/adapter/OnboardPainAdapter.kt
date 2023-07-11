package kr.co.nottodo.presentation.onboard.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kr.co.nottodo.R
import kr.co.nottodo.databinding.ItemOnboardThirdBinding
import kr.co.nottodo.presentation.onboard.OnboardInterface
import kr.co.nottodo.presentation.onboard.view.OnboardFourthFragment
import kr.co.nottodo.util.NotTodoAmplitude
import java.util.Timer
import kotlin.concurrent.schedule

class OnboardPainAdapter(
    private val context: Context,
    private val itemList: List<String>,
) : RecyclerView.Adapter<OnboardPainAdapter.OnboardPainViewHolder>() {
    lateinit var binding: ItemOnboardThirdBinding
    private val inflater by lazy { LayoutInflater.from(context) }
    lateinit var onboardInterface: OnboardInterface

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OnboardPainViewHolder {
        binding = ItemOnboardThirdBinding.inflate(inflater, parent, false)
        if (context is OnboardInterface) {
            onboardInterface = context
        }
        return OnboardPainViewHolder(binding, itemList)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun onBindViewHolder(holder: OnboardPainViewHolder, position: Int) {
        holder.onBind(binding, position, onboardInterface)
    }

    class OnboardPainViewHolder(
        binding: ItemOnboardThirdBinding,
        private val itemList: List<String>,
    ) : RecyclerView.ViewHolder(binding.root) {

        fun onBind(
            binding: ItemOnboardThirdBinding,
            position: Int,
            onboardInterface: OnboardInterface,
        ) {
            with(binding) {
                tvItemOnboardThird.text = itemList[position]
                tvItemOnboardThird.setOnClickListener {
                    NotTodoAmplitude.trackEventWithProperty(
                        root.context.getString(R.string.click_onboarding_next_2),
                        root.context.getString(R.string.onboard_select),
                        tvItemOnboardThird.text
                    )
                    layoutItemOnboardThird.isSelected = !layoutItemOnboardThird.isSelected
                    Timer().schedule(500) {
                        onboardInterface.changeFragment(OnboardFourthFragment())
                    }
                }
            }
        }
    }
}
