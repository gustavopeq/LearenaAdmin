package gustavo.projects.learenaadmin.questions.allQuestion

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class AllQuestionsViewModel : ViewModel() {

    private val _listOfQuestionItem = MutableLiveData<ArrayList<QuestionItem>>()
    val listOfCategoryItem: LiveData<ArrayList<QuestionItem>>
        get() = _listOfQuestionItem

    init {
        var list = arrayListOf<QuestionItem>(QuestionItem("Question1"), QuestionItem("Question 2 dlsasladsk asdaskas,dl aslfasfaf sfklasfk"))

        _listOfQuestionItem.value = list
    }

}
