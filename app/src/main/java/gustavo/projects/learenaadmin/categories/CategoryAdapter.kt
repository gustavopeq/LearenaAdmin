package gustavo.projects.learenaadmin.categories

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import gustavo.projects.learenaadmin.R
import kotlinx.android.synthetic.main.category_item.view.*

class CategoryAdapter(
    private val itemListener: OnItemClickListener,
    private val moreOptionListener: OnMoreOptionClickListener
)
    : RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>() {

    private var categoryItemList: List<CategoryItem> = arrayListOf()

    fun submitCategoryItemList(newList: List<CategoryItem>) {
        val oldList = categoryItemList
        val diffResult: DiffUtil.DiffResult = DiffUtil.calculateDiff(
            CategoryItemDiffCallback(
                oldList,
                newList
            )
        )
        categoryItemList = newList
        diffResult.dispatchUpdatesTo(this)
    }

    class CategoryItemDiffCallback(
        var oldCategoryItemList: List<CategoryItem>,
        var newCategoryItemList: List<CategoryItem>
    ) : DiffUtil.Callback() {
        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return (oldCategoryItemList[oldItemPosition].categoryName == newCategoryItemList[newItemPosition].categoryName)
        }

        override fun getOldListSize(): Int {
            return oldCategoryItemList.size
        }

        override fun getNewListSize(): Int {
            return newCategoryItemList.size
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldCategoryItemList[oldItemPosition].equals(newCategoryItemList[newItemPosition])
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.category_item, parent, false)

        return CategoryViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        val currentItem = categoryItemList[position]

        holder.categoryName.text = currentItem.categoryName
        holder.numOfQuestions.text = currentItem.numOfQuestion.toString()

        Log.d("print", "Recreating ${currentItem.categoryName} $position")
    }

    override fun getItemCount() = categoryItemList.size

    inner class CategoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        val categoryName: TextView = itemView.categoryNameTextView
        val numOfQuestions: TextView = itemView.numOfQuestionTextView
        val itemMoreOption: ImageView = itemView.itemMoreOption

        init {
            itemMoreOption.setOnClickListener(this)
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            val itemPosition = adapterPosition
            if (itemPosition != RecyclerView.NO_POSITION) {
                when(v) {
                    itemView -> itemListener.onItemClick(itemPosition)
                    itemMoreOption -> moreOptionListener.onMoreOptionClick(itemPosition, categoryName.text.toString(), itemMoreOption)
                }
            }
        }
    }

    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }

    interface OnMoreOptionClickListener {
        fun onMoreOptionClick(position: Int, categoryName: String, icon: ImageView)
    }

}