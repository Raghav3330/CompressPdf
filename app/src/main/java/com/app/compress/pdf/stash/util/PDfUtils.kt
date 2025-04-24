package com.app.compress.pdf.stash.util


import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import com.itextpdf.io.image.ImageDataFactory
import com.itextpdf.kernel.colors.ColorConstants
import com.itextpdf.kernel.font.PdfFontFactory
import com.itextpdf.kernel.geom.PageSize
import com.itextpdf.kernel.pdf.*
import com.itextpdf.kernel.pdf.canvas.PdfCanvas
import com.itextpdf.kernel.pdf.xobject.PdfImageXObject
import com.itextpdf.kernel.utils.PdfMerger
import com.itextpdf.layout.Document
import com.itextpdf.layout.properties.TextAlignment
import com.itextpdf.kernel.pdf.extgstate.PdfExtGState
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream

/**
 * PdfUtils contains several common PDF operations using iText 7.
 *
 * Note: This is a simplified implementation for demonstration purposes.
 * In a production app, you should add error checking and handle edge cases.
 */
object PdfUtils {

    /**
     * Compresses a PDF file by opening it and writing it back using full compression.
     *
     * @param inputPath  The path to the source PDF.
     * @param outputPath The path where the compressed PDF will be saved.
     */
    fun compressPdf(inputPath: String, outputPath: String) {
        try {
            // Open the source PDF
            val reader = PdfReader(inputPath)

            // Configure WriterProperties: full compression mode and best compression level.
            val writerProperties = WriterProperties()
                .useSmartMode()
                .setFullCompressionMode(true)
                .setCompressionLevel(CompressionConstants.BEST_COMPRESSION)

            // Create PdfWriter with the output path and properties.
            val writer = PdfWriter(outputPath, writerProperties)

            // Open the document
            val pdfDoc = PdfDocument(reader, writer)
            Log.d("PdfCompressorAdv", "Opened PDF with ${pdfDoc.numberOfPages} pages.")

            // Process each page in the PDF.
            for (i in 1..pdfDoc.numberOfPages) {
                val page = pdfDoc.getPage(i)
                val resources = page.resources

                // Get the dictionary of XObject resources (if any)
                val xObjects = resources.getResource(PdfName.XObject)
                if (xObjects != null) {
                    // Iterate through each resource key in the dictionary.
                    for (key in xObjects.keySet()) {
                        // Get the XObject stream.
                        val xObject = xObjects.getAsStream(key)
                        // Check if the XObject exists and its subtype is Image.
                        if (xObject != null && PdfName.Image == xObject.getAsName(PdfName.Subtype)) {
                            try {
                                Log.d("PdfCompressorAdv", "Found image XObject: $key on page $i")

                                // Wrap the stream as a PdfImageXObject to access image data.
                                val oldImageXObject = PdfImageXObject(xObject)
                                // Get the original image bytes.
                                val imageBytes = oldImageXObject.imageBytes

                                // Decode the image bytes into a Bitmap.
                                val bitmap: Bitmap? = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
                                if (bitmap != null) {
                                    // Create a ByteArrayOutputStream for the recompressed image.
                                    val baos = ByteArrayOutputStream()
                                    if (true) {
                                        // Compress as JPEG with the given quality.
                                        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, baos)
                                    } else {
                                        // Compress as PNG for lossless compression.
                                        bitmap.compress(Bitmap.CompressFormat.PNG, 50, baos)
                                    }
                                    val compressedBytes = baos.toByteArray()
                                    baos.close()

                                    Log.d("PdfCompressorAdv", "Recompressed image size: ${compressedBytes.size} bytes")

                                    // Create ImageData from the recompressed bytes.
                                    val imageData = ImageDataFactory.create(compressedBytes)
                                    // Create a new PdfImageXObject from the ImageData.
                                    val newImageXObject = PdfImageXObject(imageData)
                                    // Replace the original image XObject in the resources with the new one.
                                    xObjects.put(key, newImageXObject.pdfObject)
                                    // Recycle the bitmap to free memory.
                                    bitmap.recycle()
                                } else {
                                    Log.w("PdfCompressorAdv", "Failed to decode image for key: $key")
                                }
                            } catch (imgEx: Exception) {
                                Log.e("PdfCompressorAdv", "Error processing image XObject: $key", imgEx)
                            }
                        }

                    }
                }
            }

            // Close the document (which writes all changes).
            pdfDoc.close()
            Log.d("PdfCompressorAdv", "Compressed PDF saved to: $outputPath")
        } catch (e: Exception) {
            Log.e("PdfCompressorAdv", "Error compressing PDF advanced", e)
        }
    }

    /**
     * Merges multiple PDFs into a single PDF.
     *
     * @param inputPaths List of file paths to the source PDFs.
     * @param outputPath The path where the merged PDF will be saved.
     */
    fun mergePdfs(inputPaths: List<String>, outputPath: String) {
        try {
            val writer = PdfWriter(outputPath)
            val mergedDoc = PdfDocument(writer)
            val merger = PdfMerger(mergedDoc)
            inputPaths.forEach { path ->
                val reader = PdfReader(path)
                val srcDoc = PdfDocument(reader)
                merger.merge(srcDoc, 1, srcDoc.numberOfPages)
                srcDoc.close()
            }
            mergedDoc.close()
            Log.d("PdfUtils", "Merge: Merged PDF saved to: $outputPath")
        } catch (e: Exception) {
            Log.e("PdfUtils", "Error merging PDFs", e)
        }
    }

    /**
     * Splits a PDF into separate single-page PDFs.
     *
     * @param inputPath    The path to the source PDF.
     * @param outputDirStr The directory where each split page PDF will be saved.
     */
    fun splitPdf(inputPath: String, outputDirStr: String) {
        try {
            val reader = PdfReader(inputPath)
            val srcDoc = PdfDocument(reader)
            val totalPages = srcDoc.numberOfPages
            val outputDir = File(outputDirStr)
            if (!outputDir.exists()) {
                outputDir.mkdirs()
            }
            for (i in 1..totalPages) {
                val outputPath = "$outputDirStr/page_$i.pdf"
                val writer = PdfWriter(outputPath)
                val splitDoc = PdfDocument(writer)
                // Copy the single page to the new document
                srcDoc.copyPagesTo(i, i, splitDoc)
                splitDoc.close()
                Log.d("PdfUtils", "Split: Page $i saved to: $outputPath")
            }
            srcDoc.close()
        } catch (e: Exception) {
            Log.e("PdfUtils", "Error splitting PDF", e)
        }
    }

    /**
     * Extracts images from the PDF and saves them as separate image files.
     *
     * @param inputPath    The path to the source PDF.
     * @param outputDirStr The directory where extracted images will be saved.
     */
    fun extractImages(inputPath: String, outputDirStr: String) {
        try {
            val reader = PdfReader(inputPath)
            val pdfDoc = PdfDocument(reader)
            val outputDir = File(outputDirStr)
            if (!outputDir.exists()) {
                outputDir.mkdirs()
            }
            for (i in 1..pdfDoc.numberOfPages) {
                val page = pdfDoc.getPage(i)
                val resources = page.resources
                val xObjects = resources.getResource(PdfName.XObject)
                if (xObjects != null) {
                    for (key in xObjects.keySet()) {
                        val pdfObject = xObjects.get(key)
                        // Only process if the resource is an image stream
                        if (pdfObject is PdfStream && pdfObject.getAsName(PdfName.Subtype) == PdfName.Image) {
                            val imageXObject = PdfImageXObject(pdfObject)
                            val imageBytes = imageXObject.imageBytes
                            val imageFile = File(outputDir, "page_${i}_${key.toString().replace("/", "")}.jpg")
                            FileOutputStream(imageFile).use { it.write(imageBytes) }
                            Log.d("PdfUtils", "Extract: Saved image from page $i with key $key to ${imageFile.absolutePath}")
                        }
                    }
                }
            }
            pdfDoc.close()
        } catch (e: Exception) {
            Log.e("PdfUtils", "Error extracting images from PDF", e)
        }
    }

    /**
     * Adds a watermark to every page of the PDF.
     *
     * @param inputPath     The path to the source PDF.
     * @param outputPath    The path where the watermarked PDF will be saved.
     * @param watermarkText The text of the watermark.
     */
    fun addWatermark(inputPath: String, outputPath: String, watermarkText: String) {
        try {
            val reader = PdfReader(inputPath)
            val writer = PdfWriter(outputPath)
            val pdfDoc = PdfDocument(reader, writer)
            val doc = Document(pdfDoc)
            val font = PdfFontFactory.createFont()
            val gState = PdfExtGState().setFillOpacity(0.3f)
            for (i in 1..pdfDoc.numberOfPages) {
                val page = pdfDoc.getPage(i)
                val canvas = PdfCanvas(page.newContentStreamAfter(), page.resources, pdfDoc)
                val pageSize = page.pageSize
                // Position the watermark roughly at the center.
                val x = (pageSize.width / 2).toDouble()
                val y = (pageSize.height / 2).toDouble()
                canvas.saveState()
                canvas.setExtGState(gState)
                canvas.beginText()
                    .setFontAndSize(font, 60f)
                    .moveText(x - 100, y)
                    .showText(watermarkText)
                    .endText()
                canvas.restoreState()
            }
            doc.close()
            Log.d("PdfUtils", "Watermark: Watermarked PDF saved to: $outputPath")
        } catch (e: Exception) {
            Log.e("PdfUtils", "Error adding watermark to PDF", e)
        }
    }
}
