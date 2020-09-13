package gustavo.projects.learenaadmin.questions

import gustavo.projects.learenaadmin.questions.allQuestion.QuestionObject

data class QuestionDocument(var listOfQuestions: QuestionObject, var categoryDescription: String, var starLeveL: Int)