package com.example.employeetasks

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_user.*

class User : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user)

        printName.setOnClickListener {
            val db = DatabaseHandler(this, null)

            val cursor = db.getName()

            cursor!!.moveToFirst()
            enterName.append(cursor.getString(cursor.getColumnIndex(DatabaseHandler.KEY_NAME)) + "\n")
            enterStatus.append(cursor.getString(cursor.getColumnIndex(DatabaseHandler.KEY_STATUS)) + "\n")

            while (cursor.moveToNext()) {
                enterName.append(cursor.getString(cursor.getColumnIndex(DatabaseHandler.KEY_NAME)) + "\n")
                enterStatus.append(cursor.getString(cursor.getColumnIndex(DatabaseHandler.KEY_STATUS)) + "\n")

                cursor.close()
            }
        }
    }
}