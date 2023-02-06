package com.lamesa.crudconexusit.util

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.service.autofill.Validators.or
import android.text.InputType
import android.util.Log
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.lamesa.crudconexusit.R
import com.lamesa.crudconexusit.app.App
import com.lamesa.crudconexusit.model.Customer
import com.lamesa.crudconexusit.view.MainActivity
import java.text.SimpleDateFormat
import java.util.*

object Util {
    private fun currentDate(): String {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        return dateFormat.format(Calendar.getInstance().time)
    }

    internal fun isOnline(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val capabilities =
                connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
            if (capabilities != null) {
                if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                    Log.i("Internet", "NetworkCapabilities.TRANSPORT_CELLULAR")
                    return true
                } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                    Log.i("Internet", "NetworkCapabilities.TRANSPORT_WIFI")
                    return true
                } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)) {
                    Log.i("Internet", "NetworkCapabilities.TRANSPORT_ETHERNET")
                    return true
                }
            }
        }
        Toast.makeText(App.instance, "Error: Sin conexión a internet.", Toast.LENGTH_LONG).show()
        return false
    }

    internal fun dialogDeleteCustomer(customer: Customer, itemView: View) {
        val builder = AlertDialog.Builder(itemView.context)
        builder.setTitle("Eliminar cliente")
        builder.setMessage("¿Estás seguro de que deseas eliminar a este cliente?")
        builder.setPositiveButton("Sí") { _, _ ->
            // Eliminar cliente en la base de datos.
            val mainActivity = itemView.context as MainActivity
            val customerViewModel = mainActivity.customerViewModel
            customerViewModel.deleteCustomer(customer)
            Toast.makeText(itemView.context,"Eliminando cliente...",Toast.LENGTH_LONG).show()
        }
        builder.setNegativeButton("No") { dialog, _ ->
            dialog.dismiss()
        }
        val alertDialog = builder.create()
        alertDialog.show()

    }

    internal fun dialogUpdateCustomer(customer: Customer, itemView: View) {
        val dialogBuilder = AlertDialog.Builder(itemView.context, R.style.Theme_CRUDconexusit)
        val inflater = LayoutInflater.from(itemView.context)
        val dialogView = inflater.inflate(R.layout.update_customer_dialog, null)
        dialogBuilder.setView(dialogView)

        //region Pasar datos del cleinte al formulario.
        val identificationEditText =
            dialogView.findViewById<EditText>(R.id.et_identification)
        identificationEditText.setText(customer.identification)
        identificationEditText.inputType = InputType.TYPE_CLASS_NUMBER

        val firstNameEditText = dialogView.findViewById<EditText>(R.id.et_first_name)
        firstNameEditText.setText(customer.firstName)
        firstNameEditText.inputType = InputType.TYPE_CLASS_TEXT
        val lastNameEditText = dialogView.findViewById<EditText>(R.id.et_last_name)
        lastNameEditText.setText(customer.lastName)
        lastNameEditText.inputType = InputType.TYPE_CLASS_TEXT

        val emailEditText = dialogView.findViewById<EditText>(R.id.et_email)
        emailEditText.setText(customer.email)
        emailEditText.inputType = InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS

        val registerDateEditText = dialogView.findViewById<EditText>(R.id.et_register_date)
        registerDateEditText.setText(customer.registerDate)
        //endregion

        dialogBuilder.setTitle("Actualizar datos del cliente ID ${customer.identification}")
        dialogBuilder.setMessage("Todos los campos son obligatorios.")
        dialogBuilder.setPositiveButton("Guardar") { _, _ -> }
        dialogBuilder.setNegativeButton("Cancelar") { dialog, _ -> dialog.dismiss() }
        val alertDialog = dialogBuilder.create()
        alertDialog.show()
        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener {
            val customerID = customer.customerID
            val identification = identificationEditText.text.toString()
            val firstName = firstNameEditText.text.toString()
            val lastName = lastNameEditText.text.toString()
            val email = emailEditText.text.toString()
            val registerDate = registerDateEditText.text.toString()

            if (!isEmailValid(email)) {
                emailEditText.error = "Correo no valido. Ej. luisguzman014.m@gmail.com"
            }
            var customer =
                Customer(customerID, identification, firstName, lastName, email, registerDate)

            // Actualizar cliente en la base de datos.
            val mainActivity = itemView.context as MainActivity
            val customerViewModel = mainActivity.customerViewModel
            if (isInputValid(customer)) {
                if(isEmailValid(email)) {
                    customerViewModel.updateCustomer(customer)
                    Toast.makeText(itemView.context, "Actualizando datos...", Toast.LENGTH_SHORT)
                        .show()
                    alertDialog.dismiss()
                }
            } else {
                Toast.makeText(itemView.context, "Todos los campos son obligatorios.", Toast.LENGTH_LONG).show()
            }
        }

    }

    internal fun dialogAddCustomer(context: Context) {
        val dialogBuilder = AlertDialog.Builder(context, R.style.Theme_CRUDconexusit)
        val inflater = LayoutInflater.from(context)
        val dialogView = inflater.inflate(R.layout.add_customer_dialog, null)
        dialogBuilder.setView(dialogView)

        //region Definición de tipos de entrada de datos
        val identificationEditText = dialogView.findViewById<EditText>(R.id.et_identification)
        identificationEditText.inputType = InputType.TYPE_CLASS_NUMBER

        val firstNameEditText = dialogView.findViewById<EditText>(R.id.et_first_name)
        val lastNameEditText = dialogView.findViewById<EditText>(R.id.et_last_name)
        firstNameEditText.inputType = InputType.TYPE_CLASS_TEXT
        lastNameEditText.inputType = InputType.TYPE_CLASS_TEXT

        val emailEditText = dialogView.findViewById<EditText>(R.id.et_email)
        emailEditText.inputType = InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS
        //endregion

        dialogBuilder.setTitle("Agregar nuevo cliente.")
        dialogBuilder.setMessage("Todos los campos son obligatorios.")
        dialogBuilder.setPositiveButton("Guardar") { dialog, _ -> }
        dialogBuilder.setNegativeButton("Cancelar") { dialog, _ -> dialog.dismiss() }
        val alertDialog = dialogBuilder.create()
        alertDialog.setCancelable(false)
        alertDialog.show()
        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener {
            val customerID = null
            val identification = identificationEditText.text.toString()
            val firstName = firstNameEditText.text.toString()
            val lastName = lastNameEditText.text.toString()
            val email = emailEditText.text.toString()
            if (!isEmailValid(email)) {
                emailEditText.error = "Correo no valido. Ej. luisguzman014.m@gmail.com"
            }

            var customer =
                Customer(customerID, identification, firstName, lastName, email, currentDate())

            // Agregar cliente en la base de datos.
            val mainActivity = context as MainActivity
            val customerViewModel = mainActivity.customerViewModel
            if (isInputValid(customer)) { // Se valida campos vacios
                if(isEmailValid(email)) { // Se identifica correo valido
                    var clienteExistente = false
                    for (existingCustomer in customerViewModel.customers.value!!) { // Se identifica cliente existente
                        if (existingCustomer.identification == identification || existingCustomer.email == email) {
                            Toast.makeText(
                                context,
                                "El cliente con identificación o correo electrónico ya existe.",
                                Toast.LENGTH_LONG
                            ).show()
                            clienteExistente = true
                            break
                        }
                    }

                    // Agregar cliente nuevo
                    if (!clienteExistente) {
                        customerViewModel.addCustomer(customer)
                        Toast.makeText(context, "Agregrando cliente...", Toast.LENGTH_SHORT).show()
                        alertDialog.dismiss()
                    }
                }
            } else {
                Toast.makeText(context, "Todos los campos son obligatorios.", Toast.LENGTH_LONG).show()
            }
        }
    }

    // Validar entrada de datos sobre el cliente
    private fun isInputValid(customer: Customer): Boolean {
        return (customer.identification.isNotEmpty()
                && customer.firstName.isNotEmpty()
                && customer.lastName.isNotEmpty()
                && isEmailValid(customer.email))
    }
    // Validar correo electronico
    private fun isEmailValid(email: String): Boolean {
        val pattern = Patterns.EMAIL_ADDRESS
        return pattern.matcher(email).matches()
    }

}