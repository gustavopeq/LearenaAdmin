package gustavo.projects.learenaadmin.categories.newCategory

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

import gustavo.projects.learenaadmin.R
import gustavo.projects.learenaadmin.KeyboardUtil
import gustavo.projects.learenaadmin.databinding.NewCategoryFragmentBinding

class NewCategory : Fragment(), KeyboardUtil {

    private lateinit var viewModel: NewCategoryViewModel
    private lateinit var binding: NewCategoryFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(inflater, R.layout.new_category_fragment, container, false)

        viewModel = ViewModelProvider(this).get(NewCategoryViewModel::class.java)

        binding.createBtn.setOnClickListener{
            onCreateNewCategory()
            hideKeyboard()
        }

        setViewModelObservers()

        // CATEGORY NAME UPDATE
        binding.newCategoryNameTextField.editText?.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                // Nothing to implement
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // Nothing to implement
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                binding.createBtn.isEnabled = !s.isNullOrBlank()
                binding.newCategoryNameTextField.error = null
            }
        })

        return binding.root
    }

    private fun setViewModelObservers() {
        viewModel.categoryCreatedSuccessfully.observe(
            viewLifecycleOwner,
            Observer { categoryCreatedSuccessfully ->
                if (categoryCreatedSuccessfully) {
                    viewModel.resetCategoryCreatedSuccessfully()
                    returnToCategoryScreen()
                }
            })

        viewModel.categoryAlreadyExists.observe(
            viewLifecycleOwner,
            Observer { categoryAlreadyExists ->
                if (categoryAlreadyExists) {
                    nameAlreadyExistsError()
                    viewModel.resetCategoryAlreadyExists()
                }
            })
    }

    private fun onCreateNewCategory() {
        viewModel.addNewCategoryToDatabase(binding.newCategoryNameTextField.editText?.text.toString())
    }

    private fun returnToCategoryScreen() {

        val action = NewCategoryDirections.actionNewCategoryToCategories()

        action.newCategoryName = binding.newCategoryNameTextField.editText?.text.toString()

        findNavController().navigate(action)
    }

    private fun nameAlreadyExistsError() {
        binding.newCategoryNameTextField.error = "You already have a category with this name"
    }



}
