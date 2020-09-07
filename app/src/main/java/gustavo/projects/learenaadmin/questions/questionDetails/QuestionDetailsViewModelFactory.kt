package gustavo.projects.learenaadmin.questions.questionDetails

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class QuestionDetailsViewModelFactory(private val categoryName: String, private val questionName: String) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(QuestionDetailsViewModel::class.java)) {
            return QuestionDetailsViewModel(categoryName, questionName) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}