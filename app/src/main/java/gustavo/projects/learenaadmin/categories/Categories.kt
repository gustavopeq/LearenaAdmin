package gustavo.projects.learenaadmin.categories

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import gustavo.projects.learenaadmin.R
import gustavo.projects.learenaadmin.databinding.CategoriesFragmentBinding


class Categories : Fragment(), CategoryAdapter.OnItemClickListener {

    private lateinit var binding: CategoriesFragmentBinding
    private lateinit var viewModel: CategoriesViewModel

    private var listOfRecyclerItem = ArrayList<CategoryItem>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(inflater,
            R.layout.categories_fragment, container, false)

        viewModel = ViewModelProvider(this).get(CategoriesViewModel::class.java)

        viewModel.listOfCategoryItem.observe(viewLifecycleOwner, Observer<ArrayList<CategoryItem>> {
            listOfRecyclerItem = it
            setRecyclerView()
        })

        return binding.root
    }

    override fun onItemClick(position: Int) {
        Log.d("print", "Navigate to the CategoryDetail fragment -> " + listOfRecyclerItem[position].categoryName)
    }

    private fun setRecyclerView() {
        binding.recyclerView.adapter = CategoryAdapter(listOfRecyclerItem, this)
        binding.recyclerView.layoutManager = LinearLayoutManager(this.context)
        binding.recyclerView.setHasFixedSize(true)
    }
}
