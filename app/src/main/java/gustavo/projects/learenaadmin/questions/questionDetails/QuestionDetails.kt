package gustavo.projects.learenaadmin.questions.questionDetails

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider

import gustavo.projects.learenaadmin.R
import gustavo.projects.learenaadmin.databinding.QuestionDetailsFragmentBinding

class QuestionDetails : Fragment() {

    private lateinit var viewModel: QuestionDetailsViewModel
    private lateinit var binding: QuestionDetailsFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(inflater, R.layout.question_details_fragment, container, false)
        viewModel = ViewModelProvider(this).get(QuestionDetailsViewModel::class.java)

        return binding.root
    }

}
