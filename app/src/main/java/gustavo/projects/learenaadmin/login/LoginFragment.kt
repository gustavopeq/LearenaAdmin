package gustavo.projects.learenaadmin.login

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import gustavo.projects.learenaadmin.R
import gustavo.projects.learenaadmin.databinding.LoginFragmentBinding
import kotlinx.android.synthetic.main.login_fragment.*


class LoginFragment : Fragment() {

    private lateinit var viewModel: LoginViewModel


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding = DataBindingUtil.inflate<LoginFragmentBinding>(
            inflater,
            R.layout.login_fragment,
            container,
            false
        )

        viewModel = ViewModelProvider(this).get(LoginViewModel::class.java)

        binding.loginViewModel = viewModel

        // EMAIL TEXT FIELD UPDATE
        binding.emailEditText.addTextChangedListener(object : TextWatcher {
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
        binding.passwordEditText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                // Nothing to implement
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // Nothing to implement
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                binding.signinBtn.isEnabled = s?.count()!! >= 6 && !binding.emailEditText.text.isNullOrBlank()
            }
        })

        binding.signinBtn.setOnClickListener { viewModel.signIn(binding.emailEditText.text.toString(), binding.passwordEditText.text.toString()) }

        binding.newAccountBtn.setOnClickListener { findNavController().navigate(R.id.action_loginFragment_to_signupFragment) }

        return binding.root
    }

}
