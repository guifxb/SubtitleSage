package com.gfbdev.subtitlesage.pdfmanager

import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import androidx.core.content.FileProvider
import com.gfbdev.subtitlesage.R
import com.gfbdev.subtitlesage.model.Experience
import com.gfbdev.subtitlesage.model.UserInfo
import com.gfbdev.subtitlesage.ui.managers.AppUiState
import java.io.File
import java.io.FileOutputStream

class PdfController {
    //Handles Pdf creation, saving and sharing

    fun generatePDF(
        context: Context,
        uiState: AppUiState,
        mainUser: UserInfo,
        experiences: List<Experience>
    ) {

        val movies = when (uiState) {
            is AppUiState.Loading -> emptyList()
            is AppUiState.Success -> uiState.movies
            else -> emptyList()
        }

        if (movies.isEmpty()) {
            Toast.makeText(context, context.getString(R.string.empty_list_toast), Toast.LENGTH_SHORT).show()
            return
        }

        val document = pdfModel(context, movies, mainUser, experiences)

        val path = context.getExternalFilesDir(null)
        val filename = "portfolio_${System.currentTimeMillis()}.pdf"
        val file = File(path, filename)

        try {
            document.writeTo(FileOutputStream(file))
            Toast.makeText(context, context.getString(R.string.pdf_created_toast), Toast.LENGTH_LONG)
                .show()
        } catch (e: Exception) {
            Toast.makeText(context, context.getString(R.string.pdf_error_toast), Toast.LENGTH_LONG).show()
            e.printStackTrace()
        }

        val documentURI = FileProvider.getUriForFile(
            context, "${context.packageName}.fileprovider", file
        )

        //Sharing and closing
        val share = Intent()
        share.action = Intent.ACTION_SEND
        share.type = "application/pdf"
        share.putExtra(Intent.EXTRA_STREAM, documentURI)
        share.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        context.grantUriPermission(
            "${context.packageName}.fileprovider",
            documentURI,
            Intent.FLAG_GRANT_READ_URI_PERMISSION.and(Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
        )
        startActivity(context, Intent.createChooser(share, "Share"), null)

        document.close()
    }
}