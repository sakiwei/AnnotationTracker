package com.sakiwei.dev.annotation_tracker.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sakiwei.demo.annotationTracker.runtime.*
import com.sakiwei.dev.annotation_tracker.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

sealed class MainUiState {
  object NotLoggedIn : MainUiState()
  data class LoggedIn(val username: String) : MainUiState()
}

@AttachTrackAttributes(
  values = [
    AttachTrackAttribute(key = "view_model", value = "main_view_model")
  ]
)
class MainViewModel : ViewModel() {

  private val _uiState = MutableStateFlow<MainUiState>(MainUiState.NotLoggedIn)

  // The UI collects from this StateFlow to get its state updates
  val uiState: StateFlow<MainUiState> = _uiState

  @TrackAction("on_click_login_toggle")
  fun onClickLoginToggle() {
    Log.d("MAIN_ACTIVITY", "click")
    viewModelScope.launch {
      if (_uiState.value is MainUiState.NotLoggedIn) {
        onLoggedIn()
      } else {
        onLoggedOut()
      }
    }
  }

  @TrackAction("on_logged_in")
  private fun onLoggedIn() {
    val username = "Testuser"
    callLoginAPI(username, User(id = 1, email = "testuser@gmail.com", name = "Unknown"))
    _uiState.value = MainUiState.LoggedIn(username = username)
    Tracker.addAttribute("username", username)
  }

  @TrackAction("call_login_api")
  private fun callLoginAPI(
    @TrackParameterNamed(name = "username") username: String,
    @TrackParameter userObject: User
  ) {

  }

  @TrackAction("on_logged_out")
  private fun onLoggedOut() {
    _uiState.value = MainUiState.NotLoggedIn
    Tracker.removeAttribute("username")
  }
}