package com.example.expensetracker.activities.auth

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.expensetracker.MainActivity
import com.example.expensetracker.R
import com.example.expensetracker.database.DatabaseDao
import com.example.expensetracker.database.DatabaseInstance
import com.example.expensetracker.model.HelperClass
import com.example.expensetracker.model.UserModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {

    var edittext_username: EditText? = null
    var edittext_password: EditText? = null
    var button_login: Button? = null
    var button_register: Button? = null
    var userName: String? = null
    var password: String? = null
    var userModelList: List<UserModel> = ArrayList()
    private lateinit var databaseInstance: DatabaseInstance
    private lateinit var databaseDao: DatabaseDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        window.statusBarColor = ContextCompat.getColor(this, R.color.button_color)

        edittext_username = findViewById(R.id.edittext_username)
        edittext_password = findViewById(R.id.edittext_password)
        button_login = findViewById(R.id.button_login)
        button_register = findViewById(R.id.button_register)

        databaseInstance = DatabaseInstance.getDatabase(this)
        databaseDao = databaseInstance.databaseDao()
        button_register?.setOnClickListener{
            startActivity(
                Intent(
                    this@LoginActivity,
                    RegisterActivity::class.java
                )
            )
        }

        button_login?.setOnClickListener{
            userName = edittext_username?.text.toString()
            password = edittext_password?.text.toString()
            if (userName!!.isEmpty()) {
                showToast("Username and/or password fields cannot be empty!")
            } else if (password!!.isEmpty()) {
                showToast("Username and/or password fields cannot be empty!")
            } else {
                GlobalScope.launch(Dispatchers.IO) {
                    userModelList = databaseDao.getUsersFromDB()
                    for (users in userModelList) {
                        if (userName == users.name && password == users.password) {
                            runOnUiThread {
                            showToast("You are now logged in.")
                            HelperClass.userModel = users
                            startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                            finish()
                            }
                            return@launch
                        }
                    }
                    runOnUiThread {
                        showToast("Username and/or password are incorrect. Try again.")
                    }
                }
            }
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}