package ie.wit.propertylistings.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import ie.wit.propertylistings.databinding.ActivityPropertyBinding
import ie.wit.propertylistings.models.PropertyModel
import timber.log.Timber
import timber.log.Timber.i

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPropertyBinding
    var property = PropertyModel()
    val properties = ArrayList<PropertyModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityPropertyBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Timber.plant(Timber.DebugTree())
        i("Property Activity started...")


        binding.btnAdd.setOnClickListener() {
            property.address = binding.propertyAddress.text.toString()
            property.description = binding.propertyDescription.text.toString()
            if (property.address.isNotEmpty() && property.description.isNotEmpty()) {
                i("add Button Pressed: $property.title")
                properties.add(property.copy())
                for (i in properties.indices){
                    i("Properties - [$i]:${this.properties[i]}")
                }
            }
            else {
                Snackbar
                    .make(it,"Please ensure there are no empty fields", Snackbar.LENGTH_LONG)
                    .show()
            }
        }
    }
}