package com.sgr.glucomaster

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.auth.FirebaseAuth
import com.sgr.glucomaster.databinding.ActivityResetPassBinding

class ResetPassActivity : AppCompatActivity() {

    //Variables
    private lateinit var auth: FirebaseAuth
    private lateinit var binding: ActivityResetPassBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityResetPassBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        auth = FirebaseAuth.getInstance()

        binding.btnResetPass.setOnClickListener {

            val email = binding.etMailReset.text.toString()
            resetPass(email)
        }
    }

    //Function to reset pass
    private fun resetPass(email: String) {

        if (email.isNotEmpty()) {

            auth.sendPasswordResetEmail(email)
                .addOnCompleteListener(this) {

                    if (it.isSuccessful) {
                        //Sent OK
                        Toast.makeText(this, getString(R.string.resetOK), Toast.LENGTH_SHORT)
                            .show()
                        finish()
                    } else {
                        //Failed to send
                        Toast.makeText(this, getString(R.string.resetKO), Toast.LENGTH_SHORT)
                            .show()
                    }
                }
            //Email empty
        } else {
            Toast.makeText(this, getString(R.string.resetempty), Toast.LENGTH_SHORT).show()
        }
    }
}