package com.example.app

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class MainActivity : AppCompatActivity() {
    private lateinit var logout_btn: Button
    private var mAuth: FirebaseAuth? = null
    private lateinit var textView: TextView
    private var email: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        logout_btn = findViewById(R.id.logout_btn)
        mAuth = FirebaseAuth.getInstance()
        textView = findViewById(R.id.textView)

        // Verifică dacă utilizatorul este nou înregistrat și afișează un mesaj corespunzător
        val isNewUser = intent.getBooleanExtra("isNewUser", false)
        if (isNewUser) {
            textView.text = "Înregistrarea la grădiniță a fost efectuată cu succes!"
        } else {
            // În caz contrar, afișează adresa de e-mail normal
            val ref = FirebaseDatabase.getInstance().getReference("Users").child(mAuth?.currentUser?.uid ?: "")
            ref.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    email = snapshot.child("email").value.toString()
                    textView.text = email
                }

                override fun onCancelled(error: DatabaseError) {}
            })
        }

        logout_btn.setOnClickListener {
            mAuth?.signOut()
            val intent = Intent(this@MainActivity, LoginActivity::class.java)
            startActivity(intent)
        }
    }
}
