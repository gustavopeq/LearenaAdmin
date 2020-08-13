package gustavo.projects.learenaadmin.login

import android.util.Log
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class LoginViewModel : ViewModel() {


    private var auth: FirebaseAuth = Firebase.auth

    fun signIn(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener() { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    val user = auth.currentUser
                    Log.d("print", "Login successful")
                } else {
                    // If sign in fails, display a message to the user.
                    Log.d("print", "Login failed")
                }
            }
    }
}