package kr.co.nottodo.presentation.recommendation.action.view

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import coil.load
import kr.co.nottodo.databinding.ActivityRecommendActionBinding
import kr.co.nottodo.presentation.addition.view.AdditionActivity
import kr.co.nottodo.presentation.recommendation.action.adapter.RecommendActionAdapter
import kr.co.nottodo.presentation.recommendation.action.viewmodel.RecommendActionViewModel
import kr.co.nottodo.presentation.recommendation.mission.view.RecommendMissionActivity
import kr.co.nottodo.presentation.recommendation.mission.view.RecommendMissionActivity.Companion.MISSION_DETAIL
import kr.co.nottodo.presentation.recommendation.model.ParcelizeMissionActionDetail
import kr.co.nottodo.presentation.recommendation.model.ParcelizeMissionDetail
import kr.co.nottodo.util.getParcelable
import kr.co.nottodo.util.showToast
import timber.log.Timber

class RecommendActionActivity : AppCompatActivity() {

    private val binding by lazy { ActivityRecommendActionBinding.inflate(layoutInflater) }
    private var recommendActionAdapter: RecommendActionAdapter? = null
    private val viewModel by viewModels<RecommendActionViewModel>()
    private val dataFromRecommendMissionActivity by lazy {
        val dataFromRecommendMissionActivity = intent.getParcelable(
            MISSION_DETAIL, ParcelizeMissionDetail::class.java
        )
        if (dataFromRecommendMissionActivity == null) {
            if (!isFinishing) finish()
        }
        dataFromRecommendMissionActivity!!
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setDataBinding()
        setData()
        setViews()
        setClickEvents()
        setObservers()
    }

    private fun setDataBinding() {
        binding.lifecycleOwner = this
        binding.vm = viewModel
    }

    private fun setData() {
        getDataFromRecommendMissionActivity()
        getRecommendActionList()
    }

    private fun getRecommendActionList() {
        viewModel.getRecommendActionList()
    }

    private fun getDataFromRecommendMissionActivity() {
        viewModel.setMissionId(
            dataFromRecommendMissionActivity.id
        )

        with(binding) {
            tvRecommendActionMission.text = dataFromRecommendMissionActivity.title
            tvRecommendActionSituation.text = dataFromRecommendMissionActivity.situation
            ivRecommendActionMissionSituation.load(dataFromRecommendMissionActivity.image)
        }
    }

    private fun setViews() {
        setContentView(binding.root)
        setRecommendActionRecyclerView()
    }

    private fun setRecommendActionRecyclerView() {
        recommendActionAdapter = RecommendActionAdapter(
            plusSelectedActionsCount = viewModel.plusSelectedActionsCount,
            minusSelectedActionsCount = viewModel.minusSelectedActionsCount,
            isSelectedActionsCountThree = viewModel.isSelectedActionsCountThree
        )
        binding.rvRecommendAction.adapter = recommendActionAdapter
        binding.rvRecommendAction.layoutManager = object : LinearLayoutManager(this) {
            override fun canScrollVertically(): Boolean = false
        }
    }

    private fun setClickEvents() {
        backIvClickEvent()
        writeDirectTvClickEvent()
        continueBtnClickEvent()
    }

    private fun continueBtnClickEvent() {
        binding.btnRecommendActionContinue.setOnClickListener {
            val selectedActionList = recommendActionAdapter?.getSelectedActionList()

            val parcelizeMissionActionDetail = ParcelizeMissionActionDetail(
                title = dataFromRecommendMissionActivity.title,
                situation = dataFromRecommendMissionActivity.situation,
                actionList = selectedActionList ?: emptyList()
            )
            startActivity(
                Intent(this, AdditionActivity::class.java).putExtra(
                    MISSION_ACTION_DETAIL, parcelizeMissionActionDetail
                )
            )
            if (!isFinishing) finish()
        }
    }

    private fun writeDirectTvClickEvent() {
        binding.tvRecommendActionWriteDirect.setOnClickListener {
            startActivity(Intent(this, AdditionActivity::class.java))
            if (!isFinishing) finish()
        }
    }

    private fun backIvClickEvent() {
        binding.ivRecommendationActionBack.setOnClickListener {
            startActivity(Intent(this, RecommendMissionActivity::class.java))
            if (!isFinishing) finish()
        }
    }

    private fun setObservers() {
        setRecommendActionObservers()
    }

    private fun setRecommendActionObservers() {
        setRecommendActionSuccessObserver()
        setRecommendActionErrorObserver()
    }

    private fun setRecommendActionErrorObserver() {
        viewModel.recommendActionListErrorResponse.observe(this) { errorMessage ->
            showToast(errorMessage)
            Timber.tag("okhttp").e(errorMessage)
        }
    }

    private fun setRecommendActionSuccessObserver() {
        viewModel.recommendActionListSuccessResponse.observe(this) { actionList ->
            recommendActionAdapter?.submitList(actionList)
        }
    }

    companion object {
        const val MISSION_ACTION_DETAIL = "MISSION_ACTION_DETAIL"
    }
}