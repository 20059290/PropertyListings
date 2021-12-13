package ie.wit.propertylistings.models

interface PropertyStore {
    fun findAll(): List<PropertyModel>
    fun create(property: PropertyModel)
}