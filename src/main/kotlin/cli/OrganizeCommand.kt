package com.eyuppastirmaci.cli

import com.eyuppastirmaci.core.engine.FileCrawler
import com.eyuppastirmaci.core.process.FileOrganizer
import com.eyuppastirmaci.model.OrganizeResult
import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.options.default
import com.github.ajalt.clikt.parameters.options.flag
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.options.required
import com.github.ajalt.clikt.parameters.types.path
import kotlinx.coroutines.runBlocking
import java.nio.file.Path
import kotlin.io.path.pathString

/**
 * Organizes files into categorized folders.
 * Default is dry-run (simulation) for safety.
 */
class OrganizeCommand : CliktCommand(
    name = "organize",
    help = "Organize files into categorized folders (Images, Documents, etc.)"
) {
    private val sourcePath: Path by option("-p", "--path", help = "Source directory to organize")
        .path(mustExist = true, canBeFile = false)
        .required()

    private val targetPath: Path by option("-t", "--target", help = "Target directory for organized files")
        .path()
        .default(Path.of("./organized"))

    private val execute: Boolean by option("-e", "--execute", help = "Actually move files (default is dry-run)")
        .flag(default = false)

    override fun run() = runBlocking {
        val sourceDir = sourcePath.toFile()
        val targetDir = targetPath.toFile()
        val dryRun = !execute

        if (dryRun) {
            echo("Mode: DRY-RUN (simulation only)")
            echo("Add --execute to actually move files")
        } else {
            echo("Mode: EXECUTE (files will be moved)")
        }
        echo()
        echo("Source: ${sourcePath.pathString}")
        echo("Target: ${targetPath.pathString}")
        echo()

        val files = FileCrawler.scan(sourceDir)

        if (files.isEmpty()) {
            echo("No files found in source directory.")
            return@runBlocking
        }

        echo("Processing ${files.size} files...")
        echo()

        var successCount = 0
        var failCount = 0
        val results = mutableListOf<OrganizeResult>()

        files.forEach { file ->
            val result = FileOrganizer.organize(file, targetDir, dryRun)

            result.fold(
                onSuccess = { organizeResult ->
                    successCount++
                    results.add(organizeResult)
                    val status = if (dryRun) "->" else "OK"
                    echo("  [$status] ${file.name} -> ${getRelativePath(organizeResult.targetPath, targetDir.absolutePath)}")
                },
                onFailure = { error ->
                    failCount++
                    echo("  [FAIL] ${file.name}: ${error.message}")
                }
            )
        }

        echo()
        echo("Summary")
        echo("-------")
        if (dryRun) {
            echo("Would organize: $successCount files")
        } else {
            echo("Organized: $successCount files")
        }

        if (failCount > 0) {
            echo("Failed: $failCount files")
        }

        if (dryRun && successCount > 0) {
            echo()
            echo("Run with --execute to apply these changes")
        }
    }

    private fun getRelativePath(fullPath: String, basePath: String): String {
        return fullPath.removePrefix(basePath).removePrefix("/").removePrefix("\\")
    }
}