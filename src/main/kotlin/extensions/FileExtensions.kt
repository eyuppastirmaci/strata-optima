package com.eyuppastirmaci.extensions

import com.eyuppastirmaci.config.AppConfig
import com.eyuppastirmaci.model.HashAlgorithm
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileInputStream
import java.security.MessageDigest

/**
 * Core module for generating unique file fingerprints (checksums).
 */
suspend fun File.computeRawChecksum(algorithm: HashAlgorithm): String = withContext(Dispatchers.IO) {
    if (!exists() || !isFile) return@withContext ""

    val digest = MessageDigest.getInstance(algorithm.algorithmName)
    val buffer = ByteArray(AppConfig.BUFFER_SIZE)

    try {
        FileInputStream(this@computeRawChecksum).use { inputStream ->
            var bytesRead: Int
            while (inputStream.read(buffer).also { bytesRead = it } != -1) {
                digest.update(buffer, 0, bytesRead)
            }
        }
    } catch (e: Exception) {
        return@withContext ""
    }

    digest.digest().joinToString("") { "%02x".format(it) }
}