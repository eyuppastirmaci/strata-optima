package com.eyuppastirmaci.cli

import com.github.ajalt.clikt.core.CliktCommand

/**
 * Main CLI application entry point.
 */
class StrataOptima : CliktCommand(
    name = "strataoptima",
    help = "A CLI tool for detecting duplicates and organizing files"
) {
    override fun run() = Unit
}