package com.hodgdon.nav

import androidx.navigation.testing.TestNavHostController
import androidx.test.annotation.UiThreadTest
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class NavGraphTest {
    @UiThreadTest
    @Test
    fun whenOnA2AndNextPressed_onA3() {
        val navController = TestNavHostController(
            ApplicationProvider.getApplicationContext()
        )
        navController.setGraph(R.navigation.nav_graph)
        navController.setCurrentDestination(R.id.a2)
        navController.navigate(R.id.action_a2_to_a3)
        assertEquals(R.id.a3, navController.currentDestination?.id)
    }
}