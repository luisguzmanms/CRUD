package com.lamesa.crudconexusit.data.db

import android.os.StrictMode
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.lamesa.crudconexusit.app.App
import com.lamesa.crudconexusit.util.Util.isOnline
import java.sql.Connection
import java.sql.DriverManager
import java.sql.SQLException

object DatabaseConnection {

    // MYSQL
    private val ip = "mysql8002.site4now.net"
    private val db = "db_a943f4_crudcon"
    private val username = "a943f4_crudcon"
    private val password = "CRUDcon001"
    var connectionStatus = MutableLiveData<String>()

    fun getConnection(): Connection? {
        var connection: Connection? = null
        // Validar conexión a internet e intentar conexión con la abse de datos
        if (isOnline(App.instance)) {
            val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
            StrictMode.setThreadPolicy(policy)
            try {
                Class.forName("com.mysql.jdbc.Driver").newInstance()
                connection = DriverManager.getConnection(
                    "jdbc:mysql://$ip/$db?useSSL=false&allowPublicKeyRetrieval=true",
                    username,
                    password
                )
            } catch (ex: SQLException) {
                ex.message.let { connectionStatus.value = ex.message }
                Log.e("dbConn Error: ", ex.message!!)
                println("dbConn Error: $ex")
            }
        }
        return connection
    }

    fun checkConnection() : Boolean {
        return getConnection()!=null
    }

}
