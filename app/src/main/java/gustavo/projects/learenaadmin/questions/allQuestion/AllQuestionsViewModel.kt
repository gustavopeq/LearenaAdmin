package gustavo.projects.learenaadmin.questions.allQuestion

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase

class AllQuestionsViewModel(private var categoryName: String) : ViewModel() {

    private var auth: FirebaseAuth = Firebase.auth
    private val db = Firebase.firestore
    private lateinit var categoryDocumentRef: DocumentReference

    private var mapOfQuestions = mutableMapOf<String, ArrayList<String>>()

    private val _listOfQuestionItem = MutableLiveData<ArrayList<QuestionItem>>()
    val listOfQuestionItem: LiveData<ArrayList<QuestionItem>>
        get() = _listOfQuestionItem

    private val _categoryDescription = MutableLiveData<String>()
    val categoryDescription: LiveData<String>
        get() = _categoryDescription

    private val _categoryStarLevel = MutableLiveData<Int>()
    val categoryStarLevel: LiveData<Int>
        get() = _categoryStarLevel

    private val _noQuestionFound = MutableLiveData<Boolean>()
    val noQuestionFound: LiveData<Boolean>
        get() = _noQuestionFound

    fun getQuestionsFromDatabase() {
        categoryDocumentRef = db.collection("Users").document(auth.currentUser?.uid.toString()).collection(categoryName).document("QuestionDocument")
        categoryDocumentRef
            .get()
            .addOnSuccessListener { document ->
                var anyQuestionFound = false
                    for (field in document.data?.keys!!) {
                        if (field == "mapOfQuestions") {
                            val questions = document.toObject<QuestionObject>()

                            if (questions != null && questions.mapOfQuestions!!.isNotEmpty()) {
                                mapOfQuestions = questions.mapOfQuestions?.toMutableMap()!!
                                _noQuestionFound.value = false
                                anyQuestionFound = true
                                updateRecyclerQuestionsItemList()
                                Log.d("print", "DB accessed and questions loaded")
                            }
                        }else if (field == "categoryDescription") {
                            _categoryDescription.value = document.data!![field] as String?
                        }else if (field == "starLevel") {
                            val starLevel = document.data!![field] as Long?
                            _categoryStarLevel.value = starLevel?.toInt()
                        }
                    }
                if(!anyQuestionFound) {
                    _noQuestionFound.value = true
                }
            }
            .addOnFailureListener { exception ->
                Log.d("print", "Error getting documents.", exception)
            }
    }

    private fun updateRecyclerQuestionsItemList() {
        val listOfItems = ArrayList<QuestionItem>()

        for(question in mapOfQuestions) {
            val item = QuestionItem(question.key)
            listOfItems.add(item)
        }

        _listOfQuestionItem.value = listOfItems
    }

}
