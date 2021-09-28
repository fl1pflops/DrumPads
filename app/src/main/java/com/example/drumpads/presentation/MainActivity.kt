package com.example.drumpads.presentation

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.drumpads.databinding.ActivityMainBinding
import com.example.drumpads.presentation.view.PadButton
import org.koin.androidx.viewmodel.ext.android.getViewModel

class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: SamplerViewModel
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = getViewModel()

        repeat(9) { index ->
            createAndAddPadButton(index)
        }
    }

    private fun createAndAddPadButton(index: Int) {
        val button = createPadButton(index)
        binding.root.addView(button)

        val referencedIds = binding.flowHelper.referencedIds
        binding.flowHelper.referencedIds = referencedIds.plus(button.id)
    }

    private fun createPadButton(index: Int) = PadButton(this).apply {
        id = View.generateViewId()
        layoutParams = ConstraintLayout.LayoutParams(0, 0).apply {
            dimensionRatio = "1:1"
        }

        setOnClickListener {
            viewModel.handleAction(Action.PlaySample(index))
        }
    }

}
