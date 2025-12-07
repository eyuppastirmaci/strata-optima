package com.eyuppastirmaci.core.engine

import com.eyuppastirmaci.model.DuplicateGroup
import java.io.File

/**
 * Finds duplicate files based on content hash.
 */
object DuplicateFinder {

    /**
     * Finds all duplicate files in the given list.
     * Returns a map of hash -> DuplicateGroup.
     */
    suspend fun find(files: List<File>): Map<String, DuplicateGroup> {
        val validFiles = files.filter { it.isFile }
        val fileHashPairs = FileDigest.computeAll(validFiles)
        return FileGrouper.byHash(fileHashPairs)
    }
}