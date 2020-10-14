package gustavo.projects.learenaadmin.login

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import gustavo.projects.learenaadmin.MainActivity
import gustavo.projects.learenaadmin.R
import gustavo.projects.learenaadmin.databinding.LoginFragmentBinding


class LoginFragment : Fragment() {

    private lateinit var binding: LoginFragmentBinding
    private lateinit var viewModel: LoginViewModel


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        (activity as MainActivity).supportActionBar?.hide()

        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.login_fragment,
            container,
            false
        )

        viewModel = ViewModelProvider(this).get(LoginViewModel::class.java)

        binding.loginViewModel = viewModel

        // EMAIL TEXT FIELD UPDATE
        binding.emailEditText.editText?.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                // Nothing to implement
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // Nothing to implement
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                binding.signinBtn.isEnabled = !s.isNullOrBlank()
            }
        })

        // PASSWORD TEXT FIELD UPDATE
        binding.passwordEditText.editText?.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                // Nothing to implement
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // Nothing to implement
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                binding.signinBtn.isEnabled = s?.count()!! >= 6 && !binding.emailEditText.editText?.text.isNullOrBlank()
            }
        })

        binding.signinBtn.setOnClickListener {
            onSignBtnClick()
        }

        binding.newAccountBtn.setOnClickListener { navigateToSignupScreen() }

        viewModel.loginSuccessful.observe(viewLifecycleOwner, Observer{loginSuccessful ->
            if(loginSuccessful) loadCategoriesFragment()
        })

        viewModel.loginFailed.observe(viewLifecycleOwner, Observer {
            if (it) {
                onLoginFailedFeedback()
            }
        })

        return binding.root
    }

    private fun onSignBtnClick() {
        resetInputFieldErrors()
        viewModel.signIn(binding.emailEditText.editText?.text.toString(), binding.passwordEditText.editText?.text.toString())
    }

    private fun loadCategoriesFragment() {
        viewModel.onResetLoginSuccessful()
        findNavController().navigate(R.id.action_loginFragment_to_categories)
    }

    private fun navigateToSignupScreen() {
        findNavController().navigate(R.id.action_loginFragment_to_signupFragment)
    }

    private fun resetInputFieldErrors() {
        binding.emailEditText.error = null
        binding.passwordEditText.error = null
    }

    private fun onLoginFailedFeedback() {
        binding.emailEditText.error = " "
        binding.passwordEditText.error = "Email or password incorrect"
    }

}
