package kr.co.nottodo.presentation.addition

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.updatePadding
import com.google.android.material.internal.ViewUtils.dpToPx
import kr.co.nottodo.databinding.ActivityAdditionBinding
import kr.co.nottodo.util.dpToPx

class AdditionActivity : AppCompatActivity() {
    lateinit var binding: ActivityAdditionBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAdditionBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}