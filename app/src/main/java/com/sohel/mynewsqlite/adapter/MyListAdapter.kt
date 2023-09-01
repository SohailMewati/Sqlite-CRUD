package com.sohel.mynewsqlite.adapter

import android.annotation.SuppressLint
import android.app.Activity
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.sohel.mynewsqlite.R

class MyListAdapter(
    private val context: Activity,
    private val id: Array<String>,
    private val name: Array<String>,
    private val email: Array<String>
) :
    ArrayAdapter<String>(context, R.layout.custom_list, name) {


    @SuppressLint("SetTextI18n", "ViewHolder")
    override fun getView(position: Int, view: View?, parent: ViewGroup): View {

        val inflater = context.layoutInflater
        val rowView = inflater.inflate(R.layout.custom_list, null,true)

        val txtId: TextView = rowView.findViewById(R.id.textViewId)
        val txtName: TextView = rowView.findViewById(R.id.textViewName)
        val txtEmail: TextView = rowView.findViewById(R.id.textViewEmail)


        txtId.text = "Id: ${id[position]}"
        txtName.text = "Name: ${name[position]}"
        txtEmail.text = "Email: ${email[position]}"

        return rowView

    }
}