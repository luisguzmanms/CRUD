package com.lamesa.crudconexusit.repository

import androidx.lifecycle.LiveData
import com.lamesa.crudconexusit.data.db.CustomerDAO
import com.lamesa.crudconexusit.model.Customer

class CustomerRepository(private val customerDAO: CustomerDAO) {

    internal fun getCustomers(): LiveData<List<Customer>> {
        return customerDAO.getCustomers()
    }

    fun addCustomer(customer: Customer) {
        customerDAO.addCustomer(customer)
    }

    fun updateCustomer(customer: Customer) {
        customerDAO.updateCustomer(customer)
    }

    fun deleteCustomer(customer: Customer) {
        customerDAO.deleteCustomer(customer)
    }

}
