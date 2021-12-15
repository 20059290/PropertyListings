package ie.wit.propertylistings.main

import android.app.Application
import ie.wit.propertylistings.models.PropertyJSONStore
import ie.wit.propertylistings.models.PropertyMemStore
import ie.wit.propertylistings.models.PropertyStore
import timber.log.Timber
import timber.log.Timber.i

class MainApp : Application() {

    lateinit var properties: PropertyStore


    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
        properties = PropertyJSONStore(applicationContext)
        i("Property Listings started")
    }
}