package com.lamesa.crudconexusit.data.db

import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.lamesa.crudconexusit.app.App
import com.lamesa.crudconexusit.data.db.DatabaseConnection.checkConnection
import com.lamesa.crudconexusit.model.Customer
import com.lamesa.crudconexusit.util.Util
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CustomerDAO() {

    fun getCustomers(): LiveData<List<Customer>> {
        val customers = mutableListOf<Customer>()
        if (Util.isOnline(App.instance)) {
            val connection = DatabaseConnection.getConnection()
            val statement = connection!!.createStatement()
            val resultSet =
                statement.executeQuery("SELECT * FROM customers ORDER BY RegisterDate DESC")

            while (resultSet.next()) {
                val customerID = resultSet.getInt("CustomerID")
                val identification = resultSet.getString("Identification")
                val firstName = resultSet.getString("FirstName")
                val lastName = resultSet.getString("LastName")
                val email = resultSet.getString("Email")
                val registerDate = resultSet.getTimestamp("RegisterDate").toString()

                val customer =
                    Customer(customerID, identification, firstName, lastName, email, registerDate)
                customers.add(customer)
            }
            connection.close()
        }
        return MutableLiveData(customers)
    }

    fun addCustomer(customer: Customer) {
        if (Util.isOnline(App.instance)) {
            if (checkConnection()) {
                val connection = DatabaseConnection.getConnection()
                val statement =
                    connection!!.prepareStatement("INSERT INTO customers (Identification, FirstName, LastName, Email, RegisterDate) VALUES (?, ?, ?, ?, ?)")
                statement.setString(1, customer.identification)
                statement.setString(2, customer.firstName)
                statement.setString(3, customer.lastName)
                statement.setString(4, customer.email)
                statement.setString(5, customer.registerDate)
                statement.executeUpdate()
                connection.close()

                CoroutineScope(Dispatchers.Main).launch {
                    Toast.makeText(
                        App.instance,
                        "Se agrega cliente ${customer.email}",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }

    fun updateCustomer(customer: Customer) {
        if (Util.isOnline(App.instance)) {
            if (checkConnection()) {
                val connection = DatabaseConnection.getConnection()
                val statement =
                    connection!!.prepareStatement("UPDATE customers SET Identification = ?, FirstName = ?, LastName = ?, Email = ?, RegisterDate = ? WHERE CustomerID = ?")
                statement.setString(1, customer.identification)
                statement.setString(2, customer.firstName)
                statement.setString(3, customer.lastName)
                statement.setString(4, customer.email)
                statement.setString(5, customer.registerDate)
                statement.setInt(6, customer.customerID!!)
                statement.executeUpdate()
                connection.close()
                CoroutineScope(Dispatchers.Main).launch {
                    Toast.makeText(
                        App.instance,
                        "Se actualiza el cliente ${customer.email}",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }

    fun deleteCustomer(customer: Customer) {
        if (Util.isOnline(App.instance)) {
            val connection = DatabaseConnection.getConnection()
            val statement =
                connection!!.prepareStatement("DELETE FROM customers WHERE CustomerID = ?")
            statement.setInt(1, customer.customerID!!)
            statement.executeUpdate()
            connection.close()
            CoroutineScope(Dispatchers.Main).launch {
                Toast.makeText(
                    App.instance,
                    "Cliente ID: ${customer.identification} eliminado.",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }
}
