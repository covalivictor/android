package com.example.app

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class RegisterActivity : AppCompatActivity() {
    private var email_register: EditText? = null
    private var password_register: EditText? = null
    private var btn_register: Button? = null
    private var btn_login_after_register: Button? = null
    private var mAuth: FirebaseAuth? = null
    private var ref: DatabaseReference? = null
    private var database: FirebaseDatabase? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        mAuth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()
        ref = database!!.reference

        email_register = findViewById<EditText>(R.id.email_register)
        password_register = findViewById<EditText>(R.id.password_register)
        btn_register = findViewById<Button>(R.id.btn_register)
        btn_login_after_register = findViewById<Button>(R.id.btn_login_after_register)

        email_register?.let { emailEditText ->
            password_register?.let { passwordEditText ->
                val phoneEditText = findViewById<EditText>(R.id.phone_register)

                btn_register?.setOnClickListener(View.OnClickListener {
                    val phone = phoneEditText.text.toString()

                    if (emailEditText.text.toString().isEmpty() || passwordEditText.text.toString().isEmpty()) {
                        Toast.makeText(
                            this@RegisterActivity,
                            "Fields cannot be empty",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        mAuth?.createUserWithEmailAndPassword(
                            emailEditText.text.toString(),
                            passwordEditText.text.toString()
                        )?.addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                ref?.child("Users")?.child(mAuth?.currentUser?.uid ?: "")?.child("email")
                                    ?.setValue(emailEditText.text.toString())
                                ref?.child("Users")?.child(mAuth?.currentUser?.uid ?: "")?.child("password")
                                    ?.setValue(passwordEditText.text.toString())

                                // Salvează numărul de telefon în baza de date Firebase
                                savePhoneNumberToDatabase(phone)

                                Toast.makeText(
                                    this@RegisterActivity,
                                    "Registration successful",
                                    Toast.LENGTH_SHORT
                                ).show()

                                // Navigare către MainActivity cu informația că utilizatorul este nou înregistrat
                                val intent = Intent(
                                    this@RegisterActivity,
                                    MainActivity::class.java
                                )
                                intent.putExtra("isNewUser", true)
                                startActivity(intent)
                                finish()
                            } else {
                                Toast.makeText(
                                    this@RegisterActivity,
                                    "Registration failed",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    }
                })

                btn_login_after_register?.setOnClickListener {
                    val intent = Intent(this@RegisterActivity, LoginActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            }
        }
    }

    private fun savePhoneNumberToDatabase(phone: String) {
        val currentUser = mAuth?.currentUser
        val userReference = FirebaseDatabase.getInstance().getReference("Users")
            .child(currentUser?.uid ?: "")

        // Salvează numărul de telefon în baza de date Firebase
        userReference.child("phone").setValue(phone)
    }
}
