package com.cts.myassignment.viewmodel

import android.databinding.BindingAdapter
import android.view.View


class IndicatorViewModel {
    @BindingAdapter("visibleGone")
    fun showHide(view: View, show: Boolean) {
        view.setVisibility(if (show) View.VISIBLE else View.GONE)
    }
}