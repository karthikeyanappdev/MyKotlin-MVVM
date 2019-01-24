package com.cts.myassignment.apiservice

import android.arch.lifecycle.LiveData
import com.cts.myassignment.service.model.Facts
import org.junit.Test
import org.mockito.Mockito
import org.mockito.Mockito.times


class UserListViewModelTest {

     var listObserver : LiveData<Facts>?= null

    @Test
    fun getLiveData(){
        Mockito.verify(listObserver, times(1))?.value
    }
}