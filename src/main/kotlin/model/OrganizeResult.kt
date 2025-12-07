package com.eyuppastirmaci.model

import java.io.File

/**
 * Represents the result of a file organize operation.
 */
data class OrganizeResult(
    val sourceFile: File,
    val targetPath: String,
    val mode: Mode
) {
    enum class Mode {
        DRY_RUN,    // Simulated, no actual changes
        EXECUTED    // Actually moved/renamed
    }

    val isDryRun: Boolean get() = mode == Mode.DRY_RUN
}