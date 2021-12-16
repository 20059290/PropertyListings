package ie.wit.propertylistings.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.LinearLayoutManager
import ie.wit.propertylistings.R
import ie.wit.propertylistings.adapters.PropertyAdapter
import ie.wit.propertylistings.adapters.PropertyListener
import ie.wit.propertylistings.databinding.ActivityPropertyListBinding
import ie.wit.propertylistings.main.MainApp
import ie.wit.propertylistings.models.PropertyModel

class PropertyListActivity : AppCompatActivity(), PropertyListener {

    lateinit var app: MainApp
    private lateinit var binding: ActivityPropertyListBinding
    private lateinit var refreshIntentLauncher : ActivityResultLauncher<Intent>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPropertyListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.toolbar.title = title
        setSupportActionBar(binding.toolbar)

        app = application as MainApp

        val layoutManager = LinearLayoutManager(this)
        binding.recyclerView.layoutManager = layoutManager
        binding.recyclerView.adapter = PropertyAdapter(app.properties.findAll(),this)
        loadProperties()

        registerRefreshCallback()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.item_add -> {
                val launcherIntent = Intent(this, PropertyActivity::class.java)
                refreshIntentLauncher.launch(launcherIntent)
            }
            R.id.delete_all -> {
                app.properties.deleteAll()
                loadProperties()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onPropertyClick(property: PropertyModel) {
        val launcherIntent = Intent(this, PropertyActivity::class.java)
        launcherIntent.putExtra("property_edit", property)
        refreshIntentLauncher.launch(launcherIntent)
    }

//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        binding.recyclerView.adapter?.notifyDataSetChanged()
//        super.onActivityResult(requestCode, resultCode, data)
//    }

    private fun registerRefreshCallback() {
        refreshIntentLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult())
            { loadProperties() }
    }

    private fun loadProperties() {
        showProperties(app.properties.findAll())
    }

    fun showProperties (properties: List<PropertyModel>) {
        binding.recyclerView.adapter = PropertyAdapter(properties, this)
        binding.recyclerView.adapter?.notifyDataSetChanged()
    }
}