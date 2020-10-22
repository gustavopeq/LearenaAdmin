package gustavo.projects.learenaadmin.categories.newCategory

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.ImageView
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController

import gustavo.projects.learenaadmin.R
import gustavo.projects.learenaadmin.IKeyboardUtil
import gustavo.projects.learenaadmin.databinding.NewCategoryFragmentBinding

class NewCategory : Fragment(), IKeyboardUtil, INewCategoryForm {

    override var starImg1: ImageView
        get() = binding.starImg1
        set(value) {}
    override var starImg2: ImageView
        get() = binding.starImg2
        set(value) {}
    override var starImg3: ImageView
        get() = binding.starImg3
        set(value) {}
    override var starImg4: ImageView
        get() = binding.starImg4
        set(value) {}
    override var starImg5: ImageView
        get() = binding.starImg5
        set(value) {}
    override var listOfStarsImg: ArrayList<ImageView>
        get() = createArrayOfStarsImg()
        set(value) {}
    override var starLevel: Int = 1

    private lateinit var viewModel: NewCategoryViewModel
    private lateinit var binding: NewCategoryFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(inflater, R.layout.new_category_fragment, container, false)

        viewModel = ViewModelProvider(this).get(NewCategoryViewModel::class.java)

        setOnClickStarListeners()

        setViewModelObservers()

        setTextFieldListeners()

        setHasOptionsMenu(true)

        return binding.root
    }

    private fun setTextFieldListeners() {
        // CATEGORY NAME UPDATE
        binding.newCategoryNameTextField.editText?.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                // Nothing to implement
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // Nothing to implement
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                binding.newCategoryNameTextField.error = null
            }
        })

        // DESCRIPTION TEXT UPDATE
        binding.newCategoryDescription.editText?.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                // Nothing to implement
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // Nothing to implement
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                binding.newCategoryDescription.error = null
            }
        })
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

    private fun validateFields() : Boolean {
        if (binding.newCategoryNameTextField.editText?.text.isNullOrEmpty()){
            binding.newCategoryNameTextField.error = "Please enter a category name"
            return false
        }

        if(binding.newCategoryDescription.editText?.text.isNullOrEmpty()) {
            binding.newCategoryDescription.error = "Please enter a description"
            return false
        }
        return true
    }

    private fun onCreateNewCategory() {
        val categoryName = binding.newCategoryNameTextField.editText?.text.toString()
        val categoryDescription = binding.newCategoryDescription.editText?.text.toString()
        viewModel.addNewCategoryToDatabase(categoryName, categoryDescription, starLevel)
    }

    private fun returnToCategoryScreen() {

        val action = NewCategoryDirections.actionNewCategoryToCategories()

        action.newCategoryName = binding.newCategoryNameTextField.editText?.text.toString()

        findNavController().navigate(action)
    }

    private fun nameAlreadyExistsError() {
        binding.newCategoryNameTextField.error = "You already have a category with this name"
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.new_category_save, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.newCategorySaveIcon -> onSaveIconClick()
            android.R.id.home -> onBackArrowClick()
        }
        return true
    }

    private fun onSaveIconClick() {
        if(validateFields()) {
            onCreateNewCategory()
        }
        hideKeyboard()
    }

    private fun onBackArrowClick() {
        hideKeyboard()
        findNavController().navigate(NewCategoryDirections.actionNewCategoryToCategories())
    }

}
