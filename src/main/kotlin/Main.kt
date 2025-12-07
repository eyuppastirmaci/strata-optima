package com.eyuppastirmaci

import com.eyuppastirmaci.cli.AnalyzeCommand
import com.eyuppastirmaci.cli.OrganizeCommand
import com.eyuppastirmaci.cli.StrataOptima
import com.github.ajalt.clikt.core.subcommands

fun main(args: Array<String>) {
    StrataOptima()
        .subcommands(AnalyzeCommand(), OrganizeCommand())
        .main(args)
}