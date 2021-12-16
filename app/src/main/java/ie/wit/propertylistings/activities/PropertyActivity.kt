package ie.wit.propertylistings.activities

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import com.squareup.picasso.Picasso
import ie.wit.propertylistings.R
import ie.wit.propertylistings.adapters.PropertyListener
import ie.wit.propertylistings.databinding.ActivityPropertyBinding
import ie.wit.propertylistings.helpers.showImagePicker
import ie.wit.propertylistings.main.MainApp
import ie.wit.propertylistings.models.Location
import ie.wit.propertylistings.models.PropertyModel
import timber.log.Timber
import timber.log.Timber.i
import android.view.View
import android.widget.NumberPicker
import androidx.core.view.get


class PropertyActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPropertyBinding
    var property = PropertyModel()
    lateinit var app: MainApp
    private lateinit var imageIntentLauncher : ActivityResultLauncher<Intent>
    private lateinit var mapIntentLauncher : ActivityResultLauncher<Intent>
//    var location = Location(52.245696, -7.139102, 15f)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var edit = false
        binding = ActivityPropertyBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.toolbarAdd.title = title
        setupNumberPickers()
        setSupportActionBar(binding.toolbarAdd)
        app = application as MainApp
        i("Property Activity started...")

        if (intent.hasExtra("property_edit")) {
            edit = true
            property = intent.extras?.getParcelable("property_edit")!!
            binding.propertyAddress.setText(property.address)
            binding.propertyBedrooms.value = property.bedrooms
            binding.propertyBathrooms.value = property.bathrooms
            binding.propertyPrice.value = property.price
            binding.propertyDescription.setText(property.description)
            binding.btnAdd.setText(R.string.save_property)
            Picasso.get()
                .load(property.image)
                .into(binding.propertyImage)
            if (property.image != Uri.EMPTY) {
                binding.chooseImage.setText(R.string.change_property_image)
            }
        }

        binding.btnAdd.setOnClickListener {
            property.address = binding.propertyAddress.text.toString()
            property.description = binding.propertyDescription.text.toString()
            property.bedrooms = binding.propertyBedrooms.value
            property.bathrooms = binding.propertyBathrooms.value
            property.price = binding.propertyPrice.value
            i("add Button Pressed: $property")
            if (property.address.isEmpty() || property.bedrooms == 0 || property.bathrooms == 0 || property.price == 0) {
                Snackbar
                    .make(it,R.string.enter_fields, Snackbar.LENGTH_LONG)
                    .show()
            }
            else {
                if (edit){
                    app.properties.update(property.copy())
                }
                else {
                    app.properties.create(property.copy())
                }
                setResult(RESULT_OK)
                finish()
            }
        }
        binding.chooseImage.setOnClickListener {
            showImagePicker(imageIntentLauncher)
        }


        binding.propertyLocation.setOnClickListener {
            i ("Set Location Pressed")
            val location = Location(52.245696, -7.139102, 15f)
            if (property.zoom != 0f) {
                location.lat =  property.lat
                location.lng = property.lng
                location.zoom = property.zoom
            }
            val launcherIntent = Intent(this, MapActivity::class.java)
                .putExtra("location", location)
            mapIntentLauncher.launch(launcherIntent)
        }
        registerMapCallback()
        registerImagePickerCallback()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_property, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.cancel_add -> {
                finish()
            }
            R.id.delete_prop -> {
                    app.properties.delete(property)
                    finish()
                }
        }
        return super.onOptionsItemSelected(item)
    }


    private fun setupNumberPickers() {
        //Similar to edit mode,
        // If the number of bedrooms or bathrooms is 0 the associated number picker will show as 0.
        // Otherwise the number pickers will show the previous value they were assigned.
        if (property.bedrooms == 0){
            var numbBeds = binding.propertyBedrooms
            numbBeds.minValue = 0
            numbBeds.maxValue = 10
            numbBeds.wrapSelectorWheel = true
        }
        else{
            binding.propertyBedrooms.value = property.bedrooms
            var numbBeds = binding.propertyBedrooms
            numbBeds.minValue = 0
            numbBeds.maxValue = 10
            numbBeds.wrapSelectorWheel = true
            property.bedrooms = binding.propertyBedrooms.value
        }
        if (property.bathrooms == 0){
            var numbBaths = binding.propertyBathrooms
            numbBaths.minValue = 0
            numbBaths.maxValue = 10
            numbBaths.wrapSelectorWheel = true
        }
        else{
            binding.propertyBathrooms.value = property.bathrooms
            var numbBaths = binding.propertyBathrooms
            numbBaths.minValue = 0
            numbBaths.maxValue = 10
            numbBaths.wrapSelectorWheel = true
            property.bathrooms = binding.propertyBathrooms.value
        }
        if (property.price == 0){
            var price = binding.propertyPrice
            price.minValue = 0
            price.maxValue = 30_000_000
        }
        else{
            binding.propertyBathrooms.value = property.price
            var numbBaths = binding.propertyPrice
            numbBaths.minValue = 0
            numbBaths.maxValue = 30_000_000
//            numbBaths.wrapSelectorWheel = true
            property.bathrooms = binding.propertyPrice.value
        }
    }


    private fun registerImagePickerCallback() {
        imageIntentLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult())
            { result ->
                when(result.resultCode){
                    RESULT_OK -> {
                        if (result.data != null) {
                            i("Got Result ${result.data!!.data}")
                            property.image = result.data!!.data!!
                            Picasso.get()
                                .load(property.image)
                                .into(binding.propertyImage)
                            binding.chooseImage.setText(R.string.change_property_image)
                        } // end of if
                    }
                    RESULT_CANCELED -> { } else -> { }
                }
            }
    }

    private fun registerMapCallback() {
        mapIntentLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult())
            { result ->
                when (result.resultCode) {
                    RESULT_OK -> {
                        if (result.data != null) {
                            i("Got Location ${result.data.toString()}")
                            val location = result.data!!.extras?.getParcelable<Location>("location")!!
                            i("Location == $location")
                            property.lat = location.lat
                            property.lng = location.lng
                            property.zoom = location.zoom
                        } // end of if
                    }
                    RESULT_CANCELED -> { } else -> { }
                }
            }
    }
}