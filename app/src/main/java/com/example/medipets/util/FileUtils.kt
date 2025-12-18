package com.example.medipets.util

import android.content.Context
import android.net.Uri
import androidx.core.content.FileProvider
import java.io.File

fun crearImagenUri(context: Context): Uri {
    val imagesDir = File(
        context.getExternalFilesDir(null),
        "Pictures"
    )
    if (!imagesDir.exists()) imagesDir.mkdirs()

    val imageFile = File.createTempFile(
        "mascota_",
        ".jpg",
        imagesDir
    )

    return FileProvider.getUriForFile(
        context,
        "${context.packageName}.fileprovider",
        imageFile
    )
}