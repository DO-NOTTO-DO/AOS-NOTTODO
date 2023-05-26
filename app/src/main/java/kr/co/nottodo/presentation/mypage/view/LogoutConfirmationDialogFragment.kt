import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.DialogFragment

class LogoutConfirmationDialogFragment : DialogFragment() {

    interface LogoutConfirmationListener {
        fun onLogoutConfirmed()
    }

    private var logoutConfirmationListener: LogoutConfirmationListener? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            builder.setMessage("로그아웃 하시겠습니까?")
                .setPositiveButton("Yes",
                    DialogInterface.OnClickListener { dialog, id ->
                        logoutConfirmationListener?.onLogoutConfirmed()
                    })
                .setNegativeButton("Cancel",
                    DialogInterface.OnClickListener { dialog, id ->
                        // User cancelled the dialog
                    })
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }

    fun setLogoutConfirmationListener(listener: LogoutConfirmationListener) {
        logoutConfirmationListener = listener
    }

    override fun onDetach() {
        super.onDetach()
        logoutConfirmationListener = null
    }
}
