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
import androidx.recyclerview.widget.LinearLayoutManager
import gustavo.projects.learenaadmin.R
import gustavo.projects.learenaadmin.databinding.CategoriesFragmentBinding


class Categories : Fragment(), CategoryAdapter.OnItemClickListener, CategoryAdapter.OnMoreOptionClickListener, PopupMenu.OnMenuItemClickListener {

    private lateinit var binding: CategoriesFragmentBinding
    private lateinit var viewModel: CategoriesViewModel

    private var listOfRecyclerItem = ArrayList<CategoryItem>()
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

        viewModel.listOfCategoryItem.observe(viewLifecycleOwner, Observer {
            listOfRecyclerItem = it
            adapter.submitCategoryItemList(listOfRecyclerItem)
        })

        viewModel.itemRemovedSuccessfully.observe(viewLifecycleOwner, Observer { itemRemovedSuccessfully ->
            if(itemRemovedSuccessfully) resetItemRemovedFlag()
        })

        return binding.root
    }

    override fun onItemClick(position: Int) {
        Log.d("print", "Navigate to the CategoryDetail fragment -> " + listOfRecyclerItem[position].categoryName)
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
