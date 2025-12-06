package com.eyuppastirmaci.model

/**
 * Supported hashing algorithms for file analysis.
 */
enum class HashAlgorithm(val algorithmName: String) {
    MD5("MD5"),
    SHA256("SHA-256"),
    SHA1("SHA-1")
}