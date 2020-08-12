package gustavo.projects.learenaadmin.signup

import android.util.Log
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class SignupViewModel : ViewModel() {

    private var auth: FirebaseAuth = Firebase.auth

    fun createUser(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener() { task ->
                if (task.isSuccessful) {
                    Log.d("print", "User registration completed...")
                    val user = auth.currentUser
                } else {
                    Log.d("print", "User registration failed")
                }
            }
    }

}
