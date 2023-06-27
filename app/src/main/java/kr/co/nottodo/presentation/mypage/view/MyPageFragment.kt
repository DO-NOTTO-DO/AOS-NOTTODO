package kr.co.nottodo.presentation.mypage.view

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.gms.oss.licenses.OssLicensesMenuActivity
import kr.co.nottodo.data.local.SharedPreferences
import kr.co.nottodo.databinding.FragmentMyPageBinding
import kr.co.nottodo.listeners.OnFragmentChangedListener
import kr.co.nottodo.presentation.login.view.LoginActivity.Companion.USER_EMAIL
import kr.co.nottodo.presentation.login.view.LoginActivity.Companion.USER_NAME

class MyPageFragment : Fragment() {
    private var _binding: FragmentMyPageBinding? = null
    private val binding: FragmentMyPageBinding
        get() = requireNotNull(_binding)
    private var onFragmentChangedListener: OnFragmentChangedListener? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        onFragmentChangedListener = context as? OnFragmentChangedListener
            ?: throw TypeCastException("context can not cast as OnFragmentChangedListener")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentMyPageBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setActivityBackgroundColor()
        setClickEvents()
        setViews()
    }

    private fun setViews() {
        setUserName()
        setUserEmail()
    }

    private fun setUserEmail() {
        binding.tvMyPageEmail.text = SharedPreferences.getString(USER_EMAIL) ?: "연동된 이메일 정보가 없습니다."
    }

    private fun setUserName() {
        binding.tvMyPageName.text = SharedPreferences.getString(USER_NAME) ?: "익명의 도전자"
    }

    private fun setClickEvents() {
        setNameClickEvent()
        setGuideClickEvent()
        setQuestionClickEvent()
        setNoticeClickEvent()
        setContactClickEvent()
        setPoliciesClickEvent()
        setOssClickEvent()
    }

    private fun setOssClickEvent() {
        binding.layoutMyPageOss.setOnClickListener {
            startActivity(Intent(requireContext(), OssLicensesMenuActivity::class.java))
        }
    }

    private fun setPoliciesClickEvent() {
        binding.layoutMyPagePolicies.setOnClickListener {
            // 버튼을 클릭했을 때 실행할 코드
            val intent = Intent(
                Intent.ACTION_VIEW,
                Uri.parse("https://teamnottodo.notion.site/81594da775614d23900cdb2475eadb73")
            )
            startActivity(intent)
        }
    }

    private fun setContactClickEvent() {
        binding.layoutMyPageContact.setOnClickListener {
            // 버튼을 클릭했을 때 실행할 코드
            val intent = Intent(
                Intent.ACTION_VIEW, Uri.parse("http://pf.kakao.com/_fUIQxj/chat")
            )
            startActivity(intent)
        }
    }

    private fun setNoticeClickEvent() {
        binding.layoutMyPageNotice.setOnClickListener {
            // 버튼을 클릭했을 때 실행할 코드
            val intent = Intent(
                Intent.ACTION_VIEW,
                Uri.parse("https://teamnottodo.notion.site/a5dbb310ec1d43baae02b7e9bf0b3411")
            )
            startActivity(intent)
        }
    }

    private fun setQuestionClickEvent() {
        binding.layoutMyPageQuestion.setOnClickListener {
            // 버튼을 클릭했을 때 실행할 코드
            val intent = Intent(
                Intent.ACTION_VIEW,
                Uri.parse("https://teamnottodo.notion.site/a6ef7036bde24e289e576ace099f39dc")
            )
            startActivity(intent)
        }
    }

    private fun setGuideClickEvent() {
        binding.layoutMyPageGuide.setOnClickListener {
            // 버튼을 클릭했을 때 실행할 코드
            val intent = Intent(
                Intent.ACTION_VIEW,
                Uri.parse("https://teamnottodo.notion.site/f35a7f2d6d5c4b33b4d0949f6077e6cd")
            )
            startActivity(intent)
        }
    }

    private fun setNameClickEvent() {
        binding.layoutMypageName.setOnClickListener {
            val intent = Intent(requireContext(), MyPageInformationActivity::class.java)
            startActivity(intent)
        }
    }

    private fun setActivityBackgroundColor() {
        onFragmentChangedListener?.setActivityBackgroundColorBasedOnFragment(this@MyPageFragment)
            ?: throw NullPointerException("onFragmentChangedListener is null")
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    override fun onDetach() {
        onFragmentChangedListener = null
        super.onDetach()
    }
}