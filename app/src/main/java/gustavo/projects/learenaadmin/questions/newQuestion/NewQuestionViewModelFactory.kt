package gustavo.projects.learenaadmin.questions.newQuestion

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class NewQuestionViewModelFactory(private val categoryName: String): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(NewQuestionViewModel::class.java)) {
            return NewQuestionViewModel(categoryName) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}