package com.cts.myassignment.viewmodel

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LiveData
import android.databinding.BindingAdapter
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.cts.myassignment.service.model.Facts
import com.cts.myassignment.service.repository.ProjectRepository

class UserListViewModel(application: Application) : AndroidViewModel(application) {
    var userListObserver : LiveData<Facts>

    init {
        userListObserver = ProjectRepository().getRowList()
    }
    fun getUserListObservable():LiveData<Facts>{ return  userListObserver}

}