package gustavo.projects.learenaadmin

import android.app.Activity
import android.app.AlertDialog
import android.content.DialogInterface

interface BasicDialogWindow {

    fun onCreateDialog(activity: Activity, message: String, positiveBtnText: String, negativeBtnText: String) {
        val builder = AlertDialog.Builder(activity)
        builder.setMessage(message)
        if(positiveBtnText.isNotEmpty()) {
            builder.setPositiveButton(positiveBtnText) { _: DialogInterface?, _: Int ->
                onDialogPositiveBtn()
            }
        }
        if(negativeBtnText.isNotEmpty()) {
            builder.setNegativeButton(negativeBtnText) { _: DialogInterface?, _: Int ->
                onDialogNegativeBtn()
            }
        }

        builder.show()
    }

    fun onDialogPositiveBtn()
    fun onDialogNegativeBtn()
}