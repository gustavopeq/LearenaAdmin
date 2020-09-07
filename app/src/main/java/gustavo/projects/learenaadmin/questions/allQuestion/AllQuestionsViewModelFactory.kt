package gustavo.projects.learenaadmin.questions.allQuestion

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class AllQuestionsViewModelFactory(private val categoryName: String) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(AllQuestionsViewModel::class.java)) {
            return AllQuestionsViewModel(categoryName) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}