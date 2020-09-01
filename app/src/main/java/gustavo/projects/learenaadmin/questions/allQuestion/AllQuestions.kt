package gustavo.projects.learenaadmin.questions.allQuestion

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import gustavo.projects.learenaadmin.R

class AllQuestions : Fragment() {

    private lateinit var viewModel: AllQuestionsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.all_questions_fragment, container, false)
    }

}
