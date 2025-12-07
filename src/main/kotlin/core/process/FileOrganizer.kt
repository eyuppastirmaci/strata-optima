package com.eyuppastirmaci.core.process

import com.eyuppastirmaci.extensions.getTargetCategory
import com.eyuppastirmaci.model.OrganizeResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.nio.file.Files
import java.nio.file.StandardCopyOption

/**
 * Organizes files into categorized folders based on file type.
 * Default behavior is dry-run (simulation) for safety.
 */
object FileOrganizer {

    /**
     * Organizes a file into the appropriate category folder.
     *
     * @param source The file to organize
     * @param targetDir The target directory
     * @param dryRun If true (default), simulates without moving. If false, actually moves the file.
     */
    suspend fun organize(
        source: File,
        targetDir: File,
        dryRun: Boolean = true
    ): Result<OrganizeResult> = withContext(Dispatchers.IO) {
        if (!source.exists() || !source.isFile) {
            return@withContext Result.failure(IllegalArgumentException("Source file does not exist"))
        }

        try {
            val category = source.getTargetCategory()
            val categoryDir = targetDir.resolve(category)
            val targetFile = resolveCollision(categoryDir, source.name)

            if (dryRun) {
                // Simulation only - don't move anything
                Result.success(
                    OrganizeResult(
                        sourceFile = source,
                        targetPath = targetFile.absolutePath,
                        mode = OrganizeResult.Mode.DRY_RUN
                    )
                )
            } else {
                // Actually move the file
                categoryDir.mkdirs()
                Files.move(source.toPath(), targetFile.toPath(), StandardCopyOption.REPLACE_EXISTING)
                Result.success(
                    OrganizeResult(
                        sourceFile = source,
                        targetPath = targetFile.absolutePath,
                        mode = OrganizeResult.Mode.EXECUTED
                    )
                )
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Resolves filename collision by appending _1, _2, etc.
     */
    private fun resolveCollision(directory: File, fileName: String): File {
        var target = directory.resolve(fileName)

        if (!target.exists()) return target

        val nameWithoutExt = fileName.substringBeforeLast(".")
        val extension = fileName.substringAfterLast(".", "")
        val extPart = if (extension.isNotEmpty()) ".$extension" else ""

        var counter = 1
        while (target.exists()) {
            target = directory.resolve("${nameWithoutExt}_$counter$extPart")
            counter++
        }

        return target
    }
}