package com.eyuppastirmaci.core.engine

import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.io.TempDir
import java.io.File
import java.nio.file.Path

@DisplayName("FileCrawler")
class FileCrawlerTest {

    @Test
    fun `should return empty list for empty directory`(@TempDir tempDir: Path) = runTest {
        // Given: an empty directory
        val directory = tempDir.toFile()

        // When: crawling the directory
        val result = FileCrawler.scan(directory)

        // Then: returns empty list
        assertTrue(result.isEmpty())
    }

    @Test
    fun `should find single file in directory`(@TempDir tempDir: Path) = runTest {
        // Given: a directory with one file
        val file = tempDir.resolve("test.txt").toFile().apply { createNewFile() }

        // When: crawling the directory
        val result = FileCrawler.scan(tempDir.toFile())

        // Then: returns that file
        assertEquals(1, result.size)
        assertEquals(file.name, result.first().name)
    }

    @Test
    fun `should find files in nested directories`(@TempDir tempDir: Path) = runTest {
        // Given: nested directory structure with files
        tempDir.resolve("root.txt").toFile().createNewFile()
        val subDir = tempDir.resolve("subdir").toFile().apply { mkdir() }
        subDir.resolve("nested.txt").createNewFile()

        // When: crawling the directory
        val result = FileCrawler.scan(tempDir.toFile())

        // Then: finds all files recursively
        assertEquals(2, result.size)
    }

    @Test
    fun `should return only files not directories`(@TempDir tempDir: Path) = runTest {
        // Given: directory containing both files and subdirectories
        tempDir.resolve("file.txt").toFile().createNewFile()
        tempDir.resolve("folder").toFile().mkdir()

        // When: crawling the directory
        val result = FileCrawler.scan(tempDir.toFile())

        // Then: returns only files
        assertEquals(1, result.size)
        assertTrue(result.all { it.isFile })
    }

    @Test
    fun `should return empty list for non-existent directory`() = runTest {
        // Given: a directory that doesn't exist
        val nonExistent = File("/non/existent/path")

        // When: crawling the directory
        val result = FileCrawler.scan(nonExistent)

        // Then: returns empty list
        assertTrue(result.isEmpty())
    }
}
