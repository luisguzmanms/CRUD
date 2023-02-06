package com.lamesa.crudconexusit.usecase

import androidx.lifecycle.LiveData
import com.lamesa.crudconexusit.data.db.CustomerDAO
import com.lamesa.crudconexusit.model.Customer
import com.lamesa.crudconexusit.repository.CustomerRepository

class GetCustomersUseCase(private val customerRepository: CustomerRepository)  {
    operator fun invoke(): LiveData<List<Customer>> { return customerRepository.getCustomers()}
}