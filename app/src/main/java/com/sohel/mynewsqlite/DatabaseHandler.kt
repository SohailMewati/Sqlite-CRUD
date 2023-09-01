package com.sohel.mynewsqlite

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.SQLException
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.view.View
import com.sohel.mynewsqlite.model.CustomerModel

 class DatabaseHandler(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_VERSION = 1
        private const val DATABASE_NAME = "CustomerDatabase.db"

        private const val TABLE_CONTACTS = "CustomerTable"

        private const val KEY_ID = "id"
        private const val KEY_NAME = "name"
        private const val KEY_EMAIL = "email"

    }

    override fun onCreate(db: SQLiteDatabase?) {

        // Create table with field
        val createTable =
            ("CREATE TABLE $TABLE_CONTACTS ($KEY_ID INTEGER PRIMARY KEY, $KEY_NAME TEXT, $KEY_EMAIL TEXT)")
        db?.execSQL(createTable)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db!!.execSQL("DROP TABLE IF EXISTS $TABLE_CONTACTS")
        onCreate(db)
    }

    // Method to insert data
    fun addCustomer(cus: CustomerModel): Long {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(KEY_ID, cus.userId)
        contentValues.put(KEY_NAME, cus.userName) // CustomerModel name
        contentValues.put(KEY_EMAIL, cus.userEmail) // CustomerModel email

        // inserting row
        val success = db.insert(TABLE_CONTACTS, null, contentValues)

        // 2nd argument is string containing nullColumnHack
        db.close() // closing database connection

        return success
    }


    // Method to read data
    @SuppressLint("Recycle")
    fun viewCustomer(): List<CustomerModel> {

        val cusList: ArrayList<CustomerModel> = ArrayList()

        val selectQuery = "SELECT * FROM $TABLE_CONTACTS"

        val db = this.readableDatabase
        var cursor: Cursor? = null

        try {

            cursor = db.rawQuery(selectQuery, null)

        } catch (e: SQLException) {
            db.execSQL(selectQuery)
            return ArrayList()
        }

        var userId: Int
        var userName: String
        var userEmail: String

        if (cursor.moveToFirst()) {
            do {

                userId = cursor.getInt(cursor.getColumnIndex("id"))
                userName = cursor.getString(cursor.getColumnIndex("name"))
                userEmail = cursor.getString(cursor.getColumnIndex("email"))

                val cus = CustomerModel(userId = userId, userName = userName, userEmail = userEmail)
                cusList.add(cus)

            } while (cursor.moveToNext())
        }

        return cusList
    }

     // Method to update
     fun updateCustomer(cus: CustomerModel): Int{

         val db = this.writableDatabase

         val contentValues = ContentValues()

         contentValues.put(KEY_ID, cus.userId)
         contentValues.put(KEY_NAME, cus.userName)
         contentValues.put(KEY_EMAIL, cus.userEmail)

         // Updating row
         val success = db.update(TABLE_CONTACTS, contentValues, "id ="+cus.userId, null)
         db.close()

         return success
     }


     // Method to delete
     fun deleteRecord(cus: CustomerModel): Int{
         val db = this.writableDatabase

         val contentValues = ContentValues()
         contentValues.put(KEY_ID, cus.userId)

         val success = db.delete(TABLE_CONTACTS, "id="+ cus.userId, null)
         db.close()

         return success
     }

}