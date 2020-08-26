package gustavo.projects.learenaadmin.signup

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController

import gustavo.projects.learenaadmin.R
import gustavo.projects.learenaadmin.databinding.SignupFragmentBinding

class SignupFragment : Fragment() {

    companion object {
        private val MIN_PASSWORD_LENGTH = 6
    }

    private lateinit var binding: SignupFragmentBinding
    private lateinit var viewModel: SignupViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.signup_fragment, container, false)

        viewModel = ViewModelProvider(this).get(SignupViewModel::class.java)

        binding.createBtn.setOnClickListener { validateCreateAccountForm() }

        viewModel.userCreated.observe(viewLifecycleOwner, Observer<Boolean>{userCreated ->
            if(userCreated) userCreationSuccess() else userCreationFailed()
        })

        return binding.root
    }

    private fun validateCreateAccountForm() {
        if( !binding.passwordEditText.text.isNullOrBlank() && binding.passwordEditText.text.length >= MIN_PASSWORD_LENGTH
            && binding.passwordEditText.text.toString() == binding.confirmPwEditText.text.toString()
            && !binding.emailEditText.text.isNullOrBlank() && binding.emailEditText.text.contains("@", true)
            && !binding.firstNameEditText.text.isNullOrBlank() && !binding.lastNameEditText.text.isNullOrBlank()
        ) {
            viewModel.createUser(binding.firstNameEditText.text.toString(), binding.emailEditText.text.toString(), binding.passwordEditText.text.toString())
        }else{
            Toast.makeText(context, "Incorrect or missing field!", Toast.LENGTH_LONG).show()
        }
    }

    private fun userCreationSuccess() {
        Toast.makeText(context, "New user created!", Toast.LENGTH_LONG).show()
        findNavController().navigate(R.id.action_signupFragment_to_loginFragment)
    }

    private fun userCreationFailed() {
        Toast.makeText(context, "New user couldn't be created...", Toast.LENGTH_LONG).show()
    }

}
