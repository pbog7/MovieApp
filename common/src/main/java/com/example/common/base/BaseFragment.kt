package com.example.common.base

import android.app.AlertDialog
import androidx.fragment.app.Fragment
import com.example.common.R

abstract class BaseFragment:Fragment() {
     fun showErrorDialog(errorMessage: String) {
        activity?.let {
            val builder = AlertDialog.Builder(it)
            builder.apply {
                setNeutralButton(
                    R.string.ok
                ) { dialog, _ ->
                    dialog.dismiss()
                }
                setTitle(getString(R.string.error))
                setMessage(errorMessage)
            }
            builder.create().show()
        }
    }
}