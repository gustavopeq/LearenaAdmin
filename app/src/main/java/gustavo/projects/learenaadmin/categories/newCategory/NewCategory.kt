package gustavo.projects.learenaadmin.categories.newCategory

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import gustavo.projects.learenaadmin.R

class NewCategory : Fragment() {

    private lateinit var viewModel: NewCategoryViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.new_category_fragment, container, false)
    }

}
