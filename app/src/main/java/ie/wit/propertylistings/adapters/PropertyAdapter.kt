package ie.wit.propertylistings.adapters

import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ie.wit.propertylistings.R
import ie.wit.propertylistings.databinding.CardPropertyBinding
import ie.wit.propertylistings.models.PropertyModel

class PropertyAdapter constructor(private var properties: List<PropertyModel>) :
    RecyclerView.Adapter<PropertyAdapter.MainHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainHolder {
        val binding = CardPropertyBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)

        return MainHolder(binding)
    }

    override fun onBindViewHolder(holder: MainHolder, position: Int) {
        val property = properties[holder.adapterPosition]
        holder.bind(property)
    }

    override fun getItemCount(): Int = properties.size

    class MainHolder(private val binding : CardPropertyBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(property: PropertyModel) {
            binding.propertyAddress.text = property.address
            binding.description.text = property.description
        }
    }
}