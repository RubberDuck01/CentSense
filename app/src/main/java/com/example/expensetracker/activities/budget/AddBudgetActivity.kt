package com.example.expensetracker.activities.budget

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
import com.example.expensetracker.model.HelperClass
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class AddBudgetActivity : AppCompatActivity() {

    var image_back: ImageView? = null
    var edittext_budget_name: EditText? = null
    var edittext_budget_amount:EditText? = null
    var cvDelete:CardView? = null
    var tvLabel:TextView? = null
    var button_save: Button? = null
    var button_delete: Button? = null
    var budgetModel: BudgetModel? = null
    var name: String? = ""
    var amount:String? = ""
    private lateinit var databaseInstance: DatabaseInstance
    private lateinit var databaseDao: DatabaseDao

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_budget)
        window.statusBarColor = ContextCompat.getColor(this, R.color.button_color)

        image_back = findViewById(R.id.image_back)
        edittext_budget_name = findViewById(R.id.edittext_budget_name)
        edittext_budget_amount = findViewById(R.id.edittext_budget_amount)
        tvLabel = findViewById(R.id.tvLabel)
        cvDelete = findViewById(R.id.cvDelete)
        button_save = findViewById(R.id.button_save)
        button_delete = findViewById(R.id.button_delete)
        databaseInstance = DatabaseInstance.getDatabase(this)
        databaseDao = databaseInstance.databaseDao()

        if (intent.extras != null){
            budgetModel = intent.getSerializableExtra("data") as BudgetModel?
            tvLabel?.text = "Update Budget"
            button_save?.text = "Update"
            edittext_budget_name?.setText(budgetModel?.name)
            edittext_budget_amount?.setText(budgetModel?.amount.toString())
            cvDelete?.visibility = View.VISIBLE
            button_delete?.setOnClickListener{
                GlobalScope.launch(Dispatchers.IO) {
                    databaseDao.deleteBudget(budgetModel!!)
                    runOnUiThread {
                        showToast("Deleted Successfully")
                        finish()
                    }
                }
            }
        }

        image_back?.setOnClickListener{
            onBackPressed()
            finish()
        }

        button_save?.setOnClickListener{
            name = edittext_budget_name?.text.toString()
            amount = edittext_budget_amount?.text.toString()
            if (name!!.isEmpty()) {
                showToast("Please enter budget name")
            } else if (amount!!.isEmpty()) {
                showToast("Please enter budget amount")
            } else {
                if (amount!!.toLong() > 0) {

                    if (budgetModel != null){
                        budgetModel?.name = name!!
                        budgetModel?.amount = amount!!.toLong()
                        GlobalScope.launch(Dispatchers.IO) {
                            databaseDao.updateBudget(budgetModel!!)
                        }
                        showToast("Successfully Updated")
                    }else{
                        val amountModel = BudgetModel(
                            name = name!!,
                            amount = amount!!.toLong(),
                            userId = HelperClass.userModel?.id?.toLong()!!
                        )
                        GlobalScope.launch(Dispatchers.IO) {
                            databaseDao.addBudget(amountModel)
                        }
                        showToast("Successfully Added")
                    }
                    finish()
                } else {
                    showToast("Amount should be greater than 0")
                }
            }
        }

    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

}