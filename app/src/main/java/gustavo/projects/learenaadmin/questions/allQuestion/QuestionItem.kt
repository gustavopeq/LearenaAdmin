package gustavo.projects.learenaadmin.questions.allQuestion

data class QuestionItem(val questionName: String) {

    override fun equals(other: Any?): Boolean {

        if(javaClass != other?.javaClass) {
            return false
        }

        other as QuestionItem

        if(questionName != other.questionName) {
            return false
        }

        return true
    }
}