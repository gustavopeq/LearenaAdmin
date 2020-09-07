package gustavo.projects.learenaadmin.questions.newQuestion

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
import gustavo.projects.learenaadmin.databinding.NewQuestionFragmentBinding
import io.grpc.NameResolver

class NewQuestion : Fragment(), IQuestionForm {

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

        binding.createNewQuestionBtn.setOnClickListener {
            if(validateAllParameters(this.requireActivity())) {
                onCreateNewQuestion()
            }
        }

        viewModel.questionCreatedSuccessfully.observe(viewLifecycleOwner, Observer {
            if(it) confirmNewQuestionCreated()
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
        viewModel.createMapOfQuestionAndAnswers(question, answers)
    }

    private fun confirmNewQuestionCreated() {
        viewModel.resetQuestionCreatedSuccessfully()
        Toast.makeText(context, "New question created", Toast.LENGTH_SHORT).show()
        findNavController().navigate(NewQuestionDirections.actionNewQuestionToAllQuestions(categoryName))
    }


}
