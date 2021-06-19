package pl.edu.pja.p03

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import pl.edu.pja.p03.databinding.ActivityRegisterBinding

class RegisterActivity : AppCompatActivity() {
    private val binding by lazy { ActivityRegisterBinding.inflate(layoutInflater) }
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setResult(RESULT_CANCELED)

        auth = FirebaseAuth.getInstance()
    }

    fun register(view: View) {
        val email = binding.emailEditText.text.toString()
        val password = binding.passwordEditText.text.toString()
        if (auth.currentUser == null) {
            if (email.isEmpty() && password.isEmpty()) {
                Toast.makeText(this, "Wprowadź adres email oraz hasło!", Toast.LENGTH_SHORT).show()
            } else if (email.isEmpty()) {
                Toast.makeText(this, "Wprowadź adres email!", Toast.LENGTH_SHORT).show()
            } else if (password.isEmpty()) {
                Toast.makeText(this, "Wprowadź hasło!", Toast.LENGTH_SHORT).show()
            } else if (password.length < 6) {
                Toast.makeText(this, "Hasło musi mieć minimum 6 znaków!", Toast.LENGTH_SHORT).show()
            } else {
                auth.createUserWithEmailAndPassword(
                    email,
                    password
                ).addOnSuccessListener {
                    Toast.makeText(this, "Zarejestrowano", Toast.LENGTH_SHORT).show()
                }
                auth.signInWithEmailAndPassword(
                    email,
                    password
                ).addOnSuccessListener {
                    Toast.makeText(this, "Zalogowano ${it.user?.uid} ${it.user?.email}", Toast.LENGTH_SHORT).show()
                    setResult(RESULT_OK)
                    finish()
                }.addOnFailureListener{
                    Toast.makeText(this, "Nie udało się!", Toast.LENGTH_SHORT).show()
                }
            }
        } else {
//            auth.signOut()
//            Toast.makeText(this, "Zalogowano ${auth.currentUser?.uid} ${auth.currentUser?.email}", Toast.LENGTH_SHORT).show()
//            FirebaseDatabase.getInstance()
//                .getReference("users")
//                .child("${auth.currentUser?.uid}")
//                .setValue("${auth.currentUser?.email}")
//                .addOnCompleteListener {
//                    println()
//                }
        }
    }
}