package com.app.compress.pdf.stash.model

data class Pdf(
    var filename: String,
    var size: Long,
    var date: Long,
    var filePath: String
)
