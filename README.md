# Documentación App CRUD de Clientes

> Documentación y desarrollo móvil realizado por Luis A. Mesa ([luisguzman014.m@gmail.com](mailto:luisguzman014.m@gmail.com))

  

Introducción
============

El siguiente documento describe el desarrollo de una aplicación móvil que permite realizar operaciones de CRUD (Create, Read, Update y Delete) en el registro de clientes. La aplicación cuenta con un formulario que permite visualizar los clientes registrados y hacer un filtro por identificación, nombre o apellidos y correo electrónico. Además, se han realizado las validaciones necesarias para evitar que se registren correos erróneos.

  

Tecnologías utilizadas
======================

Para el desarrollo de la aplicación se han utilizado las siguientes tecnologías:

*   Base de datos: **MYSQL** con conexión en [https://www.smarterasp.net/](https://www.smarterasp.net/).
*   Lenguaje de programación: **Kotlin**.
*   Arquitectura **MVVM** para la implementación de la base de datos.
*   **Coroutines** de **Kotlin** para procesar tareas asíncronas.
*   **DataBinding** para enlazar los datos del ViewModel con la interfaz.
*   **MaterialDesign3** para un diseño agradable.

  

Codigo fuente, Video e Instalador APK
=====================================

En esta sección se incluirá el código fuente completo de la aplicación, un video demostrativo para ver su funcionamiento y un instalador para descargar e instalar la aplicación.

  

*   Código Fuente: [Github](http://rebrand.ly/CRUDconexusit)
*   Demostración: [Video](https://rebrand.ly/CRUDvideo)
*   Instalador: [Descargar](https://rebrand.ly/CRUDapk)

  

Detalles del desarrollo
=======================

Base de datos
-------------

Para la base de datos se ha creado una base de datos MYSQL en [https://www.smarterasp.net/](https://www.smarterasp.net/) con los siguientes datos:

  

*   Panel de acceso: [https://mysql.site4now.net/](https://mysql.site4now.net/)
*   Servidor: [mysql8002.site4now.net](https://mysql8002.site4now.net/)
*   DB: "db\_a943f4\_crudcon"
*   Nombre de usuario: "a943f4\_crudcon"
*   Contraseña: "CRUDcon001"

  

_Se ha realizado una consulta MYSQL para la creación de la tabla "customer"._

![](https://t3128544.p.clickup-attachments.com/t3128544/b2502dca-1422-4353-a7b6-1db21bfd662d/image.png)

  

Desarrollo en Android Studio con Kotlin
---------------------------------------

Se ha creado una clase "**DatabaseConnection**" y "**CustomerDAO**", además de un "**Customer**" (data class model) para implementar la conexión a la base de datos.

  

**DatabaseConnection**

![](https://t3128544.p.clickup-attachments.com/t3128544/96fe68ad-e085-40db-8960-7bbb284521dd/image.png)

**CustomerDAO**

![](https://t3128544.p.clickup-attachments.com/t3128544/ea79563e-374c-47d5-ad8f-2e5f56e9705c/image.png)

Asi mismo, se ha creado las respectivas funciones **addCustomer, updateCustomer, deleteCustomer** que permitirá la interacción **CRUD** con la base de datos.

  

Arquitectura MVVM
-----------------

Se ha utilizado la arquitectura MVVM para el proyecto. Se ha creado un **ViewModel** para llamar las funciones desde "**CustomerRepository**" y "**UseCases**" utilizando **Coroutines** de Kotlin para el proceso de tareas asíncronas y **LiveData** para detectar los cambios.

  

```plain
suspend fun getCustomers() {
    return withContext(Dispatchers.IO) {
        customers = getCustomersUseCase.invoke() as MutableLiveData<List<Customer>>
    }
}
```

**ViewModel + CustomerRepository + UseCases + Coroutines + LiveData**

![](https://t3128544.p.clickup-attachments.com/t3128544/ae97fd7c-e4d2-48fa-be5c-04846c00e118/screenshot_04-02-2023_17-19.png)
=============================================================================================================================

  

DataBinding
-----------

  

Se ha utilizado DataBinding para enlazar los datos del ViewModel con la interfaz en "MainActivity".

Ejemplo codigo utilizado para filtrar los clientes con el uso de **etSearch (EditText). mediante el binding** se toma la vista y se aplica un **OnClickListener,** de igual manera para las variables **fab** (Boton de agregar cliente)**, ivEcuanexus** (Imagen de Ecuanexus)**, cnEmpty** (Imagen de elementos vacios)

  

Con **TextWatcher** aplicado a **etSearch,** es posible identificar el texto de entrada y crear con el un filtro de busqueda, con el cual se crea una lista con los item que coinciden y se pasa al **recyclerview.**

  

Aplicable a:

*   Identification
*   FirstName
*   Lastname
*   Email

![](https://t3128544.p.clickup-attachments.com/t3128544/52850bf9-0215-466e-a3da-31dea36ac939/image.png)

  

```plain
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


```

  

Interfaz de usuario
-------------------

se creó un **RecyclerView**, junto al **CustomerAdapter** y **CustomerViewHolder**, para enlistar los datos de la base de datos:

![](https://t3128544.p.clickup-attachments.com/t3128544/ad61732a-9513-4d55-a609-61dfcf1347d2/image.png)

  

Además, se creó un onClickListener en cada item del RecyclerView para llamar las funciones de "deleteCustomer" para eliminar de la base de datos (`customerDAO.deleteCustomer(customer)`) y "updateCustomer" (`customerDAO.updateCustomer(customer)`). Este carga un AlertDialog con EditText que permite editar los datos y ser enviados a través de `customerDAO.updateCustomer(customer)`.

  

![](https://t3128544.p.clickup-attachments.com/t3128544/f489d72f-cd6d-4281-bacf-6b37c86bbf80/image.png)

  

Validación de entrada de datos.
-------------------------------

Además, en el formulario de registro de nuevos clientes y actualización de los mismos se realizaron validaciones para evitar la introducción de correos electrónicos inválidos. Para ello se crearon las funciones "`isInputValid()`" y "`isEmailValid()`" en el código de la clase MainActivity.

  

Para agregar un nuevo cliente, se utilizó un botón flotante que carga un formulario **dialogAddCustomer** con **EditText** que permite ingresar la información del cliente. Al presionar el botón de guardar, se llama a la función "`isInputValid()`", "`isEmailValid()`", de estar correcto se llamará "**addCustomer**" en **CustomerDAO**, la cual inserta un nuevo cliente en la base de datos. Por otro lado se validará antes si el cliente ya existe:

![](https://t3128544.p.clickup-attachments.com/t3128544/ffd585d0-fe7a-4e94-a405-974b5ae17d96/image.png)

Con la logica anterior se implementa de igual manera para **dialogUpdateCustomer** el cual **valida los datos de la misma forma.**

```kotlin
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
```

  

Conclusión
==========

En conclusión, se ha desarrollado una aplicación móvil para realizar operaciones **CRUD** en el registro de clientes. Se ha utilizado una base de datos **MySQL** en la nube para almacenar los datos de los clientes y se ha establecido una conexión segura a la misma. La aplicación se ha desarrollado utilizando Android Studio con **Kotlin** y se ha implementado la arquitectura **MVVM** para la gestión de la base de datos. Se han creado clases como **CustomerDAO**, **DatabaseConnection** y **Customer** (clase de datos modelo) para facilitar las operaciones de **creación, lectura, actualización y eliminación** de clientes en la base de datos. Además, se ha utilizado un **RecyclerView** junto con un **CustomerAdapter** y un **CustomerViewHolder** para mostrar los datos de los clientes en la interfaz gráfica. También se han implementado funciones de validación para evitar errores en el registro de correos electrónicos.
