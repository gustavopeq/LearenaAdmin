package gustavo.projects.learenaadmin.questions.newQuestion

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Toast
import androidx.appcompat.widget.SwitchCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputLayout
import gustavo.projects.learenaadmin.IKeyboardUtil
import gustavo.projects.learenaadmin.INetworkCheck

import gustavo.projects.learenaadmin.R
import gustavo.projects.learenaadmin.databinding.NewQuestionFragmentBinding

class NewQuestion : Fragment(), IQuestionForm, IKeyboardUtil, INetworkCheck {

    private lateinit var viewModel: NewQuestionViewModel
    private lateinit var binding: NewQuestionFragmentBinding
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

        binding = DataBindingUtil.inflate(inflater, R.layout.new_question_fragment, container, false)

        categoryName = NewQuestionArgs.fromBundle(requireArguments()).categoryName

        val viewModelFactory = NewQuestionViewModelFactory(categoryName)

        viewModel = ViewModelProvider(this, viewModelFactory).get(NewQuestionViewModel::class.java)

        correctSwitchesListeners()
        setTextChangedListener()

        viewModel.questionCreatedSuccessfully.observe(viewLifecycleOwner, Observer {
            if(it) confirmNewQuestionCreated()
        })

        setHasOptionsMenu(true)

        return binding.root
    }

    private fun onCreateNewQuestion(){
        if(isNetworkAvailable(requireContext())) {
            val question = newQuestionTextInput.editText?.text.toString()
            val answers = arrayListOf<String>()
            answers.add(answer1TextField.editText?.text.toString())
            answers.add(answer2TextField.editText?.text.toString())
            if (!answer3TextField.editText?.text.isNullOrEmpty()) {
                answers.add(answer3TextField.editText?.text.toString())
            }
            if (!answer4TextField.editText?.text.isNullOrEmpty()) {
                answers.add(answer4TextField.editText?.text.toString())
            }

            setCorrectAnswerAsFirst(answers)

            viewModel.createMapOfQuestionAndAnswers(question, answers)
        }else {
            Snackbar.make(requireView(), "No Internet connection", 3000).show()
        }
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

    private fun confirmNewQuestionCreated() {
        viewModel.resetQuestionCreatedSuccessfully()
        Toast.makeText(context, "New question created", Toast.LENGTH_SHORT).show()
        findNavController().navigate(NewQuestionDirections.actionNewQuestionToAllQuestions(categoryName))
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.new_question_save, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.newQuestionSaveIcon -> onSaveIconClick()
            android.R.id.home -> onBackArrowClick()
        }
        return true
    }

    private fun onSaveIconClick() {
        if(validateAllParameters(this.requireActivity())) {
            onCreateNewQuestion()
        }
        hideKeyboard()
    }

    private fun onBackArrowClick() {
        hideKeyboard()
        findNavController().navigate(NewQuestionDirections.actionNewQuestionToAllQuestions(categoryName))
    }


}
