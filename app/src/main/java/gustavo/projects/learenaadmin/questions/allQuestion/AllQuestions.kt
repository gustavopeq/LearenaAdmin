package gustavo.projects.learenaadmin.questions.allQuestion

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager

import gustavo.projects.learenaadmin.R
import gustavo.projects.learenaadmin.databinding.AllQuestionsFragmentBinding

class AllQuestions : Fragment() {

    private lateinit var viewModel: AllQuestionsViewModel
    private lateinit var binding: AllQuestionsFragmentBinding

    private lateinit var adapter: AllQuestionsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.all_questions_fragment, container, false)
        viewModel = ViewModelProvider(this).get(AllQuestionsViewModel::class.java)

        setRecyclerView()

        viewModel.listOfCategoryItem.observe(viewLifecycleOwner, Observer {
            adapter.submitQuestionItemList(it)
        })

        return binding.root
    }

    private fun setRecyclerView() {
        adapter = AllQuestionsAdapter()
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(this.context)
        binding.recyclerView.setHasFixedSize(true)
    }

}
