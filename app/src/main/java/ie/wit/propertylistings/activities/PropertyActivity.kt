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
import ie.wit.propertylistings.models.PropertyModel
import timber.log.Timber
import timber.log.Timber.i

class PropertyActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPropertyBinding
    var property = PropertyModel()
    lateinit var app: MainApp
    private lateinit var imageIntentLauncher : ActivityResultLauncher<Intent>
    private lateinit var mapIntentLauncher : ActivityResultLauncher<Intent>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var edit = false
        binding = ActivityPropertyBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.toolbarAdd.title = title
//        setSupportActionBar(binding.toolbarAdd)      #This is disabled for testing purposes... Will be enabled in the final edit
        app = application as MainApp
        i("Property Activity started...")

        if (intent.hasExtra("property_edit")) {
            edit = true
            property = intent.extras?.getParcelable("property_edit")!!
            binding.propertyAddress.setText(property.address)
            binding.propertyDescription.setText(property.description)
            binding.btnAdd.setText(R.string.save_property)
            Picasso.get()
                .load(property.image)
                .into(binding.propertyImage)
            if (property.image != Uri.EMPTY) {
                binding.chooseImage.setText(R.string.change_property_image)
            }
        }

        binding.btnAdd.setOnClickListener() {
            property.address = binding.propertyAddress.text.toString()
            property.description = binding.propertyDescription.text.toString()
            if (property.address.isEmpty() && property.description.isEmpty()) {
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
            }
            i("add Button Pressed: $property")
            setResult(RESULT_OK)
            finish()
        }
        binding.chooseImage.setOnClickListener {
            showImagePicker(imageIntentLauncher)
        }
        registerImagePickerCallback()

        binding.propertyLocation.setOnClickListener {
            i ("Set Location Pressed")
            val launcherIntent = Intent(this, MapActivity::class.java)
            mapIntentLauncher.launch(launcherIntent)
        }
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
        }
        return super.onOptionsItemSelected(item)
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
            { i("Map Loaded") }
    }
}