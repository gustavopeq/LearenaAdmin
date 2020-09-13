package gustavo.projects.learenaadmin.questions.allQuestion.editCategory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import java.lang.IllegalArgumentException

class EditCategoryViewModelFactory(private val categoryName: String) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(EditCategoryViewModel::class.java)) {
            return EditCategoryViewModel(categoryName) as T
        }else {
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}