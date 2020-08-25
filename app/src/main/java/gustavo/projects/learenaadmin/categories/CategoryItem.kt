package gustavo.projects.learenaadmin.categories

data class CategoryItem (val categoryName: String, val numOfQuestion: Int) {
    override fun equals(other: Any?): Boolean {

        if (javaClass != other?.javaClass) {
            return false
        }

        other as CategoryItem

        if (categoryName != other.categoryName) {
            return false
        }

        if (numOfQuestion != other.numOfQuestion) {
            return false
        }

        return true
    }
}