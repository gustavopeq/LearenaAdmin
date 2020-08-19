package gustavo.projects.learenaadmin.categories

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import gustavo.projects.learenaadmin.R
import kotlinx.android.synthetic.main.category_item.view.*

class CategoryAdapter(private val categoryItemList: List<CategoryItem>) : RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.category_item, parent, false)

        return CategoryViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        val currentItem = categoryItemList[position]

        holder.categoryName.text = currentItem.categoryName
        holder.numOfQuestions.text = currentItem.numOfQuestion.toString()
    }

    override fun getItemCount() = categoryItemList.size

    class CategoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val categoryName: TextView = itemView.categoryNameTextView
        val numOfQuestions: TextView = itemView.numOfQuestionTextView
    }
}