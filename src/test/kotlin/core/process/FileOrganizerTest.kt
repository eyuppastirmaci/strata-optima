package com.eyuppastirmaci.core.process

import com.eyuppastirmaci.model.OrganizeResult
import com.eyuppastirmaci.util.TestFileUtil.createFile
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.io.TempDir
import java.io.File
import java.nio.file.Path

@DisplayName("FileOrganizer")
class FileOrganizerTest {

    @Nested
    @DisplayName("Dry-Run Mode (Default)")
    inner class DryRunTests {

        @Test
        fun `should NOT move file in dry-run mode`(@TempDir tempDir: Path) = runTest {
            // Given: an image file
            val source = createFile(tempDir, "photo.jpg", "image content")
            val targetDir = tempDir.resolve("organized").toFile().apply { mkdir() }

            // When: organizing with dry-run (default)
            val result = FileOrganizer.organize(source, targetDir)

            // Then: file NOT moved, still exists at source
            assertTrue(result.isSuccess)
            assertTrue(source.exists())
            assertFalse(targetDir.resolve("Images/photo.jpg").exists())
            assertEquals(OrganizeResult.Mode.DRY_RUN, result.getOrNull()?.mode)
        }

        @Test
        fun `should return simulated target path`(@TempDir tempDir: Path) = runTest {
            // Given: a document file
            val source = createFile(tempDir, "report.pdf", "pdf content")
            val targetDir = tempDir.resolve("organized").toFile().apply { mkdir() }

            // When: organizing with dry-run
            val result = FileOrganizer.organize(source, targetDir)

            // Then: result contains expected target path
            val organizeResult = result.getOrNull()!!
            assertTrue(organizeResult.targetPath.contains("Documents"))
            assertTrue(organizeResult.targetPath.endsWith("report.pdf"))
            assertTrue(organizeResult.isDryRun)
        }

        @Test
        fun `should detect collision in dry-run mode`(@TempDir tempDir: Path) = runTest {
            // Given: target already has file with same name
            val source = createFile(tempDir, "photo.jpg", "new content")
            val targetDir = tempDir.resolve("organized").toFile().apply { mkdir() }
            val imagesDir = targetDir.resolve("Images").apply { mkdir() }
            createFile(imagesDir.toPath(), "photo.jpg", "existing")

            // When: dry-run
            val result = FileOrganizer.organize(source, targetDir)

            // Then: simulated path shows _1 suffix
            val organizeResult = result.getOrNull()!!
            assertTrue(organizeResult.targetPath.endsWith("photo_1.jpg"))
            assertTrue(source.exists())
        }
    }

    @Nested
    @DisplayName("Execute Mode")
    inner class ExecuteTests {

        @Test
        fun `should move image to Images folder`(@TempDir tempDir: Path) = runTest {
            // Given: an image file
            val source = createFile(tempDir, "photo.jpg", "image content")
            val targetDir = tempDir.resolve("organized").toFile().apply { mkdir() }

            // When: organizing with execute mode
            val result = FileOrganizer.organize(source, targetDir, dryRun = false)

            // Then: file moved to Images subfolder
            assertTrue(result.isSuccess)
            assertTrue(targetDir.resolve("Images/photo.jpg").exists())
            assertFalse(source.exists())
            assertEquals(OrganizeResult.Mode.EXECUTED, result.getOrNull()?.mode)
        }

        @Test
        fun `should move document to Documents folder`(@TempDir tempDir: Path) = runTest {
            // Given: a document file
            val source = createFile(tempDir, "report.pdf", "pdf content")
            val targetDir = tempDir.resolve("organized").toFile().apply { mkdir() }

            // When: organizing with execute mode
            val result = FileOrganizer.organize(source, targetDir, dryRun = false)

            // Then: file moved to Documents subfolder
            assertTrue(result.isSuccess)
            assertTrue(targetDir.resolve("Documents/report.pdf").exists())
        }

        @Test
        fun `should rename file when name collision occurs`(@TempDir tempDir: Path) = runTest {
            // Given: target already has file with same name
            val source = createFile(tempDir, "photo.jpg", "new content")
            val targetDir = tempDir.resolve("organized").toFile().apply { mkdir() }
            val imagesDir = targetDir.resolve("Images").apply { mkdir() }
            createFile(imagesDir.toPath(), "photo.jpg", "existing content")

            // When: organizing with execute mode
            val result = FileOrganizer.organize(source, targetDir, dryRun = false)

            // Then: file renamed with suffix
            assertTrue(result.isSuccess)
            assertTrue(targetDir.resolve("Images/photo_1.jpg").exists())
        }

        @Test
        fun `should handle multiple collisions`(@TempDir tempDir: Path) = runTest {
            // Given: target has photo.jpg and photo_1.jpg
            val source = createFile(tempDir, "photo.jpg", "third content")
            val targetDir = tempDir.resolve("organized").toFile().apply { mkdir() }
            val imagesDir = targetDir.resolve("Images").apply { mkdir() }
            createFile(imagesDir.toPath(), "photo.jpg", "first")
            createFile(imagesDir.toPath(), "photo_1.jpg", "second")

            // When: organizing with execute mode
            val result = FileOrganizer.organize(source, targetDir, dryRun = false)

            // Then: file renamed to photo_2.jpg
            assertTrue(result.isSuccess)
            assertTrue(targetDir.resolve("Images/photo_2.jpg").exists())
        }

        @Test
        fun `should create category folder if not exists`(@TempDir tempDir: Path) = runTest {
            // Given: target directory without category subfolder
            val source = createFile(tempDir, "song.mp3", "audio content")
            val targetDir = tempDir.resolve("organized").toFile().apply { mkdir() }

            // When: organizing with execute mode
            val result = FileOrganizer.organize(source, targetDir, dryRun = false)

            // Then: Audio folder created and file moved
            assertTrue(result.isSuccess)
            assertTrue(targetDir.resolve("Audio").isDirectory)
            assertTrue(targetDir.resolve("Audio/song.mp3").exists())
        }

        @Test
        fun `should move unknown type to Other folder`(@TempDir tempDir: Path) = runTest {
            // Given: file with unknown extension
            val source = createFile(tempDir, "data.xyz", "unknown content")
            val targetDir = tempDir.resolve("organized").toFile().apply { mkdir() }

            // When: organizing with execute mode
            val result = FileOrganizer.organize(source, targetDir, dryRun = false)

            // Then: file moved to Other subfolder
            assertTrue(result.isSuccess)
            assertTrue(targetDir.resolve("Other/data.xyz").exists())
        }
    }

    @Nested
    @DisplayName("Error Handling")
    inner class ErrorTests {

        @Test
        fun `should return failure for non-existent source`(@TempDir tempDir: Path) = runTest {
            // Given: non-existent source file
            val source = tempDir.resolve("ghost.txt").toFile()
            val targetDir = tempDir.resolve("organized").toFile().apply { mkdir() }

            // When: organizing
            val result = FileOrganizer.organize(source, targetDir)

            // Then: returns failure
            assertTrue(result.isFailure)
        }
    }
}