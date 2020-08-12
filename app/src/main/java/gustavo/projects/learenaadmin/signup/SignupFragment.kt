package gustavo.projects.learenaadmin.signup

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider

import gustavo.projects.learenaadmin.R
import gustavo.projects.learenaadmin.databinding.SignupFragmentBinding

class SignupFragment : Fragment() {

    private lateinit var viewModel: SignupViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = DataBindingUtil.inflate<SignupFragmentBinding>(inflater, R.layout.signup_fragment, container, false)

        viewModel = ViewModelProvider(this).get(SignupViewModel::class.java)

        binding.createBtn.setOnClickListener { viewModel.createUser(binding.emailEditText.text.toString(), binding.passwordEditText.text.toString()) }

        return binding.root
    }

}
