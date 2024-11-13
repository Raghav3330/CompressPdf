package com.app.compress.pdf.stash.model

import java.io.File
import java.io.Serializable

data class Pdf(
    var filename: String,
    var size: Long,
    var date: Long,
    var filePath: String
)
