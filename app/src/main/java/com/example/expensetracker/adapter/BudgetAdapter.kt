package com.example.expensetracker.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.expensetracker.R
import com.example.expensetracker.activities.budget.AddBudgetActivity
import com.example.expensetracker.model.BudgetModel

class BudgetAdapter(var context: Context, var list: List<BudgetModel>) :
    RecyclerView.Adapter<BudgetAdapter.Vh>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Vh {
        val view = LayoutInflater.from(context).inflate(R.layout.list_track, parent, false)
        return Vh(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: Vh, position: Int) {
        val mList = list[position]
        holder.textView_name.text = mList.name
        holder.textview_amount.text = "${mList.amount} $"
        holder.itemView.setOnClickListener {
            val intent = Intent(context, AddBudgetActivity::class.java)
            intent.putExtra("data", mList)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    class Vh(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var textView_name: TextView
        var textview_amount: TextView

        init {
            textView_name = itemView.findViewById(R.id.textView_name)
            textview_amount = itemView.findViewById(R.id.textview_amount)
        }
    }
}