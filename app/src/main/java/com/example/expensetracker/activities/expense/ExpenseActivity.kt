package com.example.expensetracker.activities.expense

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.expensetracker.R
import com.example.expensetracker.adapter.ExpenseAdapter
import com.example.expensetracker.database.DatabaseDao
import com.example.expensetracker.database.DatabaseInstance
import com.example.expensetracker.model.ExpenseModel
import com.example.expensetracker.model.HelperClass
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class ExpenseActivity : AppCompatActivity() {

    var image_back: ImageView? = null
    var textview_expense: TextView? = null
    var textview_budget:TextView? = null
    var no_expense_found:TextView? = null
    var recyclerView_expense: RecyclerView? = null
    var fab_add: FloatingActionButton? = null
    var list: List<ExpenseModel>? = null
    var expenseAdapter: ExpenseAdapter? = null
    var remainingBudget: Double = 0.0
    var getTotalBudget: Double = 0.0
    var getTotalExpense: Double = 0.0
    private lateinit var databaseInstance: DatabaseInstance
    private lateinit var databaseDao: DatabaseDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_expense)
        window.statusBarColor = ContextCompat.getColor(this, R.color.button_color)

        image_back = findViewById(R.id.image_back)
        textview_expense = findViewById(R.id.textview_expense)
        textview_budget = findViewById(R.id.textview_budget)
        no_expense_found = findViewById(R.id.no_expense_found)
        recyclerView_expense = findViewById(R.id.recyclerView_expense)
        fab_add = findViewById(R.id.fab_add)
        databaseInstance = DatabaseInstance.getDatabase(this)
        databaseDao = databaseInstance.databaseDao()

        image_back?.setOnClickListener{
            onBackPressed()
            finish()
        }

        fab_add?.setOnClickListener{
            startActivity(
                Intent(
                    this@ExpenseActivity,
                    AddExpenseActivity::class.java
                )
            )
        }

    }

    @SuppressLint("SetTextI18n", "NotifyDataSetChanged")
    override fun onResume() {
        super.onResume()
        GlobalScope.launch(Dispatchers.IO) {
            getTotalBudget =  databaseDao.getTotalBudget(HelperClass.userModel?.id?.toLong()!!)
            getTotalExpense = databaseDao.getTotalExpenses(HelperClass.userModel?.id?.toLong()!!)
            list = databaseDao.getAllExpenses(HelperClass.userModel?.id?.toLong()!!)
            runOnUiThread {

                remainingBudget = getTotalBudget-getTotalExpense
                textview_expense!!.text = "My current expenses: $getTotalExpense €"
                textview_budget!!.text = "My current budget: $remainingBudget €"
                expenseAdapter = ExpenseAdapter(this@ExpenseActivity, list!!)
                recyclerView_expense!!.layoutManager = LinearLayoutManager(this@ExpenseActivity)
                recyclerView_expense!!.adapter = expenseAdapter
                expenseAdapter!!.notifyDataSetChanged()
                if (list!!.isNotEmpty()) {
                    no_expense_found!!.visibility = View.GONE
                }

            }
        }
    }
}