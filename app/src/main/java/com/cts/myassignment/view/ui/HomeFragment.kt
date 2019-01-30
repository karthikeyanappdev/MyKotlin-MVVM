package com.cts.myassignment.view.ui


import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.annotation.Nullable
import android.support.v4.app.Fragment
import android.view.*
import com.cts.myassignment.R
import com.cts.myassignment.databinding.FragmentUserListBinding
import com.cts.myassignment.service.model.Facts
import com.cts.myassignment.service.model.Row
import com.cts.myassignment.view.adapter.RecyclerAdapter
import com.cts.myassignment.viewmodel.UserListViewModel
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexWrap
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent


/**
 *  Fragment with Recyclerview
 */

class HomeFragment : Fragment() {

    private var binding : FragmentUserListBinding ?= null
    private var rowList = mutableListOf<Row>()
    private var recyclerAdapter : RecyclerAdapter ?= null

    @Nullable
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_user_list, container, false)
        setHasOptionsMenu(true)
        binding?.recyclerView?.layoutManager = getLayoutManager()
        recyclerAdapter = RecyclerAdapter(context as Context, rowList)
        binding?.recyclerView?.adapter = recyclerAdapter
        binding?.isLoading =true
        return binding?.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val viewModel = ViewModelProviders.of(this).get(UserListViewModel::class.java)
        getUserList(viewModel)
    }

    // Fetching API after update Recyclerview adapter and ActionBar
    fun getUserList(viewModel: UserListViewModel){
        viewModel.getUserListObservable().observe(this, Observer<Facts> {facts ->
            if(facts != null){
                binding?.isLoading = false
                rowList.clear()
                rowList.addAll(facts.rows)
                (activity as HomeScreen).setActionBarTitle(facts.title)
                recyclerAdapter?.notifyDataSetChanged()
            }
        } )
    }

    // update recyclerview layout manager
    private fun getLayoutManager(): FlexboxLayoutManager {
        val layoutManager = FlexboxLayoutManager(context as Context)
        layoutManager.setFlexDirection(FlexDirection.ROW)
        layoutManager.setFlexWrap(FlexWrap.WRAP)
        layoutManager.setJustifyContent(JustifyContent.FLEX_START)
        return layoutManager
    }

    companion object {
        val TAG = "ProjectListFragment"
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater?.inflate(com.cts.myassignment.R.menu.mymenu, menu);
        super.onCreateOptionsMenu(menu,inflater);
    }

    override fun onOptionsItemSelected(item: MenuItem?):Boolean {
        val id = item?.getItemId()
        when (id) {
            R.id.refresh -> {
                if (rowList != null) {
                    binding?.isLoading =true
                    recyclerAdapter?.notifyDataSetChanged()
                    binding?.isLoading =false
                }
                return true
            }
        }
        return false
    }



}
