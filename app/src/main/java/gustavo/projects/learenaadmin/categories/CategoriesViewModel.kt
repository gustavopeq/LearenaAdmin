package gustavo.projects.learenaadmin.categories

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

    private val _noCategoryFound = MutableLiveData<Boolean>()
    val noCategoryFound: LiveData<Boolean>
        get() = _noCategoryFound

    fun getCategoriesFromDatabase() {
        userDocumentRef = db.collection("Users").document(auth.currentUser?.uid.toString())
        userDocumentRef
            .get()
            .addOnSuccessListener { document ->
                var anyCategoryFound = false
                if (!document.data.isNullOrEmpty()) {
                    val categories = document.toObject<CategoryObject>()

                    if (categories != null && categories.listOfCategories!!.isNotEmpty()) {
                        arrayOfCategories = categories.listOfCategories?.toMutableSet()!!
                        _noCategoryFound.value = false
                        anyCategoryFound = true
                        updateRecyclerCategoriesItemList()
                        //Log.d("print", "DB accessed and categories loaded")
                    }
                }else{
                    //Log.d("print", "This user haven't created any category yet")
                }

                if(!anyCategoryFound) {
                    _noCategoryFound.value = true
                }
            }
            .addOnFailureListener { exception ->
                //Log.d("print", "Error getting documents.", exception)
            }
    }

    private fun updateRecyclerCategoriesItemList() {
        val listOfItems = ArrayList<CategoryItem>()

        for(category in arrayOfCategories) {
            val item = CategoryItem(category)
            listOfItems.add(item)
        }

        _listOfCategoryItem.value = listOfItems
    }

    fun deleteCategoryFromDatabase(categoryName: String) {
        userDocumentRef
            .get()
            .addOnSuccessListener { document ->
                if (document.data!!.isNotEmpty()) {
                    userDocumentRef.update("listOfCategories", FieldValue.arrayRemove(categoryName))
                    //Log.d("print", "The category $categoryName was removed!")
                    arrayOfCategories.remove(categoryName)
                    deleteDocumentsFromCategory(categoryName)
                    updateRecyclerCategoriesItemList()
                    _itemRemovedSuccessfully.value = true
                }else{
                    //Log.d("print", "Document empty!")
                }
            }
            .addOnFailureListener { exception ->
                //Log.d("print", "Error getting documents.", exception)
            }
    }

    private fun deleteDocumentsFromCategory(categoryName: String) {
        userDocumentRef.collection(categoryName).document("QuestionDocument").delete()
    }

    fun resetItemRemovedFlag() {
        _itemRemovedSuccessfully.value = false
    }

    fun quantityOfCategories(): Int {
        return arrayOfCategories.size
    }


}
