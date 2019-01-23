package com.cts.myassignment.view.adapter

import android.content.Context
import android.databinding.DataBindingUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.cts.myassignment.R
import com.cts.myassignment.databinding.RecyclerListItemBinding
import com.cts.myassignment.service.model.Row


class RecyclerAdapter(val context: Context, private val rowList: MutableList<Row>):RecyclerView.Adapter<RecyclerAdapter.UserViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerAdapter.UserViewHolder {
        val binding = DataBindingUtil.inflate<RecyclerListItemBinding>(LayoutInflater.from(parent.context), com.cts.myassignment.R.layout.recycler_list_item,parent,false)
        return UserViewHolder(binding)
    }

    override fun getItemCount(): Int {
       return  rowList!!.size
    }

    override fun onBindViewHolder(holder: RecyclerAdapter.UserViewHolder, position: Int) {
        holder.binding.row = rowList?.get(position)

        val options = RequestOptions()
            .centerCrop()
            .error(R.drawable.ic_no_image)
            .diskCacheStrategy(DiskCacheStrategy.ALL)

        Glide.with(context).load(rowList?.get(position).imageHref).apply(options)
            .into(holder.binding.imgView)
        holder.binding.executePendingBindings()

    }

    class UserViewHolder(val binding :RecyclerListItemBinding):RecyclerView.ViewHolder(binding.root)

}

