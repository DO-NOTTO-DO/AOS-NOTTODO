package kr.co.nottodo.presentation.mypage.view

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import kr.co.nottodo.databinding.FragmentMyPageBinding
import kr.co.nottodo.listeners.OnFragmentChangedListener

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

        binding.tvNottodoGuide.setOnClickListener {
            // 버튼을 클릭했을 때 실행할 코드
            val intent = Intent(
                Intent.ACTION_VIEW,
                Uri.parse("https://teamnottodo.notion.site/f35a7f2d6d5c4b33b4d0949f6077e6cd")
            )
            startActivity(intent)
        }
        binding.tvQuestion.setOnClickListener {
            // 버튼을 클릭했을 때 실행할 코드
            val intent = Intent(
                Intent.ACTION_VIEW,
                Uri.parse("https://teamnottodo.notion.site/a6ef7036bde24e289e576ace099f39dc")
            )
            startActivity(intent)
        }
        binding.tvNotice.setOnClickListener {
            // 버튼을 클릭했을 때 실행할 코드
            val intent = Intent(
                Intent.ACTION_VIEW,
                Uri.parse("https://teamnottodo.notion.site/a5dbb310ec1d43baae02b7e9bf0b3411")
            )
            startActivity(intent)
        }
        binding.tvContact.setOnClickListener {
            // 버튼을 클릭했을 때 실행할 코드
            val intent = Intent(
                Intent.ACTION_VIEW,
                Uri.parse("http://pf.kakao.com/_fUIQxj/chat")
            )
            startActivity(intent)
        }
        binding.tvPolicies.setOnClickListener {
            // 버튼을 클릭했을 때 실행할 코드
            val intent = Intent(
                Intent.ACTION_VIEW,
                Uri.parse("https://teamnottodo.notion.site/5af34df7da3649fc941312c5f533c1eb")
            )
            startActivity(intent)
        }
        binding.layoutName.setOnClickListener {
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