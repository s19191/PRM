package pl.edu.pja.p03

import android.R
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import pl.edu.pja.p03.databinding.ActivityLogInBinding


const val REGISTER_REQ = 1

class LogInActivity : AppCompatActivity() {
    private val binding by lazy { ActivityLogInBinding.inflate(layoutInflater) }
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        if (auth.currentUser != null) {
            finish()
        }
    }

    override fun onResume() {
        super.onResume()
        if (auth.currentUser != null) {
            finish()
        }
    }

    fun logIn(view: View) {
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
                auth.signInWithEmailAndPassword(
                    email,
                    password
                ).addOnSuccessListener {
                    Toast.makeText(this, "Zalogowano ${it.user?.uid} ${it.user?.email}", Toast.LENGTH_SHORT).show()
                }
            }
        } else {
//            auth.signOut()
            Toast.makeText(this, "Zalogowano ${auth.currentUser?.uid} ${auth.currentUser?.email}", Toast.LENGTH_SHORT).show()
//            startActivity(Intent(this, ArticleListActivity::class.java))
//            FirebaseDatabase.getInstance()
//                .getReference("users")
//                .child("${auth.currentUser?.uid}")
//                .setValue("${auth.currentUser?.email}")
//                .addOnCompleteListener {
//                    println()
//                }
        }
        finish()
    }

    fun register(view: View) {
        startActivityForResult(Intent(this, RegisterActivity::class.java), REGISTER_REQ)
    }

    fun logInViaGoogle(view: View) {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
//            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REGISTER_REQ) {
            if (resultCode == Activity.RESULT_OK) {
                finish()
            }
        } else super.onActivityResult(requestCode, resultCode, data)
    }
}