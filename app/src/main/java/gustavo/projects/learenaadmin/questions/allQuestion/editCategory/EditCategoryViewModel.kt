package gustavo.projects.learenaadmin.questions.allQuestion.editCategory

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import gustavo.projects.learenaadmin.categories.CategoryObject
import gustavo.projects.learenaadmin.questions.QuestionDocument
import gustavo.projects.learenaadmin.questions.allQuestion.QuestionObject

class EditCategoryViewModel(categoryName: String) : ViewModel() {

    private var auth: FirebaseAuth = Firebase.auth
    private val db = Firebase.firestore
    private lateinit var categoryDocumentRef: DocumentReference
    private lateinit var userDocumentRef: DocumentReference
    var categoryName = categoryName
    private lateinit var newCategoryName: String
    var isChangingName = false

    private val _categoryDescription = MutableLiveData<String>()
    val categoryDescription: LiveData<String>
        get() = _categoryDescription

    private val _categoryStarLevel = MutableLiveData<Int>()
    val categoryStarLevel: LiveData<Int>
        get() = _categoryStarLevel

    private val _categoryEditedSuccessfully = MutableLiveData<Boolean>()
    val categoryEditedSuccessfully: LiveData<Boolean>
        get() = _categoryEditedSuccessfully

    private val _categoryAlreadyExists = MutableLiveData<Boolean>()
    val categoryAlreadyExists: LiveData<Boolean>
        get() = _categoryAlreadyExists

    init {
        getCategoryInformationFromDatabase()
    }

    private fun getCategoryInformationFromDatabase() {
        categoryDocumentRef = db.collection("Users").document(auth.currentUser?.uid.toString()).collection(categoryName).document("QuestionDocument")
        categoryDocumentRef
            .get()
            .addOnSuccessListener { document ->
                for (field in document.data?.keys!!) {
                    if (field == "categoryDescription") {
                        _categoryDescription.value = document.data!![field] as String?
                    }else if (field == "starLevel") {
                        val starLevel = document.data!![field] as Long?
                        _categoryStarLevel.value = starLevel?.toInt()
                    }
                }
            }
            .addOnFailureListener { exception ->
                //Log.d("print", "Error getting documents.", exception)
            }
    }

    fun updateCategoryName(categoryName: String) {
        userDocumentRef = db.collection("Users").document(auth.currentUser?.uid.toString())
        userDocumentRef
            .get()
            .addOnSuccessListener { document ->
                if (document.data!!.isNotEmpty()) {
                    val categories = document.toObject<CategoryObject>()

                    if (categories != null) {
                        if(categories.listOfCategories?.contains(categoryName)!!) {
                            _categoryAlreadyExists.value = true
                            //Log.d("print", "This category name already exists")
                        }else {
                            userDocumentRef.update("listOfCategories", FieldValue.arrayUnion(categoryName))
                            newCategoryName = categoryName
                            getAllCategoryQuestionDocument()
                        }
                    }
                }else{
                    userDocumentRef.update("listOfCategories", FieldValue.arrayUnion(categoryName))
                    newCategoryName = categoryName
                    getAllCategoryQuestionDocument()
                }
            }
            .addOnFailureListener { exception ->
                //Log.d("print", "Error getting documents.", exception)
            }
    }

    fun updateCategoryInformation(categoryDescription: String, categoryStarLevel: Int) {
        val questionDocRef = db.collection("Users").document(auth.currentUser?.uid.toString()).collection(categoryName).document("QuestionDocument")
        questionDocRef
            .get()
            .addOnSuccessListener {
                val description = hashMapOf("categoryDescription" to categoryDescription)
                questionDocRef.set(description, SetOptions.merge())
                //Log.d("print", "Category description updated")
                val starLevel = hashMapOf("starLevel" to categoryStarLevel)
                questionDocRef.set(starLevel, SetOptions.merge())
                //Log.d("print", "Category star level updated")

                if (!isChangingName) {
                    _categoryEditedSuccessfully.value = true
                }
            }
            .addOnFailureListener { exception ->
                //Log.d("print", "Error getting documents.", exception)
            }
    }

    private fun getAllCategoryQuestionDocument() {
        val questionDocument = QuestionDocument()
        val questionDocRef = db.collection("Users").document(auth.currentUser?.uid.toString()).collection(categoryName).document("QuestionDocument")
        questionDocRef
            .get()
            .addOnSuccessListener {document ->
                if (document.data!!.isNotEmpty()) {
                    val databaseQuestionDoc = document.data!!
                    for (field in databaseQuestionDoc.keys) {
                        when (field) {
                            "mapOfQuestions" -> questionDocument.listOfQuestions = databaseQuestionDoc[field] as MutableMap<String, ArrayList<String>>
                            "categoryDescription" -> questionDocument.categoryDescription = databaseQuestionDoc [field] as String
                            "starLevel" -> questionDocument.starLeveL = (databaseQuestionDoc[field] as Long).toInt()
                        }
                    }
                    copyQuestionDocumentToNewCategory(newCategoryName, questionDocument)
                }
            }
            .addOnFailureListener { exception ->
                //Log.d("print", "Error getting documents.", exception)
            }
    }

    private fun copyQuestionDocumentToNewCategory(newCategoryName: String, questionDocument: QuestionDocument) {
        val questionDocRef = db.collection("Users").document(auth.currentUser?.uid.toString()).collection(newCategoryName).document("QuestionDocument")
        questionDocRef
            .get()
            .addOnSuccessListener {
                val description = hashMapOf("categoryDescription" to questionDocument.categoryDescription)
                questionDocRef.set(description, SetOptions.merge())
                val starLevel = hashMapOf("starLevel" to questionDocument.starLeveL)
                questionDocRef.set(starLevel, SetOptions.merge())

                if(questionDocument.listOfQuestions != null) {
                    val mapOfQuestion = QuestionObject(questionDocument.listOfQuestions)
                    questionDocRef.set(mapOfQuestion, SetOptions.merge())
                }
                deleteCategoryFromDatabase(categoryName)
                categoryName = newCategoryName
            }
            .addOnFailureListener { exception ->
                //Log.d("print", "Error getting documents.", exception)
            }
    }

    private fun deleteCategoryFromDatabase(categoryName: String) {
        userDocumentRef
            .get()
            .addOnSuccessListener { document ->
                if (document.data!!.isNotEmpty()) {
                    userDocumentRef.update("listOfCategories", FieldValue.arrayRemove(categoryName))
                    //Log.d("print", "The category $categoryName was removed!")
                    deleteDocumentsFromCategory(categoryName)
                    _categoryEditedSuccessfully.value = true
                    isChangingName = false
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

}
