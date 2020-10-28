package gustavo.projects.learenaadmin.signup

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.auth.ktx.userProfileChangeRequest
import com.google.firebase.ktx.Firebase

class SignupViewModel : ViewModel() {

    private var auth: FirebaseAuth = Firebase.auth
    private lateinit var user:FirebaseUser

    private val _userCreated = MutableLiveData<Boolean>()
    val userCreated: LiveData<Boolean>
        get() = _userCreated

    fun createUser(name:String, email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener() { task ->
                if (task.isSuccessful) {
                    Log.d("print", "User registration completed...")
                    _userCreated.value = true
                    user = auth.currentUser!!
                    updateProfileName(name)
                    sendConfirmationEmail()
                } else {
                    Log.d("print", "User registration failed")
                    _userCreated.value = false
                }
            }
    }

    private fun updateProfileName(name: String) {
        val profileUpdates = userProfileChangeRequest {
            displayName = name
        }

        user.updateProfile(profileUpdates)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d("print", "User profile name updated")
                }
            }
    }

    private fun sendConfirmationEmail() {
        user.sendEmailVerification()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d("print", "Email sent.")
                }
            }
    }

}
