package com.example.employeetasks

import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.dialog_update.*

class MainActivity : AppCompatActivity() {

    private lateinit var btnAdd: Button
    private lateinit var rvItemsList: RecyclerView
    private lateinit var tvNoRecordsAvailable: TextView
    private lateinit var etName: EditText
    private lateinit var etStatus: EditText

    /*private lateinit var etUpdateName: EditText
    private lateinit var etUpdateStatus: EditText
    private lateinit var tvUpdate: TextView
    private lateinit var tvCancel: TextView*/

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        // setSupportActionBar(toolbar)
        btnAdd = findViewById(R.id.btnAdd)
        rvItemsList = findViewById(R.id.rvItemsList)
        tvNoRecordsAvailable = findViewById(R.id.tvNoRecordsAvailable)
        etName = findViewById(R.id.etName)
        etStatus = findViewById(R.id.etStatus)

        btnAdd.setOnClickListener { view ->
            addRecord(view)
        }

        setupListOfDataIntoRecyclerView()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.exit -> {
                val intent = Intent(applicationContext, Login::class.java)
                startActivity(intent)
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setupListOfDataIntoRecyclerView() {

        if (getItemsList().size > 0) {

            rvItemsList.visibility = View.VISIBLE
            tvNoRecordsAvailable.visibility = View.GONE

            // Set the LayoutManager that this RecyclerView will use.
            rvItemsList.layoutManager = LinearLayoutManager(this)
            // Adapter class is initialized and list is passed in the param.
            val itemAdapter = ItemAdapter(this, getItemsList())
            // adapter instance is set to the recyclerview to inflate the items.
            rvItemsList.adapter = itemAdapter
        } else {

            rvItemsList.visibility = View.GONE
            tvNoRecordsAvailable.visibility = View.VISIBLE
        }
    }

    // Function is used to get the Items List from the database table.
    private fun getItemsList(): ArrayList<EmpModelClass> {
        //creating the instance of DatabaseHandler class
        val databaseHandler: DatabaseHandler = DatabaseHandler(this, null)
        //calling the viewEmployee method of DatabaseHandler class to read the records
        val empList: ArrayList<EmpModelClass> = databaseHandler.viewEmployee()

        return empList
    }

    //Method for saving the employee records in database
    private fun addRecord(view: View) {
        val name = etName.text.toString()
        val status = etStatus.text.toString()
        val databaseHandler: DatabaseHandler = DatabaseHandler(this, null)
        if (!name.isEmpty() && !status.isEmpty()) {
            val status =
                databaseHandler.addEmployee(EmpModelClass(0, name, status))
            if (status > -1) {
                Toast.makeText(applicationContext, "Запись добавлена", Toast.LENGTH_LONG).show()
                etName.text.clear()
                etStatus.text.clear()

                setupListOfDataIntoRecyclerView()
            }
        } else {
            Toast.makeText(
                applicationContext,
                "Задача или Статус не могут быть пустыми",
                Toast.LENGTH_LONG
            ).show()
        }
    }

    // Method is used to show the Custom Dialog.
    fun updateRecordDialog(empModelClass: EmpModelClass) {
        val updateDialog = Dialog(this, R.style.Theme_Dialog)
        updateDialog.setCancelable(false)
        /*Set the screen content from a layout resource.
         The resource will be inflated, adding all top-level views to the screen.*/
        updateDialog.setContentView(R.layout.dialog_update)

        updateDialog.etUpdateName.setText(empModelClass.name)
        updateDialog.etUpdateStatus.setText(empModelClass.status)

        updateDialog.tvUpdate.setOnClickListener(View.OnClickListener {

            val name = updateDialog.etUpdateName.text.toString()
            val status = updateDialog.etUpdateStatus.text.toString()

            val databaseHandler: DatabaseHandler = DatabaseHandler(this, null)

            if (!name.isEmpty() && !status.isEmpty()) {
                val status =
                    databaseHandler.updateEmployee(EmpModelClass(empModelClass.id, name, status))
                if (status > -1) {
                    Toast.makeText(applicationContext, "Запись обновлена", Toast.LENGTH_LONG).show()

                    setupListOfDataIntoRecyclerView()

                    updateDialog.dismiss() // Dialog will be dismissed
                }
            } else {
                Toast.makeText(
                    applicationContext,
                    "Задача или Статус не могут быть пустыми",
                    Toast.LENGTH_LONG
                ).show()
            }
        })
        updateDialog.tvCancel.setOnClickListener(View.OnClickListener {
            updateDialog.dismiss()
        })
        //Start the dialog and display it on screen.
        updateDialog.show()
    }

    // Method is used to show the Alert Dialog.
    fun deleteRecordAlertDialog(empModelClass: EmpModelClass) {
        val builder = AlertDialog.Builder(this)
        //set title for alert dialog
        builder.setTitle("Удалить запись")
        //set message for alert dialog
        builder.setMessage("Вы уверены, что хотите удалить ${empModelClass.name}?")
        // builder.setIcon(android.R.drawable.ic_dialog_alert)

        //performing positive action
        builder.setPositiveButton("Да") { dialogInterface, which ->

            //creating the instance of DatabaseHandler class
            val databaseHandler: DatabaseHandler = DatabaseHandler(this, null)
            //calling the deleteEmployee method of DatabaseHandler class to delete record
            val status = databaseHandler.deleteEmployee(EmpModelClass(empModelClass.id, "", ""))
            if (status > -1) {
                Toast.makeText(
                    applicationContext,
                    "Запись удалена успешно",
                    Toast.LENGTH_LONG
                ).show()

                setupListOfDataIntoRecyclerView()
            }

            dialogInterface.dismiss() // Dialog will be dismissed
        }
        //performing negative action
        builder.setNegativeButton("Нет") { dialogInterface, which ->
            dialogInterface.dismiss() // Dialog will be dismissed
        }
        // Create the AlertDialog
        val alertDialog: AlertDialog = builder.create()
        // Set other dialog properties
        alertDialog.setCancelable(false) // Will not allow user to cancel after clicking on remaining screen area.
        alertDialog.show()  // show the dialog to UI
    }
}