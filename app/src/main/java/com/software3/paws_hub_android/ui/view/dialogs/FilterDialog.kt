package com.software3.paws_hub_android.ui.view.dialogs

import android.content.Context
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.software3.paws_hub_android.R

class FilterDialog(private val context: Context, onCancel: () -> Unit = {}) {
    private val dialog = MaterialAlertDialogBuilder(
        context,
        R.style.ThemeOverlay_PawsHubAndroid_AlertDialog
    )

    init {
        dialog.setTitle(context.getString(R.string.apply_filter))
        dialog.setCancelable(true)
        dialog.setPositiveButton(context.getString(R.string.filter_dialog_positive)) {i, _ ->
            i.dismiss()
        }
        dialog.setNegativeButton(context.getString(R.string.filter_dialog_negative)) {i, _ ->
            onCancel()
            i.dismiss()
        }
        //dialog.setIcon(R.drawable.ic_tune_24)
    }

    fun setItems(items: Array<out String>, onClick: (item: Int) -> Unit): FilterDialog {
        dialog.setItems(items) { _, item -> onClick(item) }
        return this
    }

    fun show() {
        dialog.show()
    }
}