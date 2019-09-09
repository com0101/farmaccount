package com.snc.farmaccount


import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.snc.farmaccount.`object`.*
import com.snc.farmaccount.budget.BudgetAdapter
import com.snc.farmaccount.event.TagAdapter
import com.snc.farmaccount.home.DayEventAdapter
import com.snc.farmaccount.statistic.StatisticCatagoryAdapter
import com.snc.farmaccount.statistic.StatisticEventAdapter

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

@BindingAdapter("listStatistic")
fun bindStatistic(recyclerView: RecyclerView, data: List<Event>?) {
    val adapter = recyclerView.adapter as StatisticEventAdapter
    data?.let {
        adapter.submitList(data)
    }
}

@BindingAdapter("listTotal")
fun bindTotal(recyclerView: RecyclerView, data: List<SumEvent>?) {
    val adapter = recyclerView.adapter as StatisticCatagoryAdapter
    data?.let {
        adapter.submitList(data)
    }
}

