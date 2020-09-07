package gustavo.projects.learenaadmin.questions.allQuestion

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager

import gustavo.projects.learenaadmin.R
import gustavo.projects.learenaadmin.categories.CategoryAdapter
import gustavo.projects.learenaadmin.databinding.AllQuestionsFragmentBinding

class AllQuestions : Fragment(), CategoryAdapter.OnItemClickListener {

    private lateinit var binding: AllQuestionsFragmentBinding
    private lateinit var viewModel: AllQuestionsViewModel

    private var listOfRecyclerItem = ArrayList<QuestionItem>()
    private lateinit var adapter: AllQuestionsAdapter

    private lateinit var categoryName: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.all_questions_fragment, container, false)

        categoryName = AllQuestionsArgs.fromBundle(requireArguments()).categoryName

        val viewModelFactory = AllQuestionsViewModelFactory(categoryName)

        viewModel = ViewModelProvider(this, viewModelFactory).get(AllQuestionsViewModel::class.java)

        setRecyclerView()

        viewModel.getQuestionsFromDatabase()

        viewModel.listOfQuestionItem.observe(viewLifecycleOwner, Observer {
            listOfRecyclerItem = it
            adapter.submitQuestionItemList(listOfRecyclerItem)
        })

        binding.createNewQuestionFab.setOnClickListener { navigateToNewQuestion() }

        return binding.root
    }

    private fun setRecyclerView() {
        adapter = AllQuestionsAdapter(this)
        binding.questionsRecyclerView.adapter = adapter
        binding.questionsRecyclerView.layoutManager = LinearLayoutManager(this.context)
        binding.questionsRecyclerView.setHasFixedSize(true)
    }

    override fun onItemClick(position: Int) {
        navigateToQuestionDetail(position)
    }

    private fun navigateToQuestionDetail(position: Int) {
        val questionName = adapter.getItem(position)

        findNavController().navigate(AllQuestionsDirections.actionAllQuestionsToQuestionDetails(categoryName, questionName))
    }

    private fun navigateToNewQuestion() {
        findNavController().navigate(AllQuestionsDirections.actionAllQuestionsToNewQuestion(categoryName))
    }

}
