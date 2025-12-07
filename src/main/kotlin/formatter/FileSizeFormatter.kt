package com.eyuppastirmaci.formatter

/**
 * Utility class for formatting file sizes into human-readable strings.
 */
object FileSizeFormatter {

    /**
     * Formats a byte count into a human-readable string (bytes, KB, MB, GB).
     *
     * @param bytes The size in bytes
     * @return Formatted string (e.g., "1.50 MB")
     */
    fun format(bytes: Long): String {
        return when {
            bytes >= 1_073_741_824 -> "%.2f GB".format(bytes / 1_073_741_824.0)
            bytes >= 1_048_576 -> "%.2f MB".format(bytes / 1_048_576.0)
            bytes >= 1_024 -> "%.2f KB".format(bytes / 1_024.0)
            else -> "$bytes bytes"
        }
    }
}