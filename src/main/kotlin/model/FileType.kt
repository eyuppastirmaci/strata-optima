package com.eyuppastirmaci.model

/**
 * Represents file categories based on their purpose and content type.
 * Sealed class ensures type safety and exhaustive when expressions.
 */
sealed class FileType(val category: String) {

    data object Image : FileType("Images")
    data object Video : FileType("Videos")
    data object Audio : FileType("Audio")
    data object Document : FileType("Documents")
    data object Archive : FileType("Archives")
    data object Executable : FileType("Applications")
    data object Code : FileType("Code")
    data object Other : FileType("Other")

    companion object {
        val imageExtensions = setOf(
            "jpg", "jpeg", "png", "gif", "bmp", "webp", "svg", "ico", "tiff", "tif", "heic", "raw"
        )

        val videoExtensions = setOf(
            "mp4", "avi", "mkv", "mov", "wmv", "flv", "webm", "m4v", "mpeg", "mpg", "3gp"
        )

        val audioExtensions = setOf(
            "mp3", "wav", "flac", "aac", "ogg", "wma", "m4a", "opus", "aiff"
        )

        val documentExtensions = setOf(
            "pdf", "doc", "docx", "xls", "xlsx", "ppt", "pptx", "txt", "rtf", "odt", "ods", "odp", "csv", "md"
        )

        val archiveExtensions = setOf(
            "zip", "rar", "7z", "tar", "gz", "bz2", "xz", "iso", "dmg"
        )

        val executableExtensions = setOf(
            "exe", "msi", "apk", "app", "deb", "rpm", "bat", "sh", "jar"
        )

        val codeExtensions = setOf(
            "kt", "java", "py", "js", "ts", "c", "cpp", "h", "cs", "go", "rs", "rb", "php", "swift", "html", "css", "sql", "json", "xml", "yaml", "yml"
        )
    }
}