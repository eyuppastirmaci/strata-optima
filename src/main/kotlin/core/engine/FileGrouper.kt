package com.eyuppastirmaci.core.engine

import com.eyuppastirmaci.model.DuplicateGroup
import java.io.File

/**
 * Handles file grouping operations.
 */
object FileGrouper {

    /**
     * Groups files by their hash values.
     * Returns only groups with duplicates (size > 1).
     */
    fun byHash(fileHashPairs: List<Pair<File, String>>): Map<String, DuplicateGroup> {
        return fileHashPairs
            .groupBy({ it.second }, { it.first })
            .filterValues { it.size > 1 }
            .mapValues { (_, files) -> toDuplicateGroup(files) }
    }

    private fun toDuplicateGroup(files: List<File>): DuplicateGroup {
        val sorted = files.sortedBy { it.lastModified() }
        return DuplicateGroup(
            original = sorted.first(),
            copies = sorted.drop(1)
        )
    }
}