package kr.co.nottodo.presentation.onboard.adapter

import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kr.co.nottodo.R
import kr.co.nottodo.databinding.ItemOnboardThirdBinding
import kr.co.nottodo.util.NotTodoAmplitude.trackEventWithProperty

class OnboardPainAdapter(
    private val itemList: List<String>, private val navigateToOnboardFourthFragment: () -> Unit,
) : RecyclerView.Adapter<OnboardPainAdapter.OnboardPainViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OnboardPainViewHolder {
        val binding =
            ItemOnboardThirdBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return OnboardPainViewHolder(binding, itemList, navigateToOnboardFourthFragment)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun onBindViewHolder(holder: OnboardPainViewHolder, position: Int) {
        holder.onBind(position)
    }

    class OnboardPainViewHolder(
        val binding: ItemOnboardThirdBinding,
        private val itemList: List<String>,
        private val navigateToOnboardFourthFragment: () -> Unit,
    ) : RecyclerView.ViewHolder(binding.root) {

        fun onBind(
            position: Int,
        ) {
            with(binding) {
                tvItemOnboardThird.text = itemList[position]
                tvItemOnboardThird.setOnClickListener {
                    trackEventWithProperty(
                        root.context.getString(R.string.click_onboarding_next_2),
                        root.context.getString(R.string.onboard_select),
                        tvItemOnboardThird.text
                    )
                    layoutItemOnboardThird.isSelected = !layoutItemOnboardThird.isSelected
                    Handler(Looper.getMainLooper()).also {
                        it.postDelayed({
                            navigateToOnboardFourthFragment()
                        }, 500)
                    }
                }
            }
        }
    }
}
