package gustavo.projects.learenaadmin.categories

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.PopupMenu
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import gustavo.projects.learenaadmin.R
import gustavo.projects.learenaadmin.databinding.CategoriesFragmentBinding


class Categories : Fragment(), CategoryAdapter.OnItemClickListener, CategoryAdapter.OnMoreOptionClickListener, PopupMenu.OnMenuItemClickListener {

    private lateinit var binding: CategoriesFragmentBinding
    private lateinit var viewModel: CategoriesViewModel

    private lateinit var adapter: CategoryAdapter
    private var itemPositionToDelete = 0
    private lateinit var categoryNameToDelete: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(inflater,
            R.layout.categories_fragment, container, false)

        viewModel = ViewModelProvider(this).get(CategoriesViewModel::class.java)

        setRecyclerView()

        checkNewCategoryCreated(container)

        viewModel.getCategoriesFromDatabase()

        viewModel.listOfCategoryItem.observe(viewLifecycleOwner, Observer {
            adapter.submitCategoryItemList(it)
        })

        viewModel.itemRemovedSuccessfully.observe(viewLifecycleOwner, Observer { itemRemovedSuccessfully ->
            if(itemRemovedSuccessfully) resetItemRemovedFlag()
        })

        binding.newCategoryFab.setOnClickListener { findNavController().navigate(R.id.action_categories_to_newCategory) }

        return binding.root
    }

    private fun checkNewCategoryCreated(container: ViewGroup?) {
        val newCategoryName = CategoriesArgs.fromBundle(requireArguments()).newCategoryName

        if (newCategoryName != null) {
            Snackbar.make(container!!, "Category $newCategoryName created!", Snackbar.LENGTH_LONG)
                .setAction("Undo") {
                    viewModel.deleteCategoyFromDatabase(newCategoryName)
                }.show()
            arguments?.clear()
        }
    }

    override fun onItemClick(position: Int) {
        findNavController().navigate(R.id.action_categories_to_allQuestions)
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
        viewModel.deleteCategoyFromDatabase(categoryNameToDelete)
    }

    private fun resetItemRemovedFlag() {
        viewModel.resetItemRemovedFlag()
    }
}
