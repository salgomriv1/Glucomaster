package com.sgr.glucomaster

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import com.google.firebase.auth.FirebaseAuth
import com.sgr.glucomaster.databinding.ActivityMainBinding
import com.sgr.glucomaster.db.Usuario

class MainActivity : AppCompatActivity() {

    //Variables
    private lateinit var binding: ActivityMainBinding
    private lateinit var auth: FirebaseAuth
    private val database = Database(AndroidSqliteDriver(Database.Schema, this, "Database.db"))
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        auth = FirebaseAuth.getInstance()
        //Get actual logged user
        val user = auth.currentUser
        val toolbar = binding.toolbar

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(false)

        //Check if the user is in the local db
        val usuario: Usuario? = database.userQueries.getUser(auth.currentUser?.email).executeAsOneOrNull()

        //If the user is not is the DB, insert it
        if (usuario == null) {
            database.userQueries.insertUser(auth.currentUser?.email)
        }


        binding.tvPautaActual.setOnClickListener() {

            val intent = Intent (this, CurrentRegimenActivity::class.java)
            startActivity(intent)
        }

        binding.btnAddPattern.setOnClickListener {

            val intent = Intent (this, AddRegimenActivity::class.java)
            startActivity(intent)
        }

        binding.btnAddMed.setOnClickListener {

            val intent = Intent (this, AddMedicationActivity::class.java)
            startActivity(intent)
        }

        binding.btnTips.setOnClickListener {

            val intent = Intent (this, TipsActivity::class.java)
            startActivity(intent)
        }

        binding.btnDesayuno.setOnClickListener {

            val intent = Intent (this, GlucemiaActivity::class.java)
            startActivity(intent)
        }



    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        menuInflater.inflate(R.menu.my_menu, menu)
        return true
    }

    //Configure the menu option
    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            R.id.action_settings -> {
                auth.signOut()
                Toast.makeText(baseContext, getString(R.string.cierresesion), Toast.LENGTH_SHORT)
                    .show()
                val  intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }
}