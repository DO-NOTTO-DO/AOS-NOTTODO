package kr.co.nottodo.presentation.mypage.view

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.google.android.gms.oss.licenses.OssLicensesMenuActivity
import kr.co.nottodo.R
import kr.co.nottodo.data.local.SharedPreferences
import kr.co.nottodo.databinding.FragmentMyPageBinding
import kr.co.nottodo.listeners.OnFragmentChangedListener
import kr.co.nottodo.presentation.base.fragment.ViewBindingFragment
import kr.co.nottodo.presentation.login.view.LoginFragment.Companion.USER_EMAIL
import kr.co.nottodo.presentation.login.view.LoginFragment.Companion.USER_NAME
import kr.co.nottodo.util.NotTodoAmplitude.trackEvent

class MyPageFragment : ViewBindingFragment<FragmentMyPageBinding>() {
    private var onFragmentChangedListener: OnFragmentChangedListener? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        onFragmentChangedListener =
            context as? OnFragmentChangedListener ?: throw TypeCastException(
                getString(
                    R.string.context_can_not_cast_as,
                    getString(R.string.on_fragment_changed_listener)
                )
            )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        trackEvent(getString(R.string.view_my_info))
        setActivityBackgroundColor()
        setClickEvents()
        setViews()
    }

    private fun setViews() {
        setUserName()
        setUserEmail()
    }

    private fun setUserEmail() {
        binding.tvMyPageEmail.text =
            SharedPreferences.getString(USER_EMAIL) ?: getString(R.string.no_email)
    }

    private fun setUserName() {
        binding.tvMyPageName.text =
            SharedPreferences.getString(USER_NAME) ?: getString(R.string.no_name)
    }

    private fun setClickEvents() {
        setNameClickEvent()
        setGuideClickEvent()
        setQuestionClickEvent()
        setNoticeClickEvent()
        setFeedbackClickEvent()
        setContactClickEvent()
        setPoliciesClickEvent()
        setOssClickEvent()
    }

    private fun setOssClickEvent() {
        binding.layoutMyPageOss.setOnClickListener {
            trackEvent(getString(R.string.click_opensource))
            startActivity(Intent(requireContext(), OssLicensesMenuActivity::class.java))
        }
    }

    private fun setPoliciesClickEvent() {
        binding.layoutMyPagePolicies.setOnClickListener {
            trackEvent(getString(R.string.click_terms))
            val intent = Intent(
                Intent.ACTION_VIEW, Uri.parse(getString(R.string.url_polices))
            )
            startActivity(intent)
        }
    }

    private fun setContactClickEvent() {
        binding.layoutMyPageContact.setOnClickListener {
            trackEvent(getString(R.string.click_question))
            val intent = Intent(
                Intent.ACTION_VIEW, Uri.parse(getString(R.string.url_contact))
            )
            startActivity(intent)
        }
    }

    private fun setNoticeClickEvent() {
        binding.layoutMyPageNotice.setOnClickListener {
            trackEvent(getString(R.string.click_notice))
            val intent = Intent(
                Intent.ACTION_VIEW, Uri.parse(getString(R.string.url_notice))
            )
            startActivity(intent)
        }
    }

    private fun setFeedbackClickEvent() {
        binding.layoutMyPageFeedback.setOnClickListener {
            trackEvent(getString(R.string.click_suggestion))
            Intent(
                Intent.ACTION_VIEW, Uri.parse(getString(R.string.url_feedback))
            ).also { intent ->
                startActivity(intent)
            }
        }
    }

    private fun setQuestionClickEvent() {
        binding.layoutMyPageQuestion.setOnClickListener {
            trackEvent(getString(R.string.click_faq))
            val intent = Intent(
                Intent.ACTION_VIEW, Uri.parse(getString(R.string.url_faq))
            )
            startActivity(intent)
        }
    }

    private fun setGuideClickEvent() {
        binding.layoutMyPageGuide.setOnClickListener {
            trackEvent(getString(R.string.click_guide))
            val intent = Intent(
                Intent.ACTION_VIEW, Uri.parse(getString(R.string.url_guide))
            )
            startActivity(intent)
        }
    }

    private fun setNameClickEvent() {
        binding.layoutMypageName.setOnClickListener {
            trackEvent(getString(R.string.click_my_info))
            findNavController().navigate(R.id.action_myPageFragment_to_myPageInformationFragment)
        }
    }

    private fun setActivityBackgroundColor() {
        onFragmentChangedListener?.setActivityBackgroundColorBasedOnFragment(this@MyPageFragment)
            ?: throw NullPointerException(
                getString(
                    R.string._is_null, getString(R.string.on_fragment_changed_listener)
                )
            )
    }

    override fun onDetach() {
        onFragmentChangedListener = null
        super.onDetach()
    }

    override fun setBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
    ): FragmentMyPageBinding = FragmentMyPageBinding.inflate(inflater, container, false)

}