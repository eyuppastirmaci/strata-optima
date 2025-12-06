package com.eyuppastirmaci.core.engine

import java.io.File

/**
 * Recursively scans directories and collects all files.
 */
object FileCrawler {

    /**
     * Scans a directory and returns all files (excluding directories).
     * Traverses subdirectories recursively.
     */
    fun scan(directory: File): List<File> {
        if (!directory.exists() || !directory.isDirectory) return emptyList()

        return directory.walkTopDown()
            .filter { it.isFile }
            .toList()
    }
}