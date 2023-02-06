package com.lamesa.crudconexusit.model

import java.sql.Timestamp
import java.time.LocalDateTime

data class Customer(
    val customerID: Int?,
    val identification: String,
    val firstName: String,
    val lastName: String,
    val email: String,
    val registerDate: String
)