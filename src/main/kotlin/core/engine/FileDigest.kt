package com.eyuppastirmaci.core.engine

import com.eyuppastirmaci.config.AppConfig
import com.eyuppastirmaci.core.analysis.FileFingerPrint
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import java.io.File

/**
 * Handles file digest (hash) computation.
 */
object FileDigest {

    /**
     * Computes hashes for all files in parallel.
     * Returns list of (File, Hash) pairs.
     */
    suspend fun computeAll(files: List<File>): List<Pair<File, String>> = coroutineScope {
        files
            .chunked(AppConfig.CHUNK_SIZE)
            .flatMap { chunk ->
                chunk.map { file ->
                    async { file to FileFingerPrint.generate(file) }
                }.awaitAll()
            }
    }
}