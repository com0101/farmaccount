package com.snc.farmaccount


import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.snc.farmaccount.`object`.Budget
import com.snc.farmaccount.`object`.Event
import com.snc.farmaccount.`object`.Tag
import com.snc.farmaccount.budget.BudgetAdapter
import com.snc.farmaccount.event.TagAdapter
import com.snc.farmaccount.home.DayEventAdapter

@BindingAdapter("listEvent")
fun bindEvent(recyclerView: RecyclerView, data: List<Event>?) {
    val adapter = recyclerView.adapter as DayEventAdapter
    data?.let {
        adapter.submitList(data)
    }
}

@BindingAdapter("listTag")
fun bindTag(recyclerView: RecyclerView, data: List<Tag>?) {
    val adapter = recyclerView.adapter as TagAdapter
    data?.let {
        adapter.submitList(data)
    }
}

