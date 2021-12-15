package ie.wit.propertylistings.models

interface PropertyStore {
    fun findAll(): List<PropertyModel>
    fun create(property: PropertyModel)
    fun update(property: PropertyModel)
    fun delete(property: PropertyModel)
    fun deleteAll()
}