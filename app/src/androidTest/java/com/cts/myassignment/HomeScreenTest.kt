package com.cts.myassignment

import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import com.cts.myassignment.view.ui.HomeFragment
import com.cts.myassignment.view.ui.HomeScreen
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class  HomeScreenTest{

    @Rule
    @JvmField
    public val mActivityRule: ActivityTestRule<HomeScreen> = ActivityTestRule(HomeScreen::class.java)


    @Test
    fun launchHomeFragmentTest(){
        loadFragments()
        loadingTime()
    }

    fun loadFragments(){
      mActivityRule.activity.supportFragmentManager.beginTransaction().add(R.id.fragment_container, HomeFragment(), "HomeFragment").commit()
    }
    fun loadingTime(){
        Thread.sleep(5000)
    }



}