package com.example.employeetasks

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
// import android.widget.Spinner
import android.widget.Toast

class Login : AppCompatActivity() {

    private lateinit var loginUsername: EditText
    private lateinit var loginPassword: EditText
    private lateinit var loginBtn: Button
    private lateinit var db: DBHelper
    // private lateinit var spinner: Spinner

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        loginUsername = findViewById(R.id.loginUsername)
        loginPassword = findViewById(R.id.loginPassword)
        loginBtn = findViewById(R.id.loginBtn)
        db = DBHelper(this)
        // spinner = findViewById(R.id.loginSpinner)

        /*val arrayAdapter: ArrayAdapter<Any?> = ArrayAdapter<Any?>(this, R.layout.spinner_item)
        arrayAdapter.setDropDownViewResource(R.layout.spinner_item)
        spinner.adapter = arrayAdapter*/

        loginBtn.setOnClickListener {
            val loginUsernameText = loginUsername.text.toString()
            val loginPasswordText = loginPassword.text.toString()

            if (TextUtils.isEmpty(loginUsernameText) || TextUtils.isEmpty(loginPasswordText)) {
                Toast.makeText(this, "Введите Имя пользователя и Пароль", Toast.LENGTH_SHORT).show()
            }
            else {
                val checkUser = db.checkUserPassword(loginUsernameText, loginPasswordText)
                if (checkUser == true) {
                    if(loginUsernameText == "admin" && loginPasswordText == "admin") {
                        Toast.makeText(this, "Вход прошел успешно", Toast.LENGTH_SHORT).show()
                        val intent = Intent(applicationContext, Admin::class.java)
                        startActivity(intent)
                    }
                    else if (loginUsernameText == "user" && loginPasswordText == "user") {
                        Toast.makeText(this, "Вход прошел успешно", Toast.LENGTH_SHORT).show()
                        val intent = Intent(applicationContext, User::class.java)
                        startActivity(intent)
                    }
                }
                else {
                    Toast.makeText(this, "Неправильное Имя пользователя и Пароль", Toast.LENGTH_SHORT).show()
                }
            }

        }
    }
}