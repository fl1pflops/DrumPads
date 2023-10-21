package com.example.drumpads.presentation

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.drumpads.R
import com.example.drumpads.databinding.ActivityMainBinding
import com.example.drumpads.presentation.view.PadButton
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.getViewModel

class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: SamplerViewModel
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = getViewModel()

        binding.changeSamplePackButton.setOnClickListener {
            viewModel.handleAction(Action.ChangeSamplePack)
        }

        observeUiState()
    }

    private fun observeUiState() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.CREATED) {
                viewModel.state.collect { uiState ->
                    when (uiState) {
                        UiState.Initializing -> {
                            showGlobalInfo(getString(R.string.initializing))
                        }

                        is UiState.Loaded -> {
                            if (binding.flowHelper.referencedIds.isEmpty()) {
                                repeat(9) { index ->
                                    createAndAddPadButton(index)
                                }
                            }
                            binding.apply {
                                changeSamplePackButton.text = getString(
                                    R.string.change_sample_pack,
                                    uiState.currentSamplePackName
                                )
                                changeSamplePackButton.isVisible =
                                    uiState.showChangeSamplePackButton
                                globalInfoTextView.isVisible = false
                                flowHelper.isVisible = true
                            }
                        }

                        is UiState.Error -> handleUiError(uiState.type)
                    }
                }
            }
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
        isSoundEffectsEnabled = false

        setOnClickListener {
            viewModel.handleAction(Action.PlaySample(index))
        }
    }

    private fun handleUiError(errorType: ErrorType) {
        when (errorType) {
            ErrorType.NO_SAMPLE_PACKS -> {
                showGlobalInfo(getString(R.string.no_sample_packs))
            }
        }
    }

    private fun showGlobalInfo(text: String) {
        binding.apply {
            globalInfoTextView.text = text
            globalInfoTextView.isVisible = true
            changeSamplePackButton.isVisible = false
            flowHelper.isVisible = false
        }
    }

}
