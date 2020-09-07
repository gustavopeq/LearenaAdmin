package gustavo.projects.learenaadmin.questions.questionDetails


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SwitchCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.textfield.TextInputLayout

import gustavo.projects.learenaadmin.R
import gustavo.projects.learenaadmin.databinding.QuestionDetailsFragmentBinding
import gustavo.projects.learenaadmin.questions.newQuestion.IQuestionForm

class QuestionDetails : Fragment(), IQuestionForm {

    private lateinit var viewModel: QuestionDetailsViewModel
    private lateinit var binding: QuestionDetailsFragmentBinding

    override var newQuestionTextInput: TextInputLayout
        get() = binding.newQuestionTextInput
        set(@Suppress("UNUSED_PARAMETER") value) {}
    override var answer1TextField: TextInputLayout
        get() = binding.answer1TextField
        set(@Suppress("UNUSED_PARAMETER") value) {}
    override var answer2TextField: TextInputLayout
        get() = binding.answer2TextField
        set(@Suppress("UNUSED_PARAMETER") value) {}
    override var answer3TextField: TextInputLayout
        get() = binding.answer3TextField
        set(@Suppress("UNUSED_PARAMETER") value) {}
    override var answer4TextField: TextInputLayout
        get() = binding.answer4TextField
        set(@Suppress("UNUSED_PARAMETER") value) {}
    override var answer1Correct: SwitchCompat
        get() = binding.answer1Correct
        set(@Suppress("UNUSED_PARAMETER") value) {}
    override var answer2Correct: SwitchCompat
        get() = binding.answer2Correct
        set(@Suppress("UNUSED_PARAMETER") value) {}
    override var answer3Correct: SwitchCompat
        get() = binding.answer3Correct
        set(@Suppress("UNUSED_PARAMETER") value) {}
    override var answer4Correct: SwitchCompat
        get() = binding.answer4Correct
        set(@Suppress("UNUSED_PARAMETER") value) {}
    override var listOfCorrectSwitches: ArrayList<SwitchCompat>
        get() = createListOfCorrectSwitch()
        set(@Suppress("UNUSED_PARAMETER") value) {}

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(inflater, R.layout.question_details_fragment, container, false)
        viewModel = ViewModelProvider(this).get(QuestionDetailsViewModel::class.java)

        correctSwitchesListeners()
        setTextChangedListener()

        binding.saveBtn.setOnClickListener {
            if(validateAllParameters(this.requireActivity())) {

            }
        }

        return binding.root
    }

}
