package gustavo.projects.learenaadmin.questions


data class QuestionDocument(var listOfQuestions: MutableMap<String, ArrayList<String>>? = null, var categoryDescription: String? = null, var starLeveL: Int = 0)