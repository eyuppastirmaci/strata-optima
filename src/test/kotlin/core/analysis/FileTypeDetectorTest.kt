package com.eyuppastirmaci.extensions

import com.eyuppastirmaci.model.FileType
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.io.TempDir
import java.nio.file.Path

@DisplayName("FileTypeDetector")
class FileTypeDetectorTest {

    @Test
    fun `should detect all file types correctly`(@TempDir tempDir: Path) {
        // Given: files with different extensions
        val testCases = mapOf(
            "photo.jpg" to FileType.Image,
            "movie.mp4" to FileType.Video,
            "song.mp3" to FileType.Audio,
            "report.pdf" to FileType.Document,
            "backup.zip" to FileType.Archive,
            "setup.exe" to FileType.Executable,
            "Main.kt" to FileType.Code
        )

        // When & Then: each file detected correctly
        testCases.forEach { (fileName, expectedType) ->
            val file = tempDir.resolve(fileName).toFile().apply { createNewFile() }
            assertEquals(expectedType, file.detectFileType(), "Failed for: $fileName")
        }
    }

    @Test
    fun `should return Other for unknown or missing extension`(@TempDir tempDir: Path) {
        // Given: files with unknown/no extension
        val unknownExt = tempDir.resolve("data.xyz").toFile().apply { createNewFile() }
        val noExt = tempDir.resolve("README").toFile().apply { createNewFile() }

        // Then: both return Other
        assertEquals(FileType.Other, unknownExt.detectFileType())
        assertEquals(FileType.Other, noExt.detectFileType())
    }

    @Test
    fun `should be case insensitive`(@TempDir tempDir: Path) {
        // Given: files with various case extensions
        val upper = tempDir.resolve("PHOTO.JPG").toFile().apply { createNewFile() }
        val mixed = tempDir.resolve("video.Mp4").toFile().apply { createNewFile() }

        // Then: correctly detected regardless of case
        assertEquals(FileType.Image, upper.detectFileType())
        assertEquals(FileType.Video, mixed.detectFileType())
    }

    @Test
    fun `should return correct target category`(@TempDir tempDir: Path) {
        // Given: an image file
        val file = tempDir.resolve("test.png").toFile().apply { createNewFile() }

        // Then: category matches FileType
        assertEquals("Images", file.getTargetCategory())
    }
}