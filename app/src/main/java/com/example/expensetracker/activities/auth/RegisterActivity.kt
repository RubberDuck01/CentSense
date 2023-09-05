package com.example.expensetracker.activities.auth

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.expensetracker.R
import com.example.expensetracker.database.DatabaseDao
import com.example.expensetracker.database.DatabaseInstance
import com.example.expensetracker.model.UserModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class RegisterActivity : AppCompatActivity() {


    var edittext_username: EditText? = null
    var edittext_password:EditText? = null
    var button_login: Button? = null
    var button_register: Button? = null
    var userName: String? = ""
    var password: String? = ""
    private lateinit var databaseInstance: DatabaseInstance
    private lateinit var databaseDao: DatabaseDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        window.statusBarColor = ContextCompat.getColor(this, R.color.button_color)

        edittext_username = findViewById(R.id.edittext_username)
        edittext_password = findViewById(R.id.edittext_password)
        button_login = findViewById(R.id.button_login)
        button_register = findViewById(R.id.button_register)
        databaseInstance = DatabaseInstance.getDatabase(this)
        databaseDao = databaseInstance.databaseDao()

        button_register?.setOnClickListener{
            userName = edittext_username?.text.toString()
            password = edittext_password?.text.toString()
            if (userName!!.isEmpty()) {
                showToast("Username is required!")
            } else if (password!!.isEmpty()) {
                showToast("Password is required!")
            } else {
                val userModel = UserModel(
                    name = userName!!,
                    password = password!!)
                GlobalScope.launch(Dispatchers.IO) {
                    databaseDao.registerUser(userModel)
                }
                showToast("You're now registered. Proceed to login.")
                finish()
            }
        }

        button_login?.setOnClickListener{
            onBackPressed()
            finish()
        }

    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

}