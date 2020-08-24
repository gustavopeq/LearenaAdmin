package gustavo.projects.learenaadmin.categories

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase

class CategoriesViewModel : ViewModel() {

    private var auth: FirebaseAuth = Firebase.auth

    private val db = Firebase.firestore
    private lateinit var userDocumentRef: DocumentReference

    private var arrayOfCategories = mutableSetOf<String>()

    private val _listOfCategoryItem = MutableLiveData<ArrayList<CategoryItem>>()
    val listOfCategoryItem: LiveData<ArrayList<CategoryItem>>
        get() = _listOfCategoryItem

    private val _itemRemovedSuccessfully = MutableLiveData<Boolean>()
    val itemRemovedSuccessfully: LiveData<Boolean>
        get() = _itemRemovedSuccessfully


    init {
        getCategoriesFromDatabase()
    }

    private fun getCategoriesFromDatabase() {
        userDocumentRef = db.collection("Users").document(auth.currentUser?.uid.toString())
        userDocumentRef
            .get()
            .addOnSuccessListener { document ->
                if (document.data!!.isNotEmpty()) {
                    val categories = document.toObject<CategoryObject>()

                    if (categories != null) {
                        arrayOfCategories = categories.listOfCategories?.toMutableSet()!!
                        generateRecyclerCategoriesItemList()
                        Log.d("print", "DB accessed and categories loaded")
                    }

                    // Add new element to the document
                    // userDocumentRef.update("listOfCategories", FieldValue.arrayUnion("Unity"))


                }else{
                    val list = arrayListOf<String>()

                    for(x in 0..500){
                        list.add("Category $x")
                    }

                    val listOfCategories =
                        CategoryObject(list)
                    Log.w("print", "SET")
                    userDocumentRef.set(listOfCategories)
                }
            }
            .addOnFailureListener { exception ->
                Log.w("print", "Error getting documents.", exception)
            }
    }

    private fun generateRecyclerCategoriesItemList() {
        val listOfItems = ArrayList<CategoryItem>()

        for(category in arrayOfCategories) {
            val item = CategoryItem(category, 0)
            listOfItems.add(item)
        }

        _listOfCategoryItem.value = listOfItems
    }

    fun deleteCategoyFromDatabase(categoryName: String) {
        userDocumentRef
            .get()
            .addOnSuccessListener { document ->
                if (document.data!!.isNotEmpty()) {
                    userDocumentRef.update("listOfCategories", FieldValue.arrayRemove(categoryName))
                    Log.d("print", "The category $categoryName was removed!")
                    _itemRemovedSuccessfully.value = true
                }else{
                    Log.d("print", "Document empty!")
                }
            }
            .addOnFailureListener { exception ->
                Log.w("print", "Error getting documents.", exception)
            }
    }

    fun resetItemRemovedFlag() {
        _itemRemovedSuccessfully.value = false
    }


}
