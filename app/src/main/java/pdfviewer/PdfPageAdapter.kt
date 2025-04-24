import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Matrix
import android.graphics.Rect
import android.graphics.pdf.PdfRenderer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.app.compress.pdf.stash.R

class PdfPageAdapter(private val pdfRenderer: PdfRenderer) :
    RecyclerView.Adapter<PdfPageAdapter.PageViewHolder>() {

    inner class PageViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imageView: ImageView = view.findViewById(R.id.pageImage)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PageViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_pdf_page, parent, false)
        return PageViewHolder(view)
    }

    override fun onBindViewHolder(holder: PageViewHolder, position: Int) {
        val page = pdfRenderer.openPage(position)

        val displayMetrics = holder.imageView.context.resources.displayMetrics
        val targetWidth = displayMetrics.widthPixels - 32 // 16dp padding each side

        val scale = targetWidth.toFloat() / page.width
        val targetHeight = (page.height * scale).toInt()

        val bitmap = Bitmap.createBitmap(targetWidth, targetHeight, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        canvas.drawColor(Color.WHITE) // optional: white background

        val rect = Rect(0, 0, targetWidth, targetHeight)
        page.render(bitmap, rect, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY)
        page.close()

        holder.imageView.setImageBitmap(bitmap)
        holder.imageView.imageMatrix = Matrix() // reset zoom
    }

    override fun getItemCount(): Int = pdfRenderer.pageCount
}
