package com.example.expensetracker.activities.expense

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import com.example.expensetracker.R
import com.example.expensetracker.database.DatabaseDao
import com.example.expensetracker.database.DatabaseInstance
import com.example.expensetracker.model.BudgetModel
import com.example.expensetracker.model.ExpenseModel
import com.example.expensetracker.model.HelperClass
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class AddExpenseActivity : AppCompatActivity() {

    var image_back: ImageView? = null
    var textview_budget: TextView? = null
    var edittext_expense_name: EditText? = null
    var edittext_expense_amount:EditText? = null
    var cvDelete: CardView? = null
    var tvLabel:TextView? = null
    var button_save: Button? = null
    var button_delete: Button? = null
    var expenseModel: ExpenseModel? = null
    var name: String? = null
    var amount:kotlin.String? = null
    var remainingBudget = 0.0
    var getTotalBudget: Double = 0.0
    var getTotalExpense: Double = 0.0
    private lateinit var databaseInstance: DatabaseInstance
    private lateinit var databaseDao: DatabaseDao

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_expense)
        window.statusBarColor = ContextCompat.getColor(this, R.color.button_color)

        image_back = findViewById(R.id.image_back)
        edittext_expense_name = findViewById(R.id.edittext_expense_name)
        edittext_expense_amount = findViewById(R.id.edittext_expense_amount)
        tvLabel = findViewById(R.id.tvLabel)
        cvDelete = findViewById(R.id.cvDelete)
        button_save = findViewById(R.id.button_save)
        button_delete = findViewById(R.id.button_delete)
        textview_budget = findViewById(R.id.textview_budget)
        databaseInstance = DatabaseInstance.getDatabase(this)
        databaseDao = databaseInstance.databaseDao()


        if (intent.extras != null){
            expenseModel = intent.getSerializableExtra("data") as ExpenseModel?
            tvLabel?.text = "Update Expense"
            button_save?.text = "Update"
            edittext_expense_name?.setText(expenseModel?.name)
            edittext_expense_amount?.setText(expenseModel?.amount.toString())
            cvDelete?.visibility = View.VISIBLE
            button_delete?.setOnClickListener{
                GlobalScope.launch(Dispatchers.IO) {
                    databaseDao.deleteExpense(expenseModel!!)
                    runOnUiThread {
                        showToast("Expense has been deleted.")
                        finish()
                    }
                }
            }
        }

        GlobalScope.launch(Dispatchers.IO) {
            getTotalBudget = databaseDao.getTotalBudget(HelperClass.userModel?.id?.toLong()!!)
            getTotalExpense = databaseDao.getTotalExpenses(HelperClass.userModel?.id?.toLong()!!)
            remainingBudget = getTotalBudget-getTotalExpense
            runOnUiThread{
                if (expenseModel != null){
                    val updatedBudget =   remainingBudget+expenseModel?.amount!!
                    textview_budget?.text = "My budget: $updatedBudget €"
                }else{
                    textview_budget?.text = "My budget: $remainingBudget €"
                }
            }
        }

        image_back?.setOnClickListener(View.OnClickListener {
            onBackPressed()
            finish()
        })

        button_save?.setOnClickListener{
            name = edittext_expense_name?.text.toString()
            amount = edittext_expense_amount?.text.toString()
            if (name!!.isEmpty()) {
                showToast("Expense name cannot be empty!")
            } else if (amount!!.isEmpty()) {
                showToast("Expense amount cannot be empty!")
            } else {
                if (remainingBudget > 0) {

                    if (expenseModel != null){
                        if (amount!!.toDouble() <= remainingBudget+expenseModel?.amount!!) {
                            expenseModel?.name = name!!
                            expenseModel?.amount = amount!!.toLong()
                            GlobalScope.launch(Dispatchers.IO) {
                                databaseDao.updateExpense(expenseModel!!)
                                runOnUiThread {
                                    showToast("Expense has been updated!")
                                    finish()
                                }
                            }
                        } else {
                            showToast("Expense should not be greater than remaining budget!")
                        }

                    }else{
                        if (amount!!.toDouble() <= remainingBudget) {

                            val amountModel = ExpenseModel(
                                name = name!!,
                                amount = amount!!.toLong(),
                                userId = HelperClass.userModel?.id?.toLong()!!
                            )
                            GlobalScope.launch(Dispatchers.IO) {
                                databaseDao.addExpense(amountModel)
                                runOnUiThread {
                                    showToast("You've added a new expense!")
                                    finish()
                                }
                            }
                        } else {
                            showToast("Expense should not be greater than remaining budget!")
                        }
                    }
                } else {
                    showToast("You must enter budget prior to expense!")
                }
            }
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}