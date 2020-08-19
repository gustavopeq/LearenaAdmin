package gustavo.projects.learenaadmin.categories

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase

class CategoriesViewModel : ViewModel() {

    private var auth: FirebaseAuth = Firebase.auth

    val db = Firebase.firestore

    private val _arrayOfCategories = MutableLiveData<MutableSet<String>>()
    val arrayOfCategories: LiveData<MutableSet<String>>
        get() = _arrayOfCategories


    init {
        getCategoriesFromDatabase()
    }

    private fun getCategoriesFromDatabase() {
        val userDocumentRef = db.collection("Users").document(auth.currentUser?.uid.toString())
        userDocumentRef
            .get()
            .addOnSuccessListener { document ->
                if (document.data!!.isNotEmpty()) {
                    val categories = document.toObject<CategoryObject>()

                    if (categories != null) {
                        _arrayOfCategories.value = categories.listOfCategories?.toMutableSet()
                    }

                    // Add new element to the document
                    userDocumentRef.update("listOfCategories", FieldValue.arrayUnion("Unity"))


                }else{
                    val listOfCategories =
                        CategoryObject(
                            listOf(
                                "Unity",
                                "OOP"
                            )
                        )
                    Log.w("print", "SET")
                    userDocumentRef.set(listOfCategories)
                }
            }
            .addOnFailureListener { exception ->
                Log.w("print", "Error getting documents.", exception)
            }
    }


}
