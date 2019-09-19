package com.snc.farmaccount.budget



import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.snc.farmaccount.R
import com.snc.farmaccount.`object`.Budget
import com.snc.farmaccount.databinding.ItemFarmEditBinding
import com.snc.farmaccount.databinding.ItemFarmTypeBinding
import kotlinx.android.synthetic.main.item_farm_type.view.*

class BudgetAdapter(private val budget: ArrayList<Budget>,var onClickListener: OnClickListener) : RecyclerView.Adapter<BudgetAdapter.BudgetViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BudgetViewHolder {
        return BudgetViewHolder( ItemFarmEditBinding
            .inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: BudgetViewHolder, position: Int) {
        var budget: Budget = budget[position]
        holder.itemView.setOnClickListener {

            onClickListener.onClick(budget)
            notifyDataSetChanged()

        }
        holder.bind(budget)
    }

    override fun getItemCount() = budget.size

    class BudgetViewHolder(private var binding: ItemFarmEditBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(budget: Budget) {
            binding.budget = budget
            binding.budgetFarmImage.setImageResource(budget.farmImage)
            binding.type.setImageResource(budget.farmtype)


            binding.executePendingBindings()
        }

    }
    class OnClickListener(val clickListener: (budget: Budget) -> Unit) {
        fun onClick(budget: Budget) = clickListener(budget)
    }
}

