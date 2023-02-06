package com.lamesa.crudconexusit.app

import android.app.Application


/** Created by luis Mesa on 08/08/22 */
class App : Application() {

    companion object {
        lateinit var instance: App
            private set
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
    }



}



