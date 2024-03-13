package com.example.app

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {
    private var email_login: EditText? = null
    private var password_login: EditText? = null
    private var btn_login: Button? = null
    private var register_txt: TextView? = null
    private var mAuth: FirebaseAuth? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        email_login = findViewById(R.id.email_login)
        password_login = findViewById(R.id.password_login)
        btn_login = findViewById(R.id.btn_login)
        register_txt = findViewById(R.id.register_txt)
        mAuth = FirebaseAuth.getInstance()

        // Verifică dacă utilizatorul este deja autentificat și redirectează către MainActivity
        if (mAuth?.currentUser != null) {
            val intent = Intent(this@LoginActivity, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

        btn_login?.setOnClickListener(View.OnClickListener {
            email_login?.let { emailEditText ->
                password_login?.let { passwordEditText ->
                    if (emailEditText.text.toString().isEmpty() || passwordEditText.text.toString().isEmpty()) {
                        Toast.makeText(this@LoginActivity, "Fields cannot be empty", Toast.LENGTH_SHORT).show()
                    } else {
                        mAuth?.signInWithEmailAndPassword(
                            emailEditText.text.toString(),
                            passwordEditText.text.toString()
                        )?.addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                // Afișează mesajul de înregistrare cu succes
                                val registrationSuccessMessage = findViewById<TextView>(R.id.registrationSuccessMessage)
                                registrationSuccessMessage.text = "Înregistrarea dvs la grădiniță a fost efectuată cu succes!"

                                // Redirecționează către MainActivity sau efectuează alte acțiuni necesare
                                val intent = Intent(this@LoginActivity, MainActivity::class.java)
                                startActivity(intent)
                                finish()
                            } else {
                                // Afișează mesajul de eroare în caz de eșec la autentificare
                                Toast.makeText(this@LoginActivity, "Autentificare eșuată. Verificați datele introduse.", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                }
            }
        })

        // Aici folosim setOnClickListener în loc de !! pentru a evita NullPointerException
        register_txt?.setOnClickListener {
            val intent = Intent(this@LoginActivity, RegisterActivity::class.java)
            startActivity(intent)
        }
    }
}
