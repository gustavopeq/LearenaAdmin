package gustavo.projects.learenaadmin.questions.allQuestion

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import gustavo.projects.learenaadmin.R
import kotlinx.android.synthetic.main.all_questions_item.view.*

class AllQuestionsAdapter: RecyclerView.Adapter<AllQuestionsAdapter.AllQuestionsViewHolder>() {

    private var questionItemList: List<QuestionItem> = arrayListOf()

    fun submitQuestionItemList(newList: List<QuestionItem>) {
        val oldList = questionItemList
        val diffResult: DiffUtil.DiffResult = DiffUtil.calculateDiff(
            QuestionItemDiffCallback(
                oldList,
                newList
            )
        )

        questionItemList = newList
        diffResult.dispatchUpdatesTo(this)
    }

    class QuestionItemDiffCallback(
        var oldQuestionItemList: List<QuestionItem>,
        var newQuestionItemList: List<QuestionItem>
    ) : DiffUtil.Callback() {
        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return (oldQuestionItemList[oldItemPosition].questionName == newQuestionItemList[newItemPosition].questionName)
        }

        override fun getOldListSize(): Int {
            return oldQuestionItemList.size
        }

        override fun getNewListSize(): Int {
            return newQuestionItemList.size
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return (oldQuestionItemList[oldItemPosition].equals(newQuestionItemList[newItemPosition]))
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AllQuestionsViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.all_questions_item, parent, false)
        return AllQuestionsViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: AllQuestionsViewHolder, position: Int) {
        val currentItem = questionItemList[position]

        holder.questionName.text = currentItem.questionName
    }

    override fun getItemCount() = questionItemList.size

    inner class AllQuestionsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val questionName: TextView = itemView.questionNameTextView
    }

}