package gustavo.projects.learenaadmin.questions.allQuestion

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.ImageView
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager

import gustavo.projects.learenaadmin.R
import gustavo.projects.learenaadmin.categories.CategoryAdapter
import gustavo.projects.learenaadmin.categories.newCategory.INewCategoryForm
import gustavo.projects.learenaadmin.databinding.AllQuestionsFragmentBinding

class AllQuestions : Fragment(), CategoryAdapter.OnItemClickListener, INewCategoryForm {

    override var starImg1: ImageView
        get() = binding.starsImg1
        set(value) {}
    override var starImg2: ImageView
        get() = binding.starsImg2
        set(value) {}
    override var starImg3: ImageView
        get() = binding.starsImg3
        set(value) {}
    override var starImg4: ImageView
        get() = binding.starsImg4
        set(value) {}
    override var starImg5: ImageView
        get() = binding.starsImg5
        set(value) {}
    override var listOfStarsImg: ArrayList<ImageView>
        get() = createArrayOfStarsImg()
        set(value) {}
    override var starLevel: Int = 1

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

        viewModel.categoryDescription.observe(viewLifecycleOwner, Observer {
            binding.categoryDescriptionTextInput.editText?.setText(it)
        })

        viewModel.categoryStarLevel.observe(viewLifecycleOwner, Observer {
            displayStarLevel(it)
        })

        viewModel.listOfQuestionItem.observe(viewLifecycleOwner, Observer {
            listOfRecyclerItem = it
            adapter.submitQuestionItemList(listOfRecyclerItem)
            binding.allQuestionLoadingIcon.visibility = View.GONE
        })

        viewModel.noQuestionFound.observe(viewLifecycleOwner, Observer {
            if(it) {
                binding.allQuestionLoadingIcon.visibility = View.GONE
                binding.noQuestionsCreatedTextView.visibility = View.VISIBLE
            }

        })

        binding.createNewQuestionFab.setOnClickListener { navigateToNewQuestion() }

        setHasOptionsMenu(true)

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.edit_category, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.editCategoryIcon -> navigateToEditCategory()
            android.R.id.home -> onBackArrowClick()
        }
        return true
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

    private fun navigateToEditCategory() {
        findNavController().navigate(AllQuestionsDirections.actionAllQuestionsToEditCategory(categoryName))
    }

    private fun onBackArrowClick() {
        findNavController().navigate(AllQuestionsDirections.actionAllQuestionsToCategories())
    }

}
