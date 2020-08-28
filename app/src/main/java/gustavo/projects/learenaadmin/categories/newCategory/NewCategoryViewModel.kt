package gustavo.projects.learenaadmin.categories.newCategory

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import gustavo.projects.learenaadmin.categories.CategoryObject

class NewCategoryViewModel : ViewModel() {

    private var auth: FirebaseAuth = Firebase.auth
    private val db = Firebase.firestore
    private lateinit var userDocumentRef: DocumentReference

    private var arrayOfCategories = mutableSetOf<String>()

    private val _categoryCreatedSuccessfully = MutableLiveData<Boolean>()
    val categoryCreatedSuccessfully: LiveData<Boolean>
        get() = _categoryCreatedSuccessfully

    private val _categoryAlreadyExists = MutableLiveData<Boolean>()
    val categoryAlreadyExists: LiveData<Boolean>
        get() = _categoryAlreadyExists

    private val _categoryCreationFailed = MutableLiveData<Boolean>()
    val categoryCreationFailed: LiveData<Boolean>
        get() = _categoryCreatedSuccessfully

    fun addNewCategoryToDatabase(name: String) {
        userDocumentRef = db.collection("Users").document(auth.currentUser?.uid.toString())
        userDocumentRef
            .get()
            .addOnSuccessListener { document ->
                if (document.data!!.isNotEmpty()) {
                    val categories = document.toObject<CategoryObject>()

                    if (categories != null) {
                        arrayOfCategories = categories.listOfCategories?.toMutableSet()!!
                        Log.d("print", "DB accessed and categories loaded")

                        if(categories.listOfCategories.contains(name)) {
                            _categoryAlreadyExists.value = true
                            Log.d("print", "This category name already exists")
                        }else {
                            userDocumentRef.update("listOfCategories", FieldValue.arrayUnion(name))
                            _categoryCreatedSuccessfully.value = true
                            Log.d("print", "Category $name created successfully")
                        }
                    }
                }else{
                    Log.d("print", "This user haven't created any category yet")
                    userDocumentRef.update("listOfCategories", FieldValue.arrayUnion(name))
                    _categoryCreatedSuccessfully.value = true
                    Log.d("print", "Category $name created successfully")
                }
            }
            .addOnFailureListener { exception ->
                Log.d("print", "Error getting documents.", exception)
            }
    }

    fun resetCategoryCreatedSuccessfully() {
        _categoryCreatedSuccessfully.value = false
    }

    fun resetCategoryAlreadyExists() {
        _categoryAlreadyExists.value = false
    }

}
