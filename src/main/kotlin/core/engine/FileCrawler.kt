package com.eyuppastirmaci.core.engine

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File

/**
 * Recursively scans directories and collects all files.
 */
object FileCrawler {

    /**
     * Scans a directory and returns all files (excluding directories).
     * Traverses subdirectories recursively.
     */
    suspend fun scan(directory: File): List<File> = withContext(Dispatchers.IO) {
        if (!directory.exists() || !directory.isDirectory) return@withContext emptyList()

        directory.walkTopDown()
            .filter { it.isFile }
            .toList()
    }
}