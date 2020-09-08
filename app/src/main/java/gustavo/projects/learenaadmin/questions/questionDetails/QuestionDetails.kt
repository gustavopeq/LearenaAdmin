package gustavo.projects.learenaadmin.questions.questionDetails


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.SwitchCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.material.textfield.TextInputLayout

import gustavo.projects.learenaadmin.R
import gustavo.projects.learenaadmin.databinding.QuestionDetailsFragmentBinding
import gustavo.projects.learenaadmin.questions.newQuestion.IQuestionForm
import gustavo.projects.learenaadmin.questions.newQuestion.NewQuestionDirections

class QuestionDetails : Fragment(), IQuestionForm {

    private lateinit var viewModel: QuestionDetailsViewModel
    private lateinit var binding: QuestionDetailsFragmentBinding
    private lateinit var categoryName: String

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
    override var listOfAnswerTextField: ArrayList<TextInputLayout>
        get() = createListOfAnswerTextField()
        set(@Suppress("UNUSED_PARAMETER") value) {}

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(inflater, R.layout.question_details_fragment, container, false)

        categoryName = QuestionDetailsArgs.fromBundle(requireArguments()).categoryName
        val questionName = QuestionDetailsArgs.fromBundle(requireArguments()).questionName

        val viewModelFactory = QuestionDetailsViewModelFactory(categoryName, questionName)

        viewModel = ViewModelProvider(this, viewModelFactory).get(QuestionDetailsViewModel::class.java)

        correctSwitchesListeners()
        setTextChangedListener()

        binding.saveBtn.setOnClickListener {
            if(validateAllParameters(this.requireActivity())) {
                onCreateNewQuestion()
            }
        }

        newQuestionTextInput.editText?.setText(questionName)

        viewModel.listOfAnswers.observe(viewLifecycleOwner, Observer { listOfAnswers ->
            for((index, answer) in listOfAnswers.withIndex()) {
                listOfAnswerTextField[index].editText?.setText(answer)
                if(!listOfAnswerTextField[index].isEnabled) listOfAnswerTextField[index].isEnabled = true
            }
        })

        return binding.root
    }

    private fun onCreateNewQuestion(){
        val question = newQuestionTextInput.editText?.text.toString()
        val answers = arrayListOf<String>()
        answers.add(answer1TextField.editText?.text.toString())
        answers.add(answer2TextField.editText?.text.toString())
        if(!answer3TextField.editText?.text.isNullOrEmpty()) {
            answers.add(answer3TextField.editText?.text.toString())
        }
        if(!answer4TextField.editText?.text.isNullOrEmpty()) {
            answers.add(answer4TextField.editText?.text.toString())
        }
        viewModel.updateMapOfQuestion(question, answers)
    }

    private fun confirmQuestionUpdated() {
        Toast.makeText(context, "Question Updated", Toast.LENGTH_SHORT).show()
    }

}
