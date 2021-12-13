package ie.wit.propertylistings.activities

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import ie.wit.propertylistings.R
import ie.wit.propertylistings.databinding.ActivityPropertyBinding
import ie.wit.propertylistings.main.MainApp
import ie.wit.propertylistings.models.PropertyModel
import timber.log.Timber
import timber.log.Timber.i

class PropertyActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPropertyBinding
    var property = PropertyModel()
    lateinit var app: MainApp

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var edit = false
        binding = ActivityPropertyBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.toolbarAdd.title = title
//        setSupportActionBar(binding.toolbarAdd)
        app = application as MainApp
        i("Property Activity started...")

        if (intent.hasExtra("property_edit")) {
            edit = true
            property = intent.extras?.getParcelable("property_edit")!!
            binding.propertyAddress.setText(property.address)
            binding.propertyDescription.setText(property.description)
            binding.btnAdd.setText(R.string.save_property)
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
            setResult(RESULT_OK)
            finish()
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

}