package com.sgr.glucomaster

import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import com.sgr.glucomaster.databinding.ActivityGlucemiaBinding
import com.sgr.glucomaster.db.Restriccion
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class GlucemiaActivity : AppCompatActivity() {

    //Variables
    private lateinit var binding: ActivityGlucemiaBinding
    private val database = Database(AndroidSqliteDriver(Database.Schema, this, "Database.db"))
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityGlucemiaBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        //Get turn and regimen id coming from main activity
        val turno = intent.getStringExtra("turno")
        val pautaId = intent.getLongExtra("pautaId", 0)
        val userId = intent.getLongExtra("userId", 0)
        //Get the med name from db
        val medicacion = database.pauta_medicacionQueries.getMedbyReg(pautaId, turno?.lowercase()).executeAsOne().nombre

        binding.tvGlucemiaTurn.setText(turno)
        binding.tvGlucMed.setText(medicacion)

        //Get restrictions by regimen id and turn
        val listaRest = turno?.let { database.restriccionQueries.getAllRestByTurn(it, pautaId).executeAsList() }

        binding.btnCalculateDose.setOnClickListener {

            //Get glycemia
            val glucemia = binding.etGlucemia.text.toString().toLong()
            //Calculate dose
            val dosis = calculateDose(listaRest, glucemia)
            //Get actual date
            val fechaActual = LocalDate.now()
            val formateador = DateTimeFormatter.ofPattern("dd/MM/yyyy")
            val fechaFormateada = fechaActual.format(formateador)

            //Register glycemia in db
            database.glucemiaQueries.insertGluc(fechaFormateada,turno!!.lowercase(), glucemia, userId)
            //Show dose
            if (dosis.toInt() != 0) {
                binding.tvGlucDos.setText(dosis.toString() + "U")
                Toast.makeText(baseContext, getString(R.string.glucRegistrada), Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    //Function to calculate dose according to restrictions
    private fun calculateDose(listaRest: List<Restriccion>?, glucemia: Long): Long {

        var dosis: Long = 0

        if (listaRest != null) {
            for (item in listaRest) {

                if (glucemia > item.LimiteInferior!! && glucemia < item.LimiteSuperior!!) {

                    dosis = item.dosis!!
                }
            }
        }

        if (dosis.toInt() == 0) {

            Toast.makeText(baseContext, getString(R.string.noMargen), Toast.LENGTH_LONG).show()
            return 0
        } else {

            return dosis
        }
    }


}