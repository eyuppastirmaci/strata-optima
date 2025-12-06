package com.eyuppastirmaci.core.engine

import com.eyuppastirmaci.core.analysis.FileFingerPrint
import com.eyuppastirmaci.model.DuplicateGroup
import java.io.File

/**
 * Finds duplicate files based on content hash.
 * Groups identical files and identifies original vs copies.
 */
object DuplicateFinder {

    /**
     * Finds all duplicate files in the given list.
     * Returns a map of hash -> DuplicateGroup (only for files with duplicates).
     */
    fun find(files: List<File>): Map<String, DuplicateGroup> {
        // Group files by their content hash
        val hashGroups = files
            .filter { it.isFile }
            .groupBy { FileFingerPrint.generate(it) }
            .filterValues { it.size > 1 } // Keep only duplicates

        // Convert to DuplicateGroup (oldest = original)
        return hashGroups.mapValues { (_, groupedFiles) ->
            val sorted = groupedFiles.sortedBy { it.lastModified() }
            DuplicateGroup(
                original = sorted.first(),
                copies = sorted.drop(1)
            )
        }
    }
}