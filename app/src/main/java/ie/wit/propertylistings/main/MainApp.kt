package ie.wit.propertylistings.main

import android.app.Application
//import ie.wit.propertylistings.models.PropertyModel
import ie.wit.propertylistings.models.PropertyMemStore
import timber.log.Timber
import timber.log.Timber.i

class MainApp : Application() {

//    val properties = ArrayList<PropertyModel>()
    val properties = PropertyMemStore()


    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
        i("Property Listings started")
    }
}