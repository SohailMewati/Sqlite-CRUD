package com.sohel.mynewsqlite

import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.sohel.mynewsqlite.adapter.MyListAdapter
import com.sohel.mynewsqlite.model.CustomerModel

class MainActivity : AppCompatActivity() {

    private lateinit var u_id: EditText
    private lateinit var u_name: EditText
    private lateinit var u_email: EditText
    private lateinit var listView: ListView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        u_id = findViewById(R.id.u_id)
        u_name = findViewById(R.id.u_name)
        u_email = findViewById(R.id.u_email)
        listView = findViewById(R.id.listView)

        val btnSave: Button = findViewById(R.id.btnSave)
        val btnViewRecord: Button = findViewById(R.id.btnViewRecord)
        val btnUpdateRecord: Button = findViewById(R.id.btnUpdate)
        val btnDelete: Button = findViewById(R.id.btnDelete)


        btnSave.setOnClickListener {
            saveRecord()
        }

        btnViewRecord.setOnClickListener {
            viewRecord()
        }

        btnUpdateRecord.setOnClickListener {
            updateRecord()
        }

        btnDelete.setOnClickListener {
           deleteRecord()
        }

    }

    // method for saving record in database
    private fun saveRecord() {

        val id = u_id.text.toString()
        val name = u_name.text.toString()
        val email = u_email.text.toString()

        val databaseHandler = DatabaseHandler(this)

        if (id.trim()!= "" && name.trim()!= "" && email.trim()!= "") {

            val status: Long =
                databaseHandler.addCustomer(CustomerModel(Integer.parseInt(id), name, email))

            if (status > -1) {
                Toast.makeText(this, "Record Added Successfully", Toast.LENGTH_SHORT).show()
                u_id.text.clear()
                u_name.text.clear()
                u_email.text.clear()
            } else {
                Toast.makeText(this, "Error Saving Record", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
        }
    }


    // method for read records from database in listview
    private fun viewRecord() {
        val databaseHandlers = DatabaseHandler(this)

        val cus: List<CustomerModel> = databaseHandlers.viewCustomer()

        val empArrayId = Array<String>(cus.size) { "0" }
        val empArrayName = Array<String>(cus.size) { "null" }
        val empArrayEmail = Array<String>(cus.size) { "null" }
        var index = 0
        for (e in cus) {
            empArrayId[index] = e.userId.toString()
            empArrayName[index] = e.userName
            empArrayEmail[index] = e.userEmail
            index++

        }

        // Creating Custom adapter

        val myListAdapter = MyListAdapter(this, empArrayId, empArrayName, empArrayEmail)
        listView.adapter = myListAdapter

    }


    // method for updating records based on userId
    private fun updateRecord() {
        val dialogBuilder = AlertDialog.Builder(this)

        val inflater = this.layoutInflater

        val dialogView = inflater.inflate(R.layout.update_dialog, null)

        dialogBuilder.setView(dialogView)

        val etId = dialogView.findViewById(R.id.updateId) as EditText
        val etName = dialogView.findViewById(R.id.updateName) as EditText
        val etEmail = dialogView.findViewById(R.id.updateEmail) as EditText

        dialogBuilder.setTitle("Update Record")
        dialogBuilder.setMessage("Enter data below")

        dialogBuilder.setPositiveButton("Update", DialogInterface.OnClickListener { _, _ ->

            val updateId = etId.text.toString()
            val updateName = etName.text.toString()
            val updateEmail = etEmail.text.toString()

            // create the instance of DatabaseHandler class

            val databaseHandler = DatabaseHandler(this)

            if (updateId.trim()!= "" && updateName.trim()!= "" && updateEmail.trim()!= "") {

                // calling the updateCustomer method of DatabaseHandler class to update record

                val status: Int = databaseHandler.updateCustomer(CustomerModel(Integer.parseInt(updateId),updateName, updateEmail))


                if (status > -1) {

                    Toast.makeText(this, "Record update successfully", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "id or name or email cannot be blank", Toast.LENGTH_SHORT)
                    .show()
            }

        })

        dialogBuilder.setNegativeButton("Cancel", DialogInterface.OnClickListener { dialog, which ->
            // Pass
        })

        val showDialog = dialogBuilder.create()
        showDialog.show()
    }

    // method for deleting records based on userId
    private fun deleteRecord(){

        val dialogBuilder = AlertDialog.Builder(this)

        val inflater = this.layoutInflater

        val dialogView = inflater.inflate(R.layout.delete_dialog, null)

        dialogBuilder.setView(dialogView)

        val deltId = dialogView.findViewById(R.id.deleteId) as EditText

        dialogBuilder.setTitle("Delete Record")
        dialogBuilder.setMessage("Enter data below")

        dialogBuilder.setPositiveButton("Delete", DialogInterface.OnClickListener { _, _ ->
            val deletedId = deltId.text.toString()

            val databaseHandler: DatabaseHandler = DatabaseHandler(this)

            if (deletedId.trim()!= "") {

                // calling the delete method of DatabaseHandler class to delete record

                val status =
                    databaseHandler.deleteRecord(CustomerModel(Integer.parseInt(deletedId), "", ""))

                if (status > -1) {

                    Toast.makeText(this, "Recorded deleted", Toast.LENGTH_SHORT).show()

                }
            } else{
                Toast.makeText(this, "Id or name or email cannot be blank", Toast.LENGTH_SHORT).show()

            }
        })

        dialogBuilder.setNegativeButton("Cancel", DialogInterface.OnClickListener { dialog, which ->
            // Pass
        })

        val showDialog = dialogBuilder.create()

        showDialog.show()


    }

}