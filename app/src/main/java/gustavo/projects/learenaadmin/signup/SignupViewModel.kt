package gustavo.projects.learenaadmin.signup

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class SignupViewModel : ViewModel() {

    private var auth: FirebaseAuth = Firebase.auth

    private val _userCreated = MutableLiveData<Boolean>()
    val userCreated: LiveData<Boolean>
        get() = _userCreated

    fun createUser(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener() { task ->
                if (task.isSuccessful) {
                    Log.d("print", "User registration completed...")
                    _userCreated.value = true
                    val user = auth.currentUser
                } else {
                    Log.d("print", "User registration failed")
                    _userCreated.value = false
                }
            }
    }

}
