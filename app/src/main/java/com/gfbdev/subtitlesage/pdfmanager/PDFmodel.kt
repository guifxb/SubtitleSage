package com.gfbdev.subtitlesage.pdfmanager

import android.content.Context
import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.graphics.pdf.PdfDocument
import android.text.TextUtils
import androidx.core.content.ContextCompat
import coil.ImageLoader
import coil.annotation.ExperimentalCoilApi
import com.gfbdev.subtitlesage.R
import com.gfbdev.subtitlesage.model.Experience
import com.gfbdev.subtitlesage.model.MovieInfoLocal
import com.gfbdev.subtitlesage.model.UserInfo
import kotlinx.coroutines.*

@OptIn(ExperimentalCoilApi::class)
fun pdfModel(
    context: Context,
    movies: List<MovieInfoLocal>,
    mainUser: UserInfo,
    experiences: List<Experience>
): PdfDocument {

    //Start the document and setup page info
    val document = PdfDocument()
    var pageNumber = 1
    val pageInfo = PdfDocument.PageInfo.Builder(595, 842, pageNumber).create()
    var page = document.startPage(pageInfo)
    var canvas = page.canvas
    val paint = Paint()
    val imageLoader = ImageLoader(context)
    val leftMargin = 24F

    //Background painting
    val background = ContextCompat.getColor(context, R.color.md_theme_light_onSecondary)
    paint.color = background
    canvas.drawColor(paint.color)

    //Painting the separator
    paint.color = ContextCompat.getColor(context, R.color.md_theme_light_onPrimaryContainer)
    paint.strokeWidth = 2f
    canvas.drawLine(200f, 0f, 200f, 842f, paint)
    canvas.drawLine(204f, 0f, 204f, 842f, paint)
    paint.color = ContextCompat.getColor(context, R.color.md_theme_light_surfaceTint)
    canvas.drawLine(202f, 0f, 202f, 842f, paint)

    //Getting info
    val title = mainUser.title
    val name = mainUser.name
    val whatsapp = mainUser.whatsApp
    val email = mainUser.email
    val linkedIn = mainUser.linkedIn
    val aboutMe = mainUser.aboutMe

    //Painting left side info
    paint.textSize = 16f
    paint.color = Color.BLACK
    var y = 50f
    canvas.drawText(name, leftMargin, y, paint)

    paint.textSize = 14f
    y += 30f
    canvas.drawText(title, leftMargin, y, paint)

    //Breaking AboutMe into lines, do not forget to setup 300 max characters
    paint.textSize = 10f
    y += 50f
    val lineWidth = 15 * paint.textSize
    val words = TextUtils.split(aboutMe, "(?<=\\W)")
    var line = ""
    for (word in words) {
        val textWidth = paint.measureText(line + word)
        if (textWidth > lineWidth) {
            canvas.drawText(line, leftMargin, y, paint)
            y += paint.textSize
            line = word
        } else {
            line += word
        }
    }
    canvas.drawText(line, leftMargin, y, paint)

    //Contact info
    y += 50f
    canvas.drawText(whatsapp, leftMargin, y, paint)
    y += 20f
    canvas.drawText(email, leftMargin, y, paint)
    y += 20f
    canvas.drawText(linkedIn, leftMargin, y, paint)

    //Experiences
    y += 50f
    for (experience in experiences) {
            paint.textSize = 14f
            canvas.drawText(experience.company, leftMargin, y, paint)
            y += 16f
            paint.textSize = 10f
        if (experience.role != "") {
            canvas.drawText("${experience.role} - ${experience.pair}", leftMargin, y, paint)
            y += 12f
        }
        if (experience.time != "") {
            canvas.drawText("(${experience.time})", leftMargin, y, paint)
            y += 24f
        }
    }

    //Portfolio container
    //Setting start position and poster size
    var baseX = 250f
    val baseY = 80f
    val imageY = 96
    val imageX = 64
    val spacing = 22f
    var x = baseX
    y = baseY
    var row = 1
    paint.textAlign = Paint.Align.CENTER

    //container title
    paint.textSize = 16f
    canvas.drawText(context.getString(R.string.pdf_container_title), 410f, 50f, paint)

    //new page settings
    fun newPage() {
        document.finishPage(page)
        pageNumber++
        page = document.startPage(pageInfo)
        canvas = page.canvas
        paint.color = background
        canvas.drawColor(paint.color)
        paint.color = Color.BLACK
        baseX = 60f
        y = baseY
        x = baseX
    }

    // Poster painter
    movies.forEach { movie ->
        //loads poster from cache and build into a Bitmap, might wanna add a null check and a broken image
        val result = imageLoader.diskCache?.get(movie.poster)?.data
        val drawable = result?.let { path ->
            val bitmap = BitmapFactory.decodeFile(path.toString())
            val resizedBitmap = Bitmap.createScaledBitmap(bitmap, imageX, imageY, true)
            BitmapDrawable(context.resources, resizedBitmap)
        }?.bitmap

        //Painting poster, title and year
        canvas.drawBitmap(drawable!!, x, y, paint)
        val tempY = y
        paint.textSize = 10f
        y += imageY + 2f + paint.textSize
        x += imageX / 2
        canvas.drawText(movie.title, x, y, paint)
        paint.textSize = 8f
        y += paint.textSize + 4f
        canvas.drawText("(${movie.year})", x, y, paint)

        //Setting position for next
        x += imageX / 2 + spacing
        y = tempY

        //Checks for space available in row and skips row if needed
        if (x + imageX + spacing > canvas.width) {
            x = baseX
            y += imageY + spacing + 16f
            row++
            //Checks and creates new page is needed
            if (row > 5) {
                newPage()
                row = 1
            }
        }
    }

    document.finishPage(page)
    return document
}