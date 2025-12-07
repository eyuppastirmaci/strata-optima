package com.eyuppastirmaci.core.engine

import com.eyuppastirmaci.config.AppConfig
import com.eyuppastirmaci.core.analysis.FileFingerPrint
import com.eyuppastirmaci.model.DuplicateGroup
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
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
    suspend fun find(files: List<File>): Map<String, DuplicateGroup> = coroutineScope {
        val validFiles = files.filter { it.isFile }

        // Compute hashes in parallel chunks
        val fileHashPairs = validFiles
            .chunked(AppConfig.CHUNK_SIZE)
            .flatMap { chunk ->
                chunk.map { file ->
                    async { file to FileFingerPrint.generate(file) }
                }.awaitAll()
            }

        // Group by hash
        val hashGroups = fileHashPairs
            .groupBy({ it.second }, { it.first })
            .filterValues { it.size > 1 }

        hashGroups.mapValues { (_, groupedFiles) ->
            val sorted = groupedFiles.sortedBy { it.lastModified() }
            DuplicateGroup(
                original = sorted.first(),
                copies = sorted.drop(1)
            )
        }
    }
}