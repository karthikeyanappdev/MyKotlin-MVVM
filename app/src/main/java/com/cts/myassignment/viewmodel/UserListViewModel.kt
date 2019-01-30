package com.cts.myassignment.viewmodel

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LiveData
import com.cts.myassignment.service.model.Facts
import com.cts.myassignment.service.repository.APIRepository

/**
 *  Viewmodel and Livedata
 */

class UserListViewModel(application: Application) : AndroidViewModel(application) {
    var userListObserver : LiveData<Facts>

    init {

        userListObserver = APIRepository(application).getRowList(application)
    }
    fun getUserListObservable():LiveData<Facts>{ return  userListObserver}

}