package com.cts.myassignment

import android.app.Activity
import android.content.Intent
import android.os.SystemClock
import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.matcher.ViewMatchers
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import com.cts.myassignment.view.ui.HomeFragment
import com.cts.myassignment.view.ui.HomeScreen
import android.support.test.espresso.matcher.ViewMatchers.isDisplayed
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class  HomeScreenTest{

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
        onView(ViewMatchers.withId(R.id.fragment_container)).check(matches(isDisplayed()));
    }

    @Test
    fun launchHomeFragmentTest(){
        loadFragments()
        loadingTime()
    }

    fun loadFragments(){
      mActivityRule.activity.supportFragmentManager.beginTransaction().add(R.id.fragment_container, HomeFragment(), "HomeFragment").commit()
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