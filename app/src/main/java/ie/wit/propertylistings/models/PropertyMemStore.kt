package ie.wit.propertylistings.models

import timber.log.Timber.i

var lastId = 0L

internal fun getId(): Long {
    return lastId++
}

class PropertyMemStore : PropertyStore {

    val properties = ArrayList<PropertyModel>()

    override fun findAll(): List<PropertyModel> {
        return properties
    }

    override fun create(property: PropertyModel) {
        property.id = getId()
        properties.add(property)
        logAll()
    }

    override fun update(property: PropertyModel) {
        var foundProperty: PropertyModel? = properties.find { p -> p.id == property.id }
        if (foundProperty != null) {
            foundProperty.address = property.address
            foundProperty.description = property.description
            foundProperty.bedrooms = property.bedrooms
            foundProperty.bathrooms = property.bathrooms
            foundProperty.price = property.price
            foundProperty.image = property.image
            foundProperty.lat = property.lat
            foundProperty.lng = property.lng
            foundProperty.zoom = property.zoom
            logAll()
        }
    }

    override fun deleteAll() {
        properties.clear()
    }

    override fun delete(property: PropertyModel) {
        properties.remove(property)
    }

    fun logAll() {
        properties.forEach{ i("${it}") }
    }
}