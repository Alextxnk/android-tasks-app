package com.example.employeetasks

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import android.widget.Toast

class Signup : AppCompatActivity() {

    private lateinit var username: EditText
    private lateinit var password: EditText
    private lateinit var confirmPassword: EditText
    private lateinit var signupBtn: Button
    private lateinit var db: DBHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        username = findViewById(R.id.username)
        password = findViewById(R.id.password)
        confirmPassword = findViewById(R.id.confirmPassword)
        signupBtn = findViewById(R.id.signupBtn)
        db = DBHelper(this)

        signupBtn.setOnClickListener {
            val usernameText = username.text.toString()
            val passwordText = password.text.toString()
            val confirmPasswordText = confirmPassword.text.toString()
            val saveData = db.insertData(usernameText, passwordText)

            if (TextUtils.isEmpty(usernameText) || TextUtils.isEmpty(passwordText) || TextUtils.isEmpty(confirmPasswordText)) {
                Toast.makeText(this, "Введите Имя пользователя и Пароль", Toast.LENGTH_SHORT).show()
            }
            else {
                if (passwordText.equals(confirmPasswordText)) {
                    if (saveData == true) {
                        Toast.makeText(this, "Регистрация прошла успешно", Toast.LENGTH_SHORT).show()
                        val intent = Intent(applicationContext, Login::class.java)
                        startActivity(intent)
                    }
                    else {
                        Toast.makeText(this, "Пользователь '$usernameText' существует", Toast.LENGTH_SHORT).show()
                    }
                }
                else {
                    Toast.makeText(this, "Пароли не совпадают", Toast.LENGTH_SHORT).show()
                }
            }

        }
    }
}