package gustavo.projects.learenaadmin.questions.newQuestion

import android.app.Activity
import android.app.AlertDialog
import android.content.DialogInterface
import android.text.Editable
import android.text.TextWatcher
import android.widget.CompoundButton
import androidx.appcompat.widget.SwitchCompat
import com.google.android.material.textfield.TextInputLayout

interface IQuestionForm {

    var newQuestionTextInput: TextInputLayout
    var answer1TextField: TextInputLayout
    var answer2TextField: TextInputLayout
    var answer3TextField: TextInputLayout
    var answer4TextField: TextInputLayout
    var answer1Correct: SwitchCompat
    var answer2Correct: SwitchCompat
    var answer3Correct: SwitchCompat
    var answer4Correct: SwitchCompat
    var listOfCorrectSwitches: ArrayList<SwitchCompat>
    var listOfAnswerTextField: ArrayList<TextInputLayout>

    fun validateAllParameters(activity: Activity) : Boolean {
        var allTextInputSet = true
        if (newQuestionTextInput.editText?.text.isNullOrEmpty()) {
            newQuestionTextInput.error = "You have to enter the question"
            allTextInputSet = false
        }

        if (answer1TextField.editText?.text.isNullOrEmpty()) {
            answer1TextField.error = "You have to enter an answer"
            allTextInputSet = false
        }

        if (answer2TextField.editText?.text.isNullOrEmpty()) {
            answer2TextField.error = "You have to enter an answer"
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
                onCreateDialogMissingCorrectAnswer(activity)
            }else {
                return true
            }
        }
        return false
    }

    private fun onCreateDialogMissingCorrectAnswer(activity: Activity) {
        val builder = AlertDialog.Builder(activity)
        builder.setMessage("You have to select one correct answer")
        builder.setPositiveButton("OK") { _: DialogInterface?, _: Int ->
            // Nothing to implement. Just close dialog
        }

        builder.show()
    }

    fun createListOfCorrectSwitch(): ArrayList<SwitchCompat> {
        var listOfSwitch = arrayListOf<SwitchCompat>()
        listOfSwitch.add(answer1Correct)
        listOfSwitch.add(answer2Correct)
        listOfSwitch.add(answer3Correct)
        listOfSwitch.add(answer4Correct)
        return listOfSwitch
    }

    fun createListOfAnswerTextField(): ArrayList<TextInputLayout> {
        var listOfAnswerTextField = arrayListOf<TextInputLayout>()
        listOfAnswerTextField.add(answer1TextField)
        listOfAnswerTextField.add(answer2TextField)
        listOfAnswerTextField.add(answer3TextField)
        listOfAnswerTextField.add(answer4TextField)
        return listOfAnswerTextField
    }

    fun setTextChangedListener() {

        newQuestionTextInput.editText?.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                // Nothing to implement
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // Nothing to implement
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                newQuestionTextInput.error = null
            }

        })

        answer1TextField.editText?.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                // Nothing to implement
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // Nothing to implement
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                answer1TextField.error = null
            }

        })

        answer2TextField.editText?.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                // Nothing to implement
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // Nothing to implement
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                answer2TextField.error = null
            }

        })

        answer3TextField.editText?.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                // Nothing to implement
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // Nothing to implement
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s.isNullOrBlank()) {
                    answer3Correct.isChecked = false
                }
                answer3Correct.isEnabled = !s.isNullOrBlank()
            }

        })

        answer4TextField.editText?.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                // Nothing to implement
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // Nothing to implement
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s.isNullOrBlank()) {
                    answer4Correct.isChecked = false
                }
                answer4Correct.isEnabled = !s.isNullOrBlank()
            }

        })
    }

    fun correctSwitchesListeners() {
        answer1Correct.setOnCheckedChangeListener { buttonView, isChecked ->
            uncheckOthersCorrectSwitch(isChecked, listOfCorrectSwitches, buttonView)
        }

        answer2Correct.setOnCheckedChangeListener { buttonView, isChecked ->
            uncheckOthersCorrectSwitch(isChecked, listOfCorrectSwitches, buttonView)
        }

        answer3Correct.setOnCheckedChangeListener { buttonView, isChecked ->
            uncheckOthersCorrectSwitch(isChecked, listOfCorrectSwitches, buttonView)
        }

        answer4Correct.setOnCheckedChangeListener { buttonView, isChecked ->
            uncheckOthersCorrectSwitch(isChecked, listOfCorrectSwitches, buttonView)
        }
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
}