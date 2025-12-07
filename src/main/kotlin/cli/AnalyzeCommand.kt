package com.eyuppastirmaci.cli

import com.eyuppastirmaci.core.engine.DuplicateFinder
import com.eyuppastirmaci.core.engine.FileCrawler
import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.options.default
import com.github.ajalt.clikt.parameters.options.flag
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.types.path
import kotlinx.coroutines.runBlocking
import java.nio.file.Path
import kotlin.io.path.pathString

/**
 * Analyzes a directory for duplicates and file statistics.
 */
class AnalyzeCommand : CliktCommand(
    name = "analyze",
    help = "Analyze a directory for duplicate files and statistics"
) {
    private val path: Path by option("-p", "--path", help = "Directory to analyze")
        .path(mustExist = true, canBeFile = false)
        .default(Path.of("."))

    private val showDuplicates: Boolean by option("-d", "--duplicates", help = "Show duplicate file details")
        .flag(default = false)

    override fun run() = runBlocking {
        val directory = path.toFile()

        echo("Analyzing: ${path.pathString}")
        echo()

        val files = FileCrawler.scan(directory)

        if (files.isEmpty()) {
            echo("No files found in directory.")
            return@runBlocking
        }

        val duplicates = DuplicateFinder.find(files)

        val totalFiles = files.size
        val totalSize = files.sumOf { it.length() }
        val duplicateGroups = duplicates.size
        val duplicateFiles = duplicates.values.sumOf { it.copyCount }
        val wastedSpace = duplicates.values.sumOf { it.totalSize }

        echo("Analysis Results")
        echo("----------------")
        echo("Total files:       $totalFiles")
        echo("Total size:        ${formatSize(totalSize)}")
        echo()
        echo("Duplicates")
        echo("----------")
        echo("Duplicate groups:  $duplicateGroups")
        echo("Duplicate files:   $duplicateFiles")
        echo("Wasted space:      ${formatSize(wastedSpace)}")

        if (showDuplicates && duplicates.isNotEmpty()) {
            echo()
            echo("Duplicate Details")
            echo("-----------------")
            duplicates.forEach { (hash, group) ->
                echo()
                echo("[${hash.take(8)}]")
                echo("  Original: ${group.original.name}")
                group.copies.forEach { copy ->
                    echo("  Copy:     ${copy.name}")
                }
            }
        }

        if (duplicates.isNotEmpty()) {
            echo()
            echo("Tip: Use 'organize' command to clean up duplicates")
        }
    }

    private fun formatSize(bytes: Long): String {
        return when {
            bytes >= 1_073_741_824 -> "%.2f GB".format(bytes / 1_073_741_824.0)
            bytes >= 1_048_576 -> "%.2f MB".format(bytes / 1_048_576.0)
            bytes >= 1_024 -> "%.2f KB".format(bytes / 1_024.0)
            else -> "$bytes bytes"
        }
    }
}