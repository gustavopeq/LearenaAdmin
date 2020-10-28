package gustavo.projects.learenaadmin.signup

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import gustavo.projects.learenaadmin.BasicDialogWindow
import gustavo.projects.learenaadmin.MainActivity

import gustavo.projects.learenaadmin.R
import gustavo.projects.learenaadmin.databinding.SignupFragmentBinding

class SignupFragment : Fragment(), BasicDialogWindow {

    companion object {
        private const val MIN_PASSWORD_LENGTH = 6
    }

    private lateinit var binding: SignupFragmentBinding
    private lateinit var viewModel: SignupViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        (activity as MainActivity).showActionBar()

        binding = DataBindingUtil.inflate(inflater, R.layout.signup_fragment, container, false)

        viewModel = ViewModelProvider(this).get(SignupViewModel::class.java)

        binding.createBtn.setOnClickListener { validateCreateAccountForm() }

        viewModel.userCreated.observe(viewLifecycleOwner, Observer<Boolean>{userCreated ->
            if(userCreated) userCreationSuccess() else userCreationFailed()
        })

        return binding.root
    }

    private fun validateCreateAccountForm() {
        binding.signupLoadingIcon.visibility = View.VISIBLE

        resetFieldsErrors()

        var isValid = true

        if (binding.emailEditText.editText!!.text.isNullOrBlank()) {
            binding.emailEditText.error = " "
            isValid = false
        } else if (!binding.emailEditText.editText!!.text.contains("@", true)) {
            binding.emailEditText.error = "Invalid Email"
            isValid = false
        }

        if (binding.passwordEditText.editText!!.text.toString() != binding.confirmPwEditText.editText!!.text.toString()) {
            binding.confirmPwEditText.error = "Password does not match"
            isValid = false
        }

        if (binding.firstNameEditText.editText!!.text.isNullOrBlank()) {
            binding.firstNameEditText.error = " "
            isValid = false
        }

        if (binding.lastNameEditText.editText!!.text.isNullOrBlank()) {
            binding.lastNameEditText.error = " "
            isValid = false
        }

        when {
            binding.passwordEditText.editText!!.text.isNullOrBlank() -> {
                binding.passwordEditText.error = " "
                isValid = false
            }
            binding.confirmPwEditText.editText!!.text.isNullOrBlank() -> {
                binding.confirmPwEditText.error = " "
                isValid = false
            }
            binding.passwordEditText.editText!!.text.length < MIN_PASSWORD_LENGTH -> {
                binding.passwordEditText.error = "Password too short"
                isValid = false
            }
        }

        if (isValid) {
            viewModel.createUser(binding.firstNameEditText.editText!!.text.toString(), binding.emailEditText.editText!!.text.toString(), binding.passwordEditText.editText!!.text.toString())
        }else {
            binding.signupLoadingIcon.visibility = View.GONE
        }
    }

    private fun resetFieldsErrors() {
        binding.firstNameEditText.error = null
        binding.lastNameEditText.error = null
        binding.emailEditText.error = null
        binding.passwordEditText.error = null
        binding.confirmPwEditText.error = null
    }

    private fun userCreationSuccess() {
        binding.signupLoadingIcon.visibility = View.GONE
        onCreateDialog(this.requireActivity(), "Your account was created!\nWe sent a confirmation email to you.", "Ok", "")
    }

    private fun userCreationFailed() {
        binding.emailEditText.error = "This email is already registered"
        binding.signupLoadingIcon.visibility = View.GONE
        Toast.makeText(context, "New user couldn't be created...", Toast.LENGTH_SHORT).show()
    }

    override fun onDialogPositiveBtn() {
        findNavController().navigate(R.id.action_signupFragment_to_loginFragment)
    }

    override fun onDialogNegativeBtn() {
        // No negative btn
    }

}
