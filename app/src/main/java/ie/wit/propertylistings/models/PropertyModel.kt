package ie.wit.propertylistings.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class PropertyModel(var id: Long = 0,
                         var address: String = "",
                         var description: String = "") : Parcelable
