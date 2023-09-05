package com.example.expensetracker

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import com.example.expensetracker.activities.about.AboutActivity
import com.example.expensetracker.activities.auth.LoginActivity
import com.example.expensetracker.activities.budget.BudgetActivity
import com.example.expensetracker.activities.expense.ExpenseActivity
import com.example.expensetracker.database.DatabaseDao
import com.example.expensetracker.database.DatabaseInstance
import com.example.expensetracker.model.HelperClass
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    var button_budget: CardView? = null
    var button_expenses: CardView? = null
    var button_about: CardView? = null
    var button_logout: CardView? = null
    var text_greeting: TextView? = null
    var text_remaining_budget: TextView? = null
    var remainingBudget: Double? = 0.0
    private lateinit var databaseInstance: DatabaseInstance
    private lateinit var databaseDao: DatabaseDao


    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        window.statusBarColor = ContextCompat.getColor(this, R.color.button_color)

        button_budget = findViewById(R.id.button_budget)
        button_expenses = findViewById(R.id.button_expenses)
        button_about = findViewById(R.id.button_about)
        button_logout = findViewById(R.id.button_logout)
        text_greeting = findViewById(R.id.text_greeting)
        text_remaining_budget = findViewById(R.id.text_remaining_budget)
        databaseInstance = DatabaseInstance.getDatabase(this)
        databaseDao = databaseInstance.databaseDao()

        text_greeting?.text = "Hi, " + HelperClass.userModel?.name + "!"
        button_budget?.setOnClickListener(View.OnClickListener {
            startActivity(
                Intent(
                    this@MainActivity,
                    BudgetActivity::class.java
                )
            )
        })

        button_expenses?.setOnClickListener(View.OnClickListener {
            startActivity(
                Intent(
                    this@MainActivity,
                    ExpenseActivity::class.java
                )
            )
        })

        button_about?.setOnClickListener(View.OnClickListener {
            startActivity(
                Intent(
                    this@MainActivity,
                    AboutActivity::class.java
                )
            )
        })

        button_logout?.setOnClickListener{
            startActivity(Intent(this@MainActivity, LoginActivity::class.java))
            finish()
        }

    }

    @SuppressLint("SetTextI18n")
    override fun onResume() {
        super.onResume()

        GlobalScope.launch(Dispatchers.IO) {
            remainingBudget =
                databaseDao.getTotalBudget(HelperClass.userModel?.id!!.toLong()) - databaseDao.getTotalExpenses(
                    HelperClass.userModel?.id!!.toLong()
                )
            runOnUiThread {
                text_remaining_budget!!.text = "My current budget: $remainingBudget €"
            }
        }
    }

}