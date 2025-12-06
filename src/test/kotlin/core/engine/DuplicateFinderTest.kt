package com.eyuppastirmaci.core.engine

import com.eyuppastirmaci.util.TestFileUtil.createFile
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.io.TempDir
import java.nio.file.Path

@DisplayName("DuplicateFinder")
class DuplicateFinderTest {

    @Test
    fun `should return empty map when no duplicates exist`(@TempDir tempDir: Path) {
        // Given: files with unique content
        createFile(tempDir, "a.txt", "content A")
        createFile(tempDir, "b.txt", "content B")
        val files = tempDir.toFile().listFiles()?.toList() ?: emptyList()

        // When: finding duplicates
        val result = DuplicateFinder.find(files)

        // Then: no duplicates found
        assertTrue(result.isEmpty())
    }

    @Test
    fun `should group files with identical content`(@TempDir tempDir: Path) {
        // Given: two files with same content
        createFile(tempDir, "original.txt", "same content")
        createFile(tempDir, "copy.txt", "same content")
        val files = tempDir.toFile().listFiles()?.toList() ?: emptyList()

        // When: finding duplicates
        val result = DuplicateFinder.find(files)

        // Then: returns one group with both files
        assertEquals(1, result.size)
        assertEquals(2, result.values.first().totalCount)
    }

    @Test
    fun `should identify oldest file as original`(@TempDir tempDir: Path) {
        // Given: duplicate files with different timestamps
        createFile(tempDir, "old.txt", "duplicate")
        Thread.sleep(100)
        createFile(tempDir, "new.txt", "duplicate")
        val files = tempDir.toFile().listFiles()?.toList() ?: emptyList()

        // When: finding duplicates
        val result = DuplicateFinder.find(files)

        // Then: oldest file is marked as original
        val group = result.values.first()
        assertEquals("old.txt", group.original.name)
    }

    @Test
    fun `should handle multiple duplicate groups`(@TempDir tempDir: Path) {
        // Given: two separate groups of duplicates
        createFile(tempDir, "a1.txt", "group A")
        createFile(tempDir, "a2.txt", "group A")
        createFile(tempDir, "b1.txt", "group B")
        createFile(tempDir, "b2.txt", "group B")
        val files = tempDir.toFile().listFiles()?.toList() ?: emptyList()

        // When: finding duplicates
        val result = DuplicateFinder.find(files)

        // Then: returns two groups
        assertEquals(2, result.size)
    }

    @Test
    fun `should list copies separately from original`(@TempDir tempDir: Path) {
        // Given: one original and two copies
        createFile(tempDir, "first.txt", "content")
        Thread.sleep(50)
        createFile(tempDir, "second.txt", "content")
        Thread.sleep(50)
        createFile(tempDir, "third.txt", "content")
        val files = tempDir.toFile().listFiles()?.toList() ?: emptyList()

        // When: finding duplicates
        val result = DuplicateFinder.find(files)

        // Then: one original, two copies
        val group = result.values.first()
        assertEquals("first.txt", group.original.name)
        assertEquals(2, group.copies.size)
    }
}