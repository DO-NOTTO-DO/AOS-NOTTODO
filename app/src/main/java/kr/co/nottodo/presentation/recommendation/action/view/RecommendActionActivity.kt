package kr.co.nottodo.presentation.recommendation.action.view

import android.content.Intent
import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import coil.load
import kr.co.nottodo.R
import kr.co.nottodo.databinding.ActivityRecommendActionBinding
import kr.co.nottodo.presentation.addition.view.AdditionActivity
import kr.co.nottodo.presentation.recommendation.action.adapter.RecommendActionAdapter
import kr.co.nottodo.presentation.recommendation.action.viewmodel.RecommendActionViewModel
import kr.co.nottodo.presentation.recommendation.mission.view.RecommendMissionActivity
import kr.co.nottodo.presentation.recommendation.mission.view.RecommendMissionActivity.Companion.MISSION_DETAIL
import kr.co.nottodo.presentation.recommendation.model.RecommendMissionUiModel
import kr.co.nottodo.presentation.recommendation.model.RecommendUiModel
import kr.co.nottodo.util.NotTodoAmplitude.trackEvent
import kr.co.nottodo.util.NotTodoAmplitude.trackEventWithProperty
import kr.co.nottodo.util.getParcelable
import kr.co.nottodo.util.showToast

class RecommendActionActivity : AppCompatActivity() {

    private val binding by lazy { ActivityRecommendActionBinding.inflate(layoutInflater) }
    private var recommendActionAdapter: RecommendActionAdapter? = null
    private val viewModel by viewModels<RecommendActionViewModel>()
    private val dataFromRecommendMissionActivity by lazy {
        val recommendMissionUiModel = intent.getParcelable(
            MISSION_DETAIL, RecommendMissionUiModel::class.java
        )
        if (recommendMissionUiModel == null) {
            if (!isFinishing) finish()
        }
        requireNotNull(recommendMissionUiModel) {
            getString(
                R.string._is_null, getString(R.string.recommend_mission_ui_model)
            )
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setDataBinding()
        setData()
        setViews()
        overrideBackPressed()
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
        trackEventWithProperty(
            getString(R.string.view_recommend_mission_detail), mapOf(
                getString(R.string.situation) to dataFromRecommendMissionActivity.situation,
                getString(R.string.title) to dataFromRecommendMissionActivity.title

            )
        )
        viewModel.setMissionId(
            dataFromRecommendMissionActivity.id
        )
    }

    private fun setViews() {
        setContentView(binding.root)
        setRecommendActionRecyclerView()
        setMissionTextView()
        setSituationTextView()
        setMissionImageView()
    }

    private fun setMissionTextView() {
        binding.tvRecommendActionMission.text = dataFromRecommendMissionActivity.title
    }

    private fun setSituationTextView() {
        binding.tvRecommendActionSituation.text = dataFromRecommendMissionActivity.situation
    }

    private fun setMissionImageView() {
        binding.ivRecommendActionMissionSituation.load(dataFromRecommendMissionActivity.image)
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
            trackClickCreateRecommendMissionEvent(selectedActionList)
            val recommendUiModel = RecommendUiModel(
                title = dataFromRecommendMissionActivity.title,
                situation = dataFromRecommendMissionActivity.situation,
                actionList = selectedActionList ?: emptyList()
            )
            startActivity(
                Intent(this, AdditionActivity::class.java).putExtra(
                    MISSION_ACTION_DETAIL, recommendUiModel
                )
            )
            if (!isFinishing) finish()
        }
    }

    private fun trackClickCreateRecommendMissionEvent(selectedActionList: List<String>?) {
        val clickCreateRecommendMissionEventPropertyMap = mutableMapOf<String, Any>(
            getString(R.string.situation) to dataFromRecommendMissionActivity.situation,
            getString(R.string.title) to dataFromRecommendMissionActivity.title

        )
        if (selectedActionList != null) clickCreateRecommendMissionEventPropertyMap.plus(
            getString(R.string.action) to selectedActionList.toTypedArray()
        )

        trackEventWithProperty(
            getString(R.string.click_create_recommend_mission),
            clickCreateRecommendMissionEventPropertyMap
        )
    }

    private fun writeDirectTvClickEvent() {
        binding.tvRecommendActionWriteDirect.setOnClickListener {
            trackEvent(getString(R.string.click_self_create_action))
            val recommendUiModel = RecommendUiModel(
                title = dataFromRecommendMissionActivity.title,
                situation = dataFromRecommendMissionActivity.situation,
                actionList = emptyList()
            )
            startActivity(
                Intent(this, AdditionActivity::class.java).putExtra(
                    MISSION_ACTION_DETAIL, recommendUiModel
                )
            )
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
        }
    }

    private fun setRecommendActionSuccessObserver() {
        viewModel.recommendActionListSuccessResponse.observe(this) { actionList ->
            recommendActionAdapter?.submitList(actionList)
        }
    }

    private fun overrideBackPressed() {
        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                startActivity(
                    Intent(
                        this@RecommendActionActivity, RecommendMissionActivity::class.java
                    )
                )
                if (!isFinishing) finish()
            }
        }
        onBackPressedDispatcher.addCallback(this, callback)
    }


    companion object {
        const val MISSION_ACTION_DETAIL = "MISSION_ACTION_DETAIL"
    }
}