package com.software3.paws_hub_android.ui.view.dialogs

import android.content.Context
import android.content.DialogInterface
import androidx.constraintlayout.motion.widget.MotionScene.Transition.TransitionOnClick
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.software3.paws_hub_android.R

class FilterDialog(private val context: Context) {
    private val dialog = MaterialAlertDialogBuilder(
        context,
        R.style.ThemeOverlay_PawsHubAndroid_FiltersAlertDialog
    )

    init {
        dialog.setTitle(context.getString(R.string.apply_filter))
        dialog.setCancelable(true)
        dialog.setIcon(R.drawable.ic_tune_24)

    }

    fun setItems(items: Array<out String>, onClick: (item: Int) -> Unit): FilterDialog {
        dialog.setItems(items) { _, item -> onClick(item) }
        return this
    }

    fun show() {
        dialog.show()
    }
}