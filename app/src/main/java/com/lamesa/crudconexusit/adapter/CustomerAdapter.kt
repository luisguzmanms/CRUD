package com.lamesa.crudconexusit.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.AlphaAnimation
import androidx.recyclerview.widget.RecyclerView
import com.lamesa.crudconexusit.R
import com.lamesa.crudconexusit.model.Customer
import com.lamesa.crudconexusit.view.CustomerViewHolder

class CustomerAdapter(private val context: Context) : RecyclerView.Adapter<CustomerViewHolder>() {

    private var listCustomer = ArrayList<Customer>()
    private var lastPosition = -1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomerViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.customer_item, parent, false)
        return CustomerViewHolder(view)
    }

    override fun onBindViewHolder(holder: CustomerViewHolder, position: Int) {
        val customer = listCustomer[position]
        holder.bind(customer)
        setAnimation(holder, position)
    }

    override fun getItemCount(): Int {
        return listCustomer.size
    }

    internal fun updateList(customers: List<Customer>) {
        listCustomer.clear()
        listCustomer.addAll(customers)
    }

    private fun setAnimation(holder: CustomerViewHolder, position: Int) {
        if (position > lastPosition) {
            val animation = AlphaAnimation(0f, 1f)
            animation.duration = 500
            animation.startOffset = 100 * position.toLong()
            holder.itemView.startAnimation(animation)
            lastPosition = position
        }
    }

}