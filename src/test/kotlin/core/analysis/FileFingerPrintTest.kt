package com.eyuppastirmaci.core.analysis

import com.eyuppastirmaci.model.HashAlgorithm
import com.eyuppastirmaci.util.TestFileUtil.createFile
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.io.TempDir
import java.io.File
import java.nio.file.Path

@DisplayName("FileFingerPrint")
class FileFingerPrintTest {

    @Nested
    @DisplayName("MD5 Algorithm")
    inner class Md5Tests {

        @Test
        fun `should compute correct MD5 checksum`(@TempDir tempDir: Path) = runTest {
            // Given: a file with known content
            val file = createFile(tempDir, "test.txt", "Refactoring is Fun")
            val expected = "6cf5b0650064a7ec46942e72e92e612e"

            // When: generating MD5 fingerprint
            val actual = FileFingerPrint.generate(file, HashAlgorithm.MD5)

            // Then: checksum matches expected value
            assertEquals(expected, actual)
        }

        @Test
        fun `should use MD5 as default algorithm`(@TempDir tempDir: Path) = runTest {
            // Given: a file with known content
            val file = createFile(tempDir, "default.txt", "Hello")
            val expectedMd5 = FileFingerPrint.generate(file, HashAlgorithm.MD5)

            // When: generating fingerprint without specifying algorithm
            val actual = FileFingerPrint.generate(file)

            // Then: result matches MD5 output
            assertEquals(expectedMd5, actual)
        }
    }

    @Nested
    @DisplayName("SHA-256 Algorithm")
    inner class Sha256Tests {

        @Test
        fun `should compute correct SHA-256 checksum`(@TempDir tempDir: Path) = runTest {
            // Given: a file with known content
            val file = createFile(tempDir, "sha256.txt", "Kotlin")
            val expected = "c78f6c97923e81a2f04f09c5e87b69e085c1e47066a1136b5f590bfde696e2eb"

            // When: generating SHA-256 fingerprint
            val actual = FileFingerPrint.generate(file, HashAlgorithm.SHA256)

            // Then: checksum matches expected value
            assertEquals(expected, actual)
        }
    }

    @Nested
    @DisplayName("SHA-1 Algorithm")
    inner class Sha1Tests {

        @Test
        fun `should compute correct SHA-1 checksum`(@TempDir tempDir: Path) = runTest {
            // Given: a file with known content
            val file = createFile(tempDir, "sha1.txt", "Test")
            val expected = "640ab2bae07bedc4c163f679a746f7ab7fb5d1fa"

            // When: generating SHA-1 fingerprint
            val actual = FileFingerPrint.generate(file, HashAlgorithm.SHA1)

            // Then: checksum matches expected value
            assertEquals(expected, actual)
        }
    }

    @Nested
    @DisplayName("Edge Cases")
    inner class EdgeCaseTests {

        @Test
        fun `should return empty string for non-existent file`() = runTest {
            // Given: a file that does not exist
            val file = File("non_existent_file.txt")

            // When: attempting to generate fingerprint
            val result = FileFingerPrint.generate(file, HashAlgorithm.MD5)

            // Then: returns empty string
            assertEquals("", result)
        }

        @Test
        fun `should return empty string for directory`(@TempDir tempDir: Path) = runTest {
            // Given: a directory instead of a file
            val directory = tempDir.toFile()

            // When: attempting to generate fingerprint
            val result = FileFingerPrint.generate(directory, HashAlgorithm.MD5)

            // Then: returns empty string
            assertEquals("", result)
        }

        @Test
        fun `should handle empty file`(@TempDir tempDir: Path) = runTest {
            // Given: an empty file
            val file = createFile(tempDir, "empty.txt", "")
            val expectedMd5 = "d41d8cd98f00b204e9800998ecf8427e"

            // When: generating fingerprint
            val actual = FileFingerPrint.generate(file, HashAlgorithm.MD5)

            // Then: returns valid hash for empty content
            assertEquals(expectedMd5, actual)
        }

        @Test
        fun `should produce 32 character hex string for MD5`(@TempDir tempDir: Path) = runTest {
            // Given: any file with content
            val file = createFile(tempDir, "length.txt", "any content")

            // When: generating MD5 fingerprint
            val result = FileFingerPrint.generate(file, HashAlgorithm.MD5)

            // Then: output is 32 hex characters
            assertEquals(32, result.length)
            assertTrue(result.matches(Regex("[a-f0-9]{32}")))
        }

        @Test
        fun `should produce 64 character hex string for SHA-256`(@TempDir tempDir: Path) = runTest {
            // Given: any file with content
            val file = createFile(tempDir, "length256.txt", "any content")

            // When: generating SHA-256 fingerprint
            val result = FileFingerPrint.generate(file, HashAlgorithm.SHA256)

            // Then: output is 64 hex characters
            assertEquals(64, result.length)
            assertTrue(result.matches(Regex("[a-f0-9]{64}")))
        }
    }

    @Nested
    @DisplayName("Consistency")
    inner class ConsistencyTests {

        @Test
        fun `should produce identical hash for same content`(@TempDir tempDir: Path) = runTest {
            // Given: two files with identical content
            val content = "duplicate content"
            val file1 = createFile(tempDir, "file1.txt", content)
            val file2 = createFile(tempDir, "file2.txt", content)

            // When: generating fingerprints for both
            val hash1 = FileFingerPrint.generate(file1, HashAlgorithm.MD5)
            val hash2 = FileFingerPrint.generate(file2, HashAlgorithm.MD5)

            // Then: hashes are identical
            assertEquals(hash1, hash2)
        }

        @Test
        fun `should produce different hash for different content`(@TempDir tempDir: Path) = runTest {
            // Given: two files with different content
            val file1 = createFile(tempDir, "diff1.txt", "content A")
            val file2 = createFile(tempDir, "diff2.txt", "content B")

            // When: generating fingerprints for both
            val hash1 = FileFingerPrint.generate(file1, HashAlgorithm.MD5)
            val hash2 = FileFingerPrint.generate(file2, HashAlgorithm.MD5)

            // Then: hashes are different
            assertTrue(hash1 != hash2)
        }
    }
}