package com.eyuppastirmaci.extensions

import com.eyuppastirmaci.config.AppConfig
import com.eyuppastirmaci.model.HashAlgorithm
import java.io.File
import java.io.FileInputStream
import java.security.MessageDigest

/**
 * Core module for generating unique file fingerprints (checksums).
 */
fun File.computeRawChecksum(algorithm: HashAlgorithm): String {
    if (!this.exists() || !this.isFile) return ""

    val digest = MessageDigest.getInstance(algorithm.algorithmName)
    val buffer = ByteArray(AppConfig.BUFFER_SIZE)

    try {
        FileInputStream(this).use { inputStream ->
            var bytesRead: Int
            while (inputStream.read(buffer).also { bytesRead = it } != -1) {
                digest.update(buffer, 0, bytesRead)
            }
        }
    } catch (e: Exception) {
        return ""
    }

    // Format as 2-digit Hex (e.g., 'f' becomes '0f') to ensure standard checksum length
    return digest.digest().joinToString("") { "%02x".format(it) }
}