package com.lamesa.crudconexusit.view

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.lamesa.crudconexusit.R
import com.lamesa.crudconexusit.adapter.CustomerAdapter
import com.lamesa.crudconexusit.data.db.DatabaseConnection
import com.lamesa.crudconexusit.databinding.ActivityMainBinding
import com.lamesa.crudconexusit.util.Util.dialogAddCustomer
import com.lamesa.crudconexusit.util.Util.isOnline
import com.lamesa.crudconexusit.viewmodel.CustomerViewModel

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    internal lateinit var customerViewModel: CustomerViewModel
    private lateinit var customerAdapter: CustomerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initViewModel()
        initDatabase()
        initRecyclerView()
        loadCustomers()
        loadBinding()

        if (!isOnline(this)) {
            binding.cnEmpty.visibility = View.VISIBLE
            binding.tvEmpty.text = "Sin acceso a internet."
        }

    }

    private fun loadCustomers() {
        customerViewModel.customers.observe(this) { listCustomers ->
            if (listCustomers.isNotEmpty()) {
                binding.cnEmpty.visibility = View.GONE
            } else {
                binding.cnEmpty.visibility = View.VISIBLE
            }

            binding.loadingData.visibility = View.GONE
            binding.counterCustomers.visibility = View.VISIBLE
            binding.counterCustomers.text = listCustomers.size.toString()
            customerAdapter.updateList(listCustomers)
            customerAdapter.notifyDataSetChanged()

        }
    }

    private fun loadBinding() {
        val allCustomers = customerViewModel.customers.value
        binding.etSearch.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {
                val searchText = s.toString().trim().lowercase()
                val filteredCustomers = allCustomers!!.filter {
                    it.identification.contains(searchText)
                            || it.firstName.lowercase().contains(searchText)
                            || it.lastName.lowercase().contains(searchText)
                            || it.email.lowercase().contains(searchText)
                }

                customerAdapter.updateList(filteredCustomers)
                customerAdapter.notifyDataSetChanged()
                // Mostrar aviso de datos vacios.
                if (filteredCustomers.isEmpty()) {
                    binding.cnEmpty.visibility = View.VISIBLE
                } else {
                    binding.cnEmpty.visibility = View.GONE
                }
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(text: CharSequence, start: Int, before: Int, count: Int) {
                if (text.isEmpty()) {
                    customerAdapter.updateList(allCustomers!!)
                    customerAdapter.notifyDataSetChanged()
                }
            }
        })
        binding.fab.setOnClickListener { view ->
            dialogAddCustomer(this)
        }
        binding.ivEcuanexus.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse("https://www.ecuafact.com/")
            startActivity(intent)
        }
    }

    private fun initRecyclerView() {
        val customerRecyclerView = findViewById<RecyclerView>(R.id.customer_recycler_view)
        customerRecyclerView.layoutManager = LinearLayoutManager(this)
        customerAdapter = CustomerAdapter(this@MainActivity)
        customerRecyclerView.adapter = customerAdapter
    }

    private fun initDatabase() {
        //region Validar conexión con la base de datos.
        DatabaseConnection.checkConnection()
        DatabaseConnection.connectionStatus.observe(this) {
            Toast.makeText(this, it, Toast.LENGTH_LONG).show()
        }
        //endregion
    }

    private fun initViewModel() {
        customerViewModel = ViewModelProvider(this)[CustomerViewModel::class.java]
        customerViewModel.loadingData = binding.loadingData
    }

}

