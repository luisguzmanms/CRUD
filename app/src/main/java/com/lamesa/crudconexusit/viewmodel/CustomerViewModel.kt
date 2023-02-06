package com.lamesa.crudconexusit.viewmodel

import android.view.View
import android.widget.ProgressBar
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.lamesa.crudconexusit.data.db.CustomerDAO
import com.lamesa.crudconexusit.model.Customer
import com.lamesa.crudconexusit.repository.CustomerRepository
import com.lamesa.crudconexusit.usecase.AddCustomerUseCase
import com.lamesa.crudconexusit.usecase.DeleteCustomerUseCase
import com.lamesa.crudconexusit.usecase.GetCustomersUseCase
import com.lamesa.crudconexusit.usecase.UpdateCustomerUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CustomerViewModel : ViewModel() {
    private val customerRepository = CustomerRepository(CustomerDAO())
    internal var customers =
        GetCustomersUseCase(customerRepository).invoke() as MutableLiveData<List<Customer>>
    private val updateCustomerUseCase = UpdateCustomerUseCase(customerRepository)
    private val getCustomersUseCase = GetCustomersUseCase(customerRepository)
    private val addCustomerUseCase = AddCustomerUseCase(customerRepository)
    private val deleteCustomerUseCase = DeleteCustomerUseCase(customerRepository)
    lateinit var loadingData: ProgressBar

    suspend fun getCustomers() {
        return withContext(Dispatchers.IO) {
            customers = getCustomersUseCase.invoke() as MutableLiveData<List<Customer>>
        }
    }

    fun addCustomer(customer: Customer) {
        loadingData.visibility = View.VISIBLE
        CoroutineScope(Dispatchers.IO).launch {
            addCustomerUseCase(customer)
            refreshCustomers()
        }
    }

    fun updateCustomer(customer: Customer) {
        loadingData.visibility = View.VISIBLE
        CoroutineScope(Dispatchers.IO).launch {
            updateCustomerUseCase.invoke(customer)
            refreshCustomers()
        }
    }

    fun deleteCustomer(customer: Customer) {
        loadingData.visibility = View.VISIBLE
        CoroutineScope(Dispatchers.IO).launch {
            CoroutineScope(Dispatchers.IO).launch {
                deleteCustomerUseCase(customer)
                refreshCustomers()
            }
        }
    }

    private fun refreshCustomers() {
        loadingData.visibility = View.VISIBLE
        CoroutineScope(Dispatchers.IO).launch {
            val updatedCustomers: LiveData<List<Customer>> = getCustomersUseCase.invoke()
            withContext(Dispatchers.Main) {
                customers.postValue(updatedCustomers.value)
            }
        }
    }

}