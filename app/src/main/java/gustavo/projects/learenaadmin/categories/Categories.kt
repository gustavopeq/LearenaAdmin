package gustavo.projects.learenaadmin.categories

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.ImageView
import android.widget.PopupMenu
import androidx.activity.addCallback
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import gustavo.projects.learenaadmin.BasicDialogWindow
import gustavo.projects.learenaadmin.MainActivity
import gustavo.projects.learenaadmin.R
import gustavo.projects.learenaadmin.databinding.CategoriesFragmentBinding


class Categories : Fragment(), CategoryAdapter.OnItemClickListener, CategoryAdapter.OnMoreOptionClickListener, PopupMenu.OnMenuItemClickListener, BasicDialogWindow {

    private lateinit var binding: CategoriesFragmentBinding
    private lateinit var viewModel: CategoriesViewModel

    private lateinit var adapter: CategoryAdapter
    private var itemPositionToDelete = 0
    private lateinit var categoryNameToDelete: String


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        (activity as MainActivity).showActionBar()

        binding = DataBindingUtil.inflate(inflater,
            R.layout.categories_fragment, container, false)

        viewModel = ViewModelProvider(this).get(CategoriesViewModel::class.java)

        setRecyclerView()

        checkNewCategoryCreated(container)

        viewModel.getCategoriesFromDatabase()

        viewModel.listOfCategoryItem.observe(viewLifecycleOwner, Observer {
            adapter.submitCategoryItemList(it)
            binding.categoryLoadingIcon.visibility = View.GONE
            checkForNoneCategoryCreated()
        })

        viewModel.noCategoryFound.observe(viewLifecycleOwner, Observer {
            if (it) {
                binding.categoryLoadingIcon.visibility = View.GONE
                binding.noCategoryCreatedTextView.visibility = View.VISIBLE
            }
        })

        viewModel.itemRemovedSuccessfully.observe(viewLifecycleOwner, Observer { itemRemovedSuccessfully ->
            if(itemRemovedSuccessfully) resetItemRemovedFlag()
        })

        binding.newCategoryFab.setOnClickListener { findNavController().navigate(R.id.action_categories_to_newCategory) }

        setHasOptionsMenu(true)

        // Override onBackButtonPressed
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            onNavigateUpButtonPressed()
        }

        return binding.root
    }

    private fun checkNewCategoryCreated(container: ViewGroup?) {
        val newCategoryName = CategoriesArgs.fromBundle(requireArguments()).newCategoryName

        if (newCategoryName != null) {
            Snackbar.make(container!!, "Category $newCategoryName created!", Snackbar.LENGTH_LONG).show()
            arguments?.clear()
        }
    }

    override fun onItemClick(position: Int) {
        navigateToCategoryQuestions(adapter.getItem(position).categoryName)
    }

    override fun onMoreOptionClick(position: Int, categoryName: String, icon: ImageView) {
        showMoreOptionPopupMenu(icon)
        itemPositionToDelete = position
        categoryNameToDelete = categoryName
    }

    private fun setRecyclerView() {
        adapter = CategoryAdapter(this, this)
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(this.context)
        binding.recyclerView.setHasFixedSize(true)
    }

    private fun showMoreOptionPopupMenu(view: View) {
        val popupMenu = PopupMenu(this.context, view)
        popupMenu.setOnMenuItemClickListener(this)
        popupMenu.inflate(R.menu.popup_category_item_ellipsis)
        popupMenu.show()
    }

    override fun onMenuItemClick(item: MenuItem?): Boolean {
        if (item != null) {
            when(item.itemId) {
                R.id.deleteCategoryItem -> {
                    deleteCategoryItem()
                    return true
                }
            }
        }
        return false
    }

    private fun deleteCategoryItem() {
        viewModel.deleteCategoryFromDatabase(categoryNameToDelete)
        checkForNoneCategoryCreated()
    }

    private fun resetItemRemovedFlag() {
        viewModel.resetItemRemovedFlag()
    }

    private fun navigateToCategoryQuestions(categoryName: String) {
        findNavController().navigate(CategoriesDirections.actionCategoriesToAllQuestions(categoryName))
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            android.R.id.home -> onNavigateUpButtonPressed()
        }
        return true
    }

    private fun onNavigateUpButtonPressed() {
        onCreateDialog(this.requireActivity(), "Are you sure you want to disconnect from your account?", "Cancel", "Disconnect")

    }

    override fun onDialogPositiveBtn() {
        // Nothing to implement
    }

    override fun onDialogNegativeBtn() {
        findNavController().navigate(CategoriesDirections.actionCategoriesToLoginFragment())
    }

    private fun checkForNoneCategoryCreated() {
        if (viewModel.quantityOfCategories() == 0) {
            binding.noCategoryCreatedTextView.visibility = View.VISIBLE
        }
    }
}
