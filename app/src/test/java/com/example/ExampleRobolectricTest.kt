package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.viewmodel.MainTab
import com.example.viewmodel.StarVibeViewModel
import com.example.viewmodel.SubScreen
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class ExampleRobolectricTest {

  @Test
  fun `read string from context`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val appName = context.getString(R.string.app_name)
    assertEquals("StarVibe", appName)
  }

  @Test
  fun `starvibe viewmodel initial state`() {
    val viewModel = StarVibeViewModel()
    val state = viewModel.uiState.value
    assertEquals(MainTab.PARTY, state.currentTab)
    assertEquals(SubScreen.NONE, state.subScreen)
    assertNotNull(state.rooms)
    assertEquals("Royal Alex", state.user.name)
    assertEquals("SV-778899", state.user.starVibeId)
    assertEquals("Party", state.topNavSelection)
    assertEquals("Global", state.selectedCountry)
  }

  @Test
  fun `starvibe viewmodel top nav and country filter`() {
    val viewModel = StarVibeViewModel()
    viewModel.setCountryFilter("USA")
    assertEquals("USA", viewModel.uiState.value.selectedCountry)

    viewModel.setTopNav("Mine")
    assertEquals("Mine", viewModel.uiState.value.topNavSelection)
  }
}
