package gustavo.projects.learenaadmin.questions.newQuestion

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import android.widget.Toast
import androidx.appcompat.widget.SwitchCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController

import gustavo.projects.learenaadmin.R
import gustavo.projects.learenaadmin.databinding.NewQuestionFragmentBinding

class NewQuestion : Fragment() {

    private lateinit var viewModel: NewQuestionViewModel
    private lateinit var binding: NewQuestionFragmentBinding
    private lateinit var listOfCorrectSwitches: ArrayList<SwitchCompat>
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(inflater, R.layout.new_question_fragment, container, false)
        viewModel = ViewModelProvider(this).get(NewQuestionViewModel::class.java)

        listOfCorrectSwitches = createListOfCorrectSwitch()

        correctSwitchesListeners()

        setTextChangedListener()

        binding.createNewQuestionBtn.setOnClickListener {
            validateAllParametersToCreateNewQuestion()
        }

        viewModel.questionCreatedSuccessfully.observe(viewLifecycleOwner, Observer {
            if(it) confirmNewQuestionCreated()
        })

        return binding.root
    }

    private fun validateAllParametersToCreateNewQuestion() {
        var allTextInputSet = true
        if (binding.newQuestionTextInput.editText?.text.isNullOrEmpty()) {
            binding.newQuestionTextInput.error = "You have to enter the question"
            allTextInputSet = false
        }

        if (binding.answer1TextField.editText?.text.isNullOrEmpty()) {
            binding.answer1TextField.error = "You have to enter an answer"
            allTextInputSet = false
        }

        if (binding.answer2TextField.editText?.text.isNullOrEmpty()) {
            binding.answer2TextField.error = "You have to enter an answer"
            allTextInputSet = false
        }

        if (allTextInputSet) {
            var atLeastOneCorrectChecked = false
            for (correctSwitch in listOfCorrectSwitches) {
                if (correctSwitch.isChecked) {
                    atLeastOneCorrectChecked = true
                    break
                }
            }

            if (!atLeastOneCorrectChecked) {
                onCreateDialogMissingCorrectAnswer()
            }else {
                val question = binding.newQuestionTextInput.editText?.text.toString()
                val answers = arrayListOf<String>()
                answers.add(binding.answer1TextField.editText?.text.toString())
                answers.add(binding.answer2TextField.editText?.text.toString())
                if(!binding.answer3TextField.editText?.text.isNullOrEmpty()) {
                    answers.add(binding.answer3TextField.editText?.text.toString())
                }
                if(!binding.answer4TextField.editText?.text.isNullOrEmpty()) {
                    answers.add(binding.answer4TextField.editText?.text.toString())
                }
                viewModel.createMapOfQuestionAndAnswers(question, answers)
            }

        }
    }

    private fun onCreateDialogMissingCorrectAnswer() {
        val builder = AlertDialog.Builder(this.activity)
        builder.setMessage("You have to select one correct answer")
        builder.setPositiveButton("OK") { dialog: DialogInterface?, which: Int ->
            // Nothing to implement. Just close dialog
        }

        builder.show()
    }

    private fun setTextChangedListener() {

        binding.newQuestionTextInput.editText?.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                // Nothing to implement
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // Nothing to implement
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                binding.newQuestionTextInput.error = null
            }

        })

        binding.answer1TextField.editText?.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                // Nothing to implement
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // Nothing to implement
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                binding.answer1TextField.error = null
            }

        })

        binding.answer2TextField.editText?.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                // Nothing to implement
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // Nothing to implement
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                binding.answer2TextField.error = null
            }

        })

        binding.answer3TextField.editText?.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                // Nothing to implement
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // Nothing to implement
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s.isNullOrBlank()) {
                    binding.answer3Correct.isChecked = false
                }
                binding.answer3Correct.isEnabled = !s.isNullOrBlank()
            }

        })

        binding.answer4TextField.editText?.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                // Nothing to implement
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // Nothing to implement
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s.isNullOrBlank()) {
                    binding.answer4Correct.isChecked = false
                }
                binding.answer4Correct.isEnabled = !s.isNullOrBlank()
            }

        })
    }

    private fun correctSwitchesListeners() {
        binding.answer1Correct.setOnCheckedChangeListener { buttonView, isChecked ->
            uncheckOthersCorrectSwitch(isChecked, listOfCorrectSwitches, buttonView)
        }

        binding.answer2Correct.setOnCheckedChangeListener { buttonView, isChecked ->
            uncheckOthersCorrectSwitch(isChecked, listOfCorrectSwitches, buttonView)
        }

        binding.answer3Correct.setOnCheckedChangeListener { buttonView, isChecked ->
            uncheckOthersCorrectSwitch(isChecked, listOfCorrectSwitches, buttonView)
        }

        binding.answer4Correct.setOnCheckedChangeListener { buttonView, isChecked ->
            uncheckOthersCorrectSwitch(isChecked, listOfCorrectSwitches, buttonView)
        }
    }

    private fun createListOfCorrectSwitch(): ArrayList<SwitchCompat> {
        var listOfSwitch = arrayListOf<SwitchCompat>()
        listOfSwitch.add(binding.answer1Correct)
        listOfSwitch.add(binding.answer2Correct)
        listOfSwitch.add(binding.answer3Correct)
        listOfSwitch.add(binding.answer4Correct)
        return listOfSwitch
    }

    private fun uncheckOthersCorrectSwitch(
        isChecked: Boolean,
        listOfSwitch: ArrayList<SwitchCompat>,
        buttonView: CompoundButton?
    ) {
        if (isChecked) {
            for (switch in listOfSwitch) {
                if (switch != buttonView) {
                    switch.isChecked = false
                }
            }
        }
    }

    private fun confirmNewQuestionCreated() {
        viewModel.resetQuestionCreatedSuccessfully()
        Toast.makeText(context, "New question created", Toast.LENGTH_SHORT).show()
        findNavController().navigate(R.id.action_newQuestion_to_allQuestions)
    }

}
