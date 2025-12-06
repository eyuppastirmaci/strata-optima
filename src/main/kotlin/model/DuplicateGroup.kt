package com.eyuppastirmaci.model

import java.io.File

/**
 * Represents a group of duplicate files.
 * The oldest file is considered the original.
 */
data class DuplicateGroup(
    val original: File,
    val copies: List<File>
) {
    val totalCount: Int get() = copies.size + 1
    val totalSize: Long get() = copies.sumOf { it.length() }
    val copyCount: Int get() = copies.size
}