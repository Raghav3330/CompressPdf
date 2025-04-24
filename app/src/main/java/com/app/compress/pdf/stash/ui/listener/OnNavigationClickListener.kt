package com.app.compress.pdf.stash.ui.listener

import com.app.compress.pdf.stash.model.Pdf
import java.io.File

interface OnNavigationClickListener {
    fun openCompressFragment()
    fun openHistoryFragment()
    fun openResultFragment(status: Boolean, pdf: Pdf?, destFile: File?)
}