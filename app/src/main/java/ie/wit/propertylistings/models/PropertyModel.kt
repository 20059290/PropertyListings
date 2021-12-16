package ie.wit.propertylistings.models

import android.net.Uri
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class PropertyModel(var id: Long = 0,
                         var address: String = "",
                         var description: String = "",
                         var bedrooms: Int = 0,
                         var bathrooms: Int =0,
                         var price: Int = 0,
                         var image: Uri = Uri.EMPTY,
                         var lat : Double = 0.0,
                         var lng: Double = 0.0,
                         var zoom: Float = 0f) : Parcelable

@Parcelize
data class Location(var lat: Double = 0.0,
                    var lng: Double = 0.0,
                    var zoom: Float = 0f) : Parcelable
