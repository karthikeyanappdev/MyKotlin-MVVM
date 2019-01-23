package com.cts.myassignment.view.ui

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.cts.myassignment.R


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if(savedInstanceState == null){
            val fragment = UserListFragment()
            supportFragmentManager.beginTransaction().add(R.id.fragment_container, fragment, UserListFragment.TAG).commit();
        }

    }

    // Update title after fetching API
    fun setActionBarTitle(title : String){
        supportActionBar?.title=title
    }

}
