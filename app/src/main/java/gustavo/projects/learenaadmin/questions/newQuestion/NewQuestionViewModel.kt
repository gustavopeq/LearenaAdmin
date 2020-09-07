package gustavo.projects.learenaadmin.questions.newQuestion

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
import com.google.firebase.ktx.Firebase
import gustavo.projects.learenaadmin.questions.allQuestion.QuestionObject

class NewQuestionViewModel(categoryName: String) : ViewModel() {

    private var auth: FirebaseAuth = Firebase.auth
    private val db = Firebase.firestore
    private lateinit var categoryDocumentRef: DocumentReference

    private val categoryName = categoryName

    private val _questionCreatedSuccessfully = MutableLiveData<Boolean>()
    val questionCreatedSuccessfully: LiveData<Boolean>
        get() = _questionCreatedSuccessfully

    private fun addNewQuestionToDatabase(newQuestion: QuestionObject) {
        categoryDocumentRef = db.collection("Users").document(auth.currentUser?.uid.toString()).collection(categoryName).document("QuestionDocument")
        categoryDocumentRef
            .get()
            .addOnSuccessListener {
                categoryDocumentRef.set(newQuestion, SetOptions.merge())
                Log.d("print", "New question added to the database")
                _questionCreatedSuccessfully.value = true
            }
            .addOnFailureListener { exception ->
                Log.d("print", "Error getting documents.", exception)
            }
    }

    fun createMapOfQuestionAndAnswers (question: String, answers: ArrayList<String>){
        val mapOfQuestionAndAnswer = mutableMapOf<String,ArrayList<String>>()
        mapOfQuestionAndAnswer[question] = answers

        val newMap = QuestionObject(mapOfQuestionAndAnswer)

        addNewQuestionToDatabase(newMap)
    }

    fun resetQuestionCreatedSuccessfully () {
        _questionCreatedSuccessfully.value = false
    }
}
