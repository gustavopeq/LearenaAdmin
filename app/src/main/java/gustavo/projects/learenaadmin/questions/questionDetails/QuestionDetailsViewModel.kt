package gustavo.projects.learenaadmin.questions.questionDetails

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
import gustavo.projects.learenaadmin.questions.allQuestion.QuestionObject

class QuestionDetailsViewModel(private val categoryName: String, private var questionName: String) : ViewModel() {

    private var auth: FirebaseAuth = Firebase.auth
    private val db = Firebase.firestore
    private lateinit var categoryDocumentRef: DocumentReference

    private var mapOfQuestions = mutableMapOf<String, ArrayList<String>>()

    private val _listOfAnswers = MutableLiveData<ArrayList<String>>()
    val listOfAnswers: LiveData<ArrayList<String>>
        get() = _listOfAnswers

    init {
        getQuestionsFromDatabase()
    }

    private fun getQuestionsFromDatabase() {
        categoryDocumentRef = db.collection("Users").document(auth.currentUser?.uid.toString()).collection(categoryName).document("QuestionDocument")
        categoryDocumentRef
            .get()
            .addOnSuccessListener { document ->
                if(document.exists()) {
                    if (document.data!!.isNotEmpty()) {
                        val questions = document.toObject<QuestionObject>()

                        if (questions != null) {
                            mapOfQuestions = questions.mapOfQuestions?.toMutableMap()!!
                            Log.d("print", "DB accessed and questions loaded")
                            getQuestionsDetails()
                        }
                    } else {
                        Log.d("print", "This user haven't created any question yet")
                    }
                }else {
                    Log.d("print", "This user haven't created any question yet")
                }
            }
            .addOnFailureListener { exception ->
                Log.d("print", "Error getting documents.", exception)
            }
    }

    private fun getQuestionsDetails() {
        _listOfAnswers.value = mapOfQuestions[questionName]
    }

    // IF QUESTION NAME IS CHANGED, THE OLD ONE IS DELETED AND A NEW ONE IS CREATED. IF ONLY THE ANSWERS WAS CHANGED, THEN THE MAP IS UPDATED WITH THE NEW ANSWERS
    private fun updateQuestionInDatabase(newQuestionObject: QuestionObject) {
        var newQuestionName = ""
        for(key in newQuestionObject.mapOfQuestions!!.keys) {
            newQuestionName = key
        }

        categoryDocumentRef = db.collection("Users").document(auth.currentUser?.uid.toString()).collection(categoryName).document("QuestionDocument")
        categoryDocumentRef
            .get()
            .addOnSuccessListener {
                if(questionName != newQuestionName){
                    categoryDocumentRef.update("mapOfQuestions.$questionName", FieldValue.delete())
                    questionName = newQuestionName
                }
                categoryDocumentRef.set(newQuestionObject, SetOptions.merge())
            }
            .addOnFailureListener { exception ->
                Log.d("print", "Error getting documents.", exception)
            }
    }

    fun updateMapOfQuestion(question: String, answers: ArrayList<String>) {
        val mapOfQuestionAndAnswer = mutableMapOf<String, ArrayList<String>>()
        mapOfQuestionAndAnswer[question] = answers

        val newQuestionObject = QuestionObject(mapOfQuestionAndAnswer)

        updateQuestionInDatabase(newQuestionObject)
    }

}
