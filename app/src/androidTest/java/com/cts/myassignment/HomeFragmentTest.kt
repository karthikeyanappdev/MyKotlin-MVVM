package com.cts.myassignment

import android.app.Activity
import android.content.Intent
import android.os.SystemClock
import android.support.test.espresso.Espresso
import android.support.test.espresso.action.ViewActions
import android.support.test.espresso.assertion.ViewAssertions
import android.support.test.espresso.matcher.ViewMatchers
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import com.cts.myassignment.view.ui.HomeFragment
import com.cts.myassignment.view.ui.HomeScreen
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class HomeFragmentTest {
    @Rule
    @JvmField
    public val mActivityRule: ActivityTestRule<HomeScreen> = ActivityTestRule(HomeScreen::class.java)

    private lateinit var activity : Activity

    @Test
    fun lauchHomeScreenTest(){
        statActivity()
        mActivityRule.getActivity().runOnUiThread(Runnable {

        })
        loadingTime()
        Espresso.onView(ViewMatchers.withId(R.id.fragment_container))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
    }
    @Test
    fun launchHomeFragmentTest(){
        loadFragments()
        loadingTime()
    }

    @Test
    fun loadDataApiTest(){
        loadFragments()
        Espresso.onData(ViewMatchers.withId(R.id.txtTitle)).inAdapterView(ViewMatchers.withId(R.id.recycler_view)).atPosition(0)
        loadingTime()
    }

    @Test
    fun onRefreshTest(){
        loadFragments()
        Espresso.onData(ViewMatchers.withId(R.id.txtTitle)).inAdapterView(ViewMatchers.withId(R.id.recycler_view)).atPosition(0)
        Espresso.onData(ViewMatchers.withId(R.id.refresh)).inAdapterView(ViewMatchers.withId(R.id.fragment_container)).check(
            ViewAssertions.matches(ViewMatchers.isDisplayed())
        )
        Espresso.onData(ViewMatchers.withContentDescription("Refresh"))
            .inAdapterView(ViewMatchers.withId(R.id.fragment_container)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        Espresso.onData(ViewMatchers.withId(R.id.refresh)).inAdapterView(ViewMatchers.withId(R.id.fragment_container)).perform(
            ViewActions.click()
        )
        loadingTime()
    }

    fun loadFragments(){
        mActivityRule.activity.supportFragmentManager.beginTransaction().add(R.id.fragment_container, HomeFragment(), "HomeFragment").commitAllowingStateLoss()
    }
    fun loadingTime(){
        Thread.sleep(2000)
    }

    private fun statActivity() {
        val intent = Intent()
        activity = mActivityRule.launchActivity(intent)
        SystemClock.sleep(1000)
        activity = Util().getActivityInstance()!!
    }
}