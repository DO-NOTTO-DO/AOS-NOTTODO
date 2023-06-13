package kr.co.nottodo.presentation.mypage.view

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import kr.co.nottodo.databinding.ActivityMyPageBinding



class MyPageActivity : Fragment() {

    private var _binding: ActivityMyPageBinding? = null
    private val binding: ActivityMyPageBinding get() = requireNotNull(_binding)


    //    override fun onCreateView(
//        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?,
//    ): View {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {


        _binding = ActivityMyPageBinding.inflate(inflater, container, false)



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

        return binding.root
    }


    private fun replaceFragment(fragment: MyPageInformationActivity) {

    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}

