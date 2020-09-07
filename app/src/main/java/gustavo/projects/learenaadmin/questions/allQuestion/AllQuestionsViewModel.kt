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

class AllQuestionsViewModel(categoryName: String) : ViewModel() {

    private var auth: FirebaseAuth = Firebase.auth
    private val db = Firebase.firestore
    private lateinit var categoryDocumentRef: DocumentReference

    private var categoryName: String = categoryName

    private var mapOfQuestions = mutableMapOf<String, ArrayList<String>>()

    private val _listOfQuestionItem = MutableLiveData<ArrayList<QuestionItem>>()
    val listOfQuestionItem: LiveData<ArrayList<QuestionItem>>
        get() = _listOfQuestionItem

    fun getQuestionsFromDatabase() {
        categoryDocumentRef = db.collection("Users").document(auth.currentUser?.uid.toString()).collection(categoryName).document("QuestionDocument")
        categoryDocumentRef
            .get()
            .addOnSuccessListener { document ->
                if (document.data!!.isNotEmpty()) {
                    val questions = document.toObject<QuestionObject>()

                    if (questions != null) {
                        mapOfQuestions = questions.mapOfQuestions?.toMutableMap()!!
                        updateRecyclerQuestionsItemList()
                        Log.d("print", "DB accessed and questions loaded")
                    }
                }else{
                    Log.d("print", "This user haven't created any question yet")
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
