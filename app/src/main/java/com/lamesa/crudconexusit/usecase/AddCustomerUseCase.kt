package com.lamesa.crudconexusit.usecase

import com.lamesa.crudconexusit.model.Customer
import com.lamesa.crudconexusit.repository.CustomerRepository

class AddCustomerUseCase(private val customerRepository: CustomerRepository) {
    operator fun invoke(customer: Customer) = customerRepository.addCustomer(customer)
}