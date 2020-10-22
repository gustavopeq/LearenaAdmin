package gustavo.projects.learenaadmin.questions.allQuestion.editCategory

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.ImageView
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import gustavo.projects.learenaadmin.IKeyboardUtil

import gustavo.projects.learenaadmin.R
import gustavo.projects.learenaadmin.categories.newCategory.INewCategoryForm
import gustavo.projects.learenaadmin.databinding.EditCategoryFragmentBinding

class EditCategory : Fragment(), INewCategoryForm, IKeyboardUtil {

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

    private lateinit var viewModel: EditCategoryViewModel
    private lateinit var binding: EditCategoryFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.edit_category_fragment, container, false)

        val categoryName = EditCategoryArgs.fromBundle(requireArguments()).categoryName

        val viewModelFactory = EditCategoryViewModelFactory(categoryName)

        viewModel = ViewModelProvider(this, viewModelFactory).get(EditCategoryViewModel::class.java)

        binding.editCategoryName.editText?.setText(categoryName)

        viewModel.categoryDescription.observe(viewLifecycleOwner, Observer {
            binding.editCategoryDescription.editText?.setText(it)
        })

        viewModel.categoryStarLevel.observe(viewLifecycleOwner, Observer{
            displayStarLevel(it)
            starLevel = it
        })

        viewModel.categoryAlreadyExists.observe(viewLifecycleOwner, Observer {
            nameAlreadyExistsError()
        })

        viewModel.categoryEditedSuccessfully.observe(viewLifecycleOwner, Observer {
            confirmSaveChanges()
        })

        setOnClickStarListeners()

        setHasOptionsMenu(true)

        return binding.root
    }

    private fun nameAlreadyExistsError() {
        binding.editCategoryName.error = "You already have a category with this name"
    }

    private fun validateFields() {
        if(binding.editCategoryName.editText?.text.toString() == viewModel.categoryName) {
            viewModel.updateCategoryInformation(binding.editCategoryDescription.editText?.text.toString(), starLevel)
        }else {
            viewModel.isChangingName = true
            viewModel.updateCategoryInformation(binding.editCategoryDescription.editText?.text.toString(), starLevel)
            viewModel.updateCategoryName(binding.editCategoryName.editText?.text.toString())
        }
        hideKeyboard()
    }

    private fun confirmSaveChanges() {
        Toast.makeText(this.context, "Category edited successfully", Toast.LENGTH_SHORT).show()
        findNavController().navigate(EditCategoryDirections.actionEditCategoryToAllQuestions(viewModel.categoryName))
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.edit_category_save, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.editCategorySaveIcon -> onSaveIconClick()
            android.R.id.home -> onBackArrowClick()
        }
        return true
    }

    private fun onSaveIconClick() {
        validateFields()
    }

    private fun onBackArrowClick() {
        hideKeyboard()
        findNavController().navigate(EditCategoryDirections.actionEditCategoryToAllQuestions(viewModel.categoryName))
    }

}
