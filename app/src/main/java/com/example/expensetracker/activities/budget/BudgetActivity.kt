package com.example.expensetracker.activities.budget

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
import com.example.expensetracker.adapter.BudgetAdapter
import com.example.expensetracker.database.DatabaseDao
import com.example.expensetracker.database.DatabaseInstance
import com.example.expensetracker.model.BudgetModel
import com.example.expensetracker.model.HelperClass
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class BudgetActivity : AppCompatActivity() {

    var image_back: ImageView? = null
    var textview_budget: TextView? = null
    var no_budget_found:TextView? = null
    var recyclerView_budget: RecyclerView? = null
    var fab_add: FloatingActionButton? = null
    var list: List<BudgetModel>? = null
    var amountAdapter: BudgetAdapter? = null
    var getTotalBudget: Double? = null
    private lateinit var databaseInstance: DatabaseInstance
    private lateinit var databaseDao: DatabaseDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_budget)
        window.statusBarColor = ContextCompat.getColor(this, R.color.button_color)

        image_back = findViewById(R.id.image_back)
        textview_budget = findViewById(R.id.textview_budget)
        no_budget_found = findViewById(R.id.no_budget_found)
        recyclerView_budget = findViewById(R.id.recyclerView_budget)
        fab_add = findViewById(R.id.fab_add)
        databaseInstance = DatabaseInstance.getDatabase(this)
        databaseDao = databaseInstance.databaseDao()


        image_back?.setOnClickListener{
            onBackPressed()
            finish()
        }

        fab_add?.setOnClickListener {
            startActivity(
                Intent(
                    this@BudgetActivity,
                    AddBudgetActivity::class.java
                )
            )
        }
    }

    @SuppressLint("NotifyDataSetChanged", "SetTextI18n")
    override fun onResume() {
        super.onResume()

        GlobalScope.launch(Dispatchers.IO) {
            getTotalBudget = databaseDao.getTotalBudget(HelperClass.userModel?.id?.toLong()!!)
            list = databaseDao.getAllBudgets(HelperClass.userModel?.id?.toLong()!!)
            runOnUiThread {
                textview_budget!!.text =
                    "Your total budget: $getTotalBudget €"
                amountAdapter = BudgetAdapter(this@BudgetActivity, list!!)
                recyclerView_budget!!.layoutManager = LinearLayoutManager(this@BudgetActivity)
                recyclerView_budget!!.adapter = amountAdapter
                amountAdapter!!.notifyDataSetChanged()

                if (list!!.isNotEmpty()) {
                    no_budget_found!!.visibility = View.GONE
                }
            }
        }
    }

}