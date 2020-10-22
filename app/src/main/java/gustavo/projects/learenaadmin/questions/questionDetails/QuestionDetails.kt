package gustavo.projects.learenaadmin.questions.questionDetails


import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Toast
import androidx.appcompat.widget.SwitchCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.material.textfield.TextInputLayout
import gustavo.projects.learenaadmin.BasicDialogWindow
import gustavo.projects.learenaadmin.IKeyboardUtil

import gustavo.projects.learenaadmin.R
import gustavo.projects.learenaadmin.databinding.QuestionDetailsFragmentBinding
import gustavo.projects.learenaadmin.questions.newQuestion.IQuestionForm

class QuestionDetails : Fragment(), IQuestionForm, BasicDialogWindow, IKeyboardUtil {

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

        newQuestionTextInput.editText?.setText(questionName)

        viewModel.listOfAnswers.observe(viewLifecycleOwner, Observer { listOfAnswers ->
            fillAnswerTextFields(listOfAnswers)
        })

        viewModel.onQuestionUpdatedSuccessfully.observe(viewLifecycleOwner, Observer {
            if(it) confirmQuestionUpdated()
        })

        viewModel.onQuestionDeletedSuccessfully.observe(viewLifecycleOwner, Observer {
            if(it) confirmQuestionDeleted()
        })

        setHasOptionsMenu(true)

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {

        inflater.inflate(R.menu.question_detail_menu,menu)

        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.questionDetailSaveIcon -> onSaveIconClick()
            R.id.deleteQuestionIcon -> onDeleteQuestionIconClick()
            android.R.id.home -> onBackArrowClick()
        }
        return true
    }

    private fun onDeleteQuestionIconClick() {
        hideKeyboard()
        onCreateDialog(requireActivity(),"Are you sure you want to delete this question?", "Delete", "Cancel")
    }

    override fun onDialogPositiveBtn() {
       viewModel.deleteQuestionFromDatabase()
    }

    override fun onDialogNegativeBtn() {
        // Nothing to implement, just close dialog window
    }

    private fun fillAnswerTextFields(listOfAnswers: ArrayList<String>) {
        for ((index, answer) in listOfAnswers.withIndex()) {
            listOfAnswerTextField[index].editText?.setText(answer)
            if (!listOfAnswerTextField[index].isEnabled) listOfAnswerTextField[index].isEnabled = true
        }
    }

    private fun onUpdateQuestion(){
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

        setCorrectAnswerAsFirst(answers)

        viewModel.updateMapOfQuestion(question, answers)
    }

    // TAKE THE ANSWERS ARRAY, AND PUT THE CORRECT ANSWER IN THE FIRST POSITION
    private fun setCorrectAnswerAsFirst(answers: ArrayList<String>) : ArrayList<String> {
        if(listOfCorrectSwitches[0].isChecked) return answers

        for((index, answer) in answers.withIndex()) {
            if(listOfCorrectSwitches[index].isChecked) {
                answers[index] = answers[0]
                answers[0] = answer
                return answers
            }
        }

        return answers
    }

    private fun confirmQuestionUpdated() {
        viewModel.resetQuestionUpdatedSuccessfully()
        Toast.makeText(context, "Question Updated", Toast.LENGTH_SHORT).show()
        findNavController().navigate(QuestionDetailsDirections.actionQuestionDetailsToAllQuestions(categoryName))
    }

    private fun confirmQuestionDeleted() {
        viewModel.resetQuestionDeletedSuccessfully()
        Toast.makeText(context, "Question Deleted", Toast.LENGTH_SHORT).show()
        findNavController().navigate(QuestionDetailsDirections.actionQuestionDetailsToAllQuestions(categoryName))
    }

    private fun onBackArrowClick() {
        hideKeyboard()
        findNavController().navigate(QuestionDetailsDirections.actionQuestionDetailsToAllQuestions(categoryName))
    }

    private fun onSaveIconClick() {
        if(validateAllParameters(this.requireActivity())) {
            onUpdateQuestion()
        }
        hideKeyboard()
    }

}
