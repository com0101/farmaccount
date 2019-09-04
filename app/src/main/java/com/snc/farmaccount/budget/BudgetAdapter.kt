package com.snc.farmaccount.budget



import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.snc.farmaccount.`object`.Budget
import com.snc.farmaccount.databinding.ItemFarmTypeBinding

class BudgetAdapter(var onClickListener: OnClickListener, var viewModel: BudgetViewModel):
    ListAdapter<Budget, BudgetAdapter.BudgetViewHolder>(DiffCallback) {

    var selectedPosition = -1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BudgetViewHolder {
        return BudgetViewHolder(
            ItemFarmTypeBinding
                .inflate(LayoutInflater.from(parent.context), parent, false)
        )

    }
    companion object DiffCallback : DiffUtil.ItemCallback<Budget>() {
        override fun areItemsTheSame(oldItem: Budget, newItem: Budget): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: Budget, newItem: Budget): Boolean {
            return oldItem == newItem
        }
    }

    override fun onBindViewHolder(holder: BudgetViewHolder, position: Int) {
        val budget: Budget = getItem(position)


        holder.itemView.setOnClickListener {
            selectedPosition = position
            onClickListener.onClick(budget)
            notifyDataSetChanged()

        }
        holder.bind(budget, selectedPosition)
    }


    class BudgetViewHolder(private var binding: ItemFarmTypeBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(budget: Budget, selectedPosition: Int) {
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