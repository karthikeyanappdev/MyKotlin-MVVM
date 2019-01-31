package com.cts.myassignment.view.adapter

import android.content.Context
import android.databinding.DataBindingUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.cts.myassignment.R
import com.cts.myassignment.databinding.RecyclerListItemBinding
import com.cts.myassignment.service.model.Row
import com.squareup.picasso.NetworkPolicy
import com.squareup.picasso.Picasso

/**
 *    Adapter with Databinging
 */
class RecyclerAdapter(private val context: Context, private val rowList: MutableList<Row>):RecyclerView.Adapter<RecyclerAdapter.UserViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerAdapter.UserViewHolder {
        val binding = DataBindingUtil.inflate<RecyclerListItemBinding>(LayoutInflater.from(parent.context), R.layout.recycler_list_item,parent,false)
        return UserViewHolder(binding)
    }

    override fun getItemCount(): Int {
       return  rowList!!.size
    }

    override fun onBindViewHolder(holder: RecyclerAdapter.UserViewHolder, position: Int) {
        holder.binding.row = rowList?.get(position)
        Picasso.with(context).load(rowList?.get(position).imageHref).networkPolicy(NetworkPolicy.OFFLINE).error(R.drawable.ic_no_image).into(holder.binding.imgView)

    }

    class UserViewHolder(val binding :RecyclerListItemBinding):RecyclerView.ViewHolder(binding.root)

}

