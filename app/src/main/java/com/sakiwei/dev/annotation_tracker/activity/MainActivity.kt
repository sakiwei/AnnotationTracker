package com.sakiwei.dev.annotation_tracker.activity

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.sakiwei.demo.annotationTracker.runtime.AttachTrackAttribute
import com.sakiwei.demo.annotationTracker.runtime.AttachTrackAttributes
import com.sakiwei.demo.annotationTracker.runtime.TrackAction
import com.sakiwei.dev.annotation_tracker.databinding.ActivityMainBinding
import com.sakiwei.dev.annotation_tracker.viewmodel.MainUiState
import com.sakiwei.dev.annotation_tracker.viewmodel.MainViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AttachTrackAttributes(
  values = [
    AttachTrackAttribute(key = "screen_name", value = "MAIN_SCREEN"),
  ]
)
@AttachTrackAttribute(key = "screen_name2", value = "MAIN_SCREEN")
class MainActivity : AppCompatActivity() {

  private lateinit var mainViewModel: MainViewModel
  private lateinit var binding: ActivityMainBinding

  @TrackAction("on_create")
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    binding = ActivityMainBinding.inflate(layoutInflater)
    setContentView(binding.root)

    val viewModel by viewModels<MainViewModel>()
    mainViewModel = viewModel

    lifecycleScope.launch {
      repeatOnLifecycle(Lifecycle.State.STARTED) {
        viewModel.uiState.collect { uiState ->
          when (uiState) {
            is MainUiState.NotLoggedIn -> {
              binding.textView.text = "Not Logged In"
              binding.button.text = "Login"
            }
            is MainUiState.LoggedIn -> {
              binding.textView.text = "Logged In"
              binding.button.text = "Logout"
            }
          }
        }
      }
    }

    binding.button.setOnClickListener {
      mainViewModel.onClickLoginToggle()
    }
  }

  @TrackAction("on_resume")
  override fun onResume() {
    super.onResume()
  }
}