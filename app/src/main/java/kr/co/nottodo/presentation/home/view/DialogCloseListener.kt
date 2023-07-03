package kr.co.nottodo.presentation.home.view

interface DialogCloseListener {
    fun onDismissAndDataPass(selectFirstDay: String?)
    fun onDeleteButtonClicked() {}
}
