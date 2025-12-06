package com.eyuppastirmaci.extensions

import com.eyuppastirmaci.model.FileType
import java.io.File

/**
 * Detects the file type based on its extension.
 * Returns FileType.Other for unknown or missing extensions.
 */
fun File.detectFileType(): FileType {
    val ext = extension.lowercase()

    if (ext.isEmpty()) return FileType.Other

    return when (ext) {
        in FileType.imageExtensions -> FileType.Image
        in FileType.videoExtensions -> FileType.Video
        in FileType.audioExtensions -> FileType.Audio
        in FileType.documentExtensions -> FileType.Document
        in FileType.archiveExtensions -> FileType.Archive
        in FileType.executableExtensions -> FileType.Executable
        in FileType.codeExtensions -> FileType.Code
        else -> FileType.Other
    }
}

/**
 * Returns the target subdirectory name for organizing this file.
 */
fun File.getTargetCategory(): String {
    return detectFileType().category
}