package com.eyuppastirmaci.util

import java.io.File
import java.nio.file.Files
import java.nio.file.Path

/**
 * Utility functions for test file operations.
 */
object TestFileUtil {

    /**
     * Creates a temporary file with the given content.
     */
    fun createFile(dir: Path, name: String, content: String): File {
        val path = dir.resolve(name)
        Files.write(path, content.toByteArray(Charsets.UTF_8))
        return path.toFile()
    }

}