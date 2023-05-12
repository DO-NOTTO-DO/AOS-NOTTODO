package kr.co.nottodo

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import com.google.android.material.shape.CornerFamily
import com.google.android.material.shape.MaterialShapeDrawable
import kr.co.nottodo.databinding.ActivityMainBinding
import kr.co.nottodo.presentation.achieve.AchieveFragment
import kr.co.nottodo.presentation.home.view.HomeFragment
import kr.co.nottodo.presentation.mypage.view.MyPageFragment

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initBottomNavigationView(savedInstanceState)
    }

    private fun initBottomNavigationView(savedInstanceState: Bundle?) {
        binding.bnvMain.itemIconTintList = null

        val radius = resources.getDimension(R.dimen.bnv_radius)
        val bottomNavigationViewBackground = binding.bnvMain.background as MaterialShapeDrawable
        bottomNavigationViewBackground.shapeAppearanceModel =
            bottomNavigationViewBackground.shapeAppearanceModel.toBuilder()
                .setTopRightCorner(CornerFamily.ROUNDED, radius)
                .setTopLeftCorner(CornerFamily.ROUNDED, radius)
                .build()

        if (savedInstanceState == null) {
            changeFragment(HomeFragment())
        }

        binding.bnvMain.setOnItemSelectedListener {
            changeFragment(
                when (it.itemId) {
                    R.id.menu_home -> HomeFragment()
                    R.id.menu_calendar -> AchieveFragment()
                    else -> MyPageFragment()
                }
            )
            true
        }
    }

    private fun changeFragment(fragment: Fragment) {
        supportFragmentManager.commit {
            replace(R.id.fcv_main, fragment)
        }

        when (fragment) {
            is HomeFragment -> binding.root.setBackgroundColor(getColor(R.color.bg_f2f2f7))
            is AchieveFragment -> binding.root.setBackgroundColor(getColor(R.color.black))
            is MyPageFragment -> binding.root.setBackgroundColor(getColor(R.color.black))
        }
    }

    companion object {
        const val BLANK = ""
    }
}