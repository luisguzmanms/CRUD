package com.lamesa.crudconexusit.view

import android.annotation.SuppressLint
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView
import com.lamesa.crudconexusit.R
import com.lamesa.crudconexusit.model.Customer
import com.lamesa.crudconexusit.util.Util.dialogDeleteCustomer
import com.lamesa.crudconexusit.util.Util.dialogUpdateCustomer

class CustomerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val cardCustomer = itemView.findViewById<MaterialCardView>(R.id.card_customer)
    private val identification = itemView.findViewById<TextView>(R.id.identification)
    private val fullName = itemView.findViewById<TextView>(R.id.fullname)
    private val email = itemView.findViewById<TextView>(R.id.email)
    private val registerDate = itemView.findViewById<TextView>(R.id.registerDate)
    private val editIcon = itemView.findViewById<ImageView>(R.id.edit_icon)
    private val deleteIcon = itemView.findViewById<ImageView>(R.id.delete_icon)

    @SuppressLint("SetTextI18n")
    fun bind(customer: Customer) {

        identification.text = "ID: ${customer.identification}"
        fullName.text = "${customer.firstName} ${customer.lastName}"
        email.text = customer.email
        registerDate.text = customer.registerDate.toString()

        cardCustomer.setOnClickListener {
            dialogUpdateCustomer(customer, itemView)
        }
        cardCustomer.setOnLongClickListener{
            dialogDeleteCustomer(customer, itemView)
            true
        }
        editIcon.setOnClickListener {
            dialogUpdateCustomer(customer, itemView)
        }
        deleteIcon.setOnClickListener {
            dialogDeleteCustomer(customer, itemView)
        }
    }
}
