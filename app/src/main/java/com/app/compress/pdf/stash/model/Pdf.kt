package com.app.compress.pdf.stash.model

data class Pdf(
    val id: Long,
    var filename: String,
    var size: Long,
    var date: Long,
    var filePath: String
)
