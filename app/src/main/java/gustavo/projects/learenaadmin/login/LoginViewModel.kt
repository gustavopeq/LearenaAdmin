package gustavo.projects.learenaadmin.login

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class LoginViewModel : ViewModel() {

    private var auth: FirebaseAuth = Firebase.auth
    private lateinit var user: FirebaseUser

    private val _loginSuccessful = MutableLiveData<Boolean>()
    val loginSuccessful: LiveData<Boolean>
        get() = _loginSuccessful

    private val _loginFailed = MutableLiveData<Boolean>()
    val loginFailed: LiveData<Boolean>
        get() = _loginFailed

    private val _loginNotVerified = MutableLiveData<Boolean>()
    val loginNotVerified: LiveData<Boolean>
        get() = _loginNotVerified

    fun signIn(email: String, password: String) {
        onResetLoginFailed()
        onResetLoginNotVerified()

        if(email.isNullOrBlank() || password.isNullOrBlank()) {
            Log.d("print", "Login failed")
            _loginFailed.value = true
            return
        }

        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener() { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    user = auth.currentUser!!
                    if(!user.isEmailVerified){
                        Log.d("print", "Please, verify your email first!")
                        _loginNotVerified.value = true
                    }else {
                        Log.d("print", "Login successful")
                        _loginSuccessful.value = true
                    }
                } else {
                    // If sign in fails, display a message to the user.
                    Log.d("print", "Login failed")
                    _loginFailed.value = true
                }
            }
    }

    fun onResetLoginSuccessful() {
        _loginSuccessful.value = false
    }

    private fun onResetLoginFailed() {
        _loginNotVerified.value = false
    }

    private fun onResetLoginNotVerified() {
        _loginFailed.value = false
    }
}
