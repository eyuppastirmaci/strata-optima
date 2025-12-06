# StrataOptima

A high-performance CLI tool for organizing messy directories. It detects duplicate files by content, categorizes files by type, and cleans up disk space.

## Features

- **Duplicate Detection** - Finds identical files using MD5/SHA-256 hash, regardless of filename
- **Smart Categorization** - Sorts files into logical folders (Images, Documents, Videos, etc.)
- **Dry-Run Mode** - Preview changes before applying them
- **HTML Reports** - Generates detailed operation reports

## Build

```bash
./gradlew build
```

## Usage

```bash
# Analyze a directory
strataoptima analyze --path "./Downloads"

# Organize files
strataoptima organize --path "./Downloads" --strategy DATE_AND_TYPE

# Preview without changes
strataoptima organize --path "./Downloads" --dry-run
```

## Running Tests

```bash
# Run all tests
./gradlew test

# Run specific test class
./gradlew test --tests "com.eyuppastirmaci.core.engine.FileCrawlerTest"

# Run with verbose output
./gradlew test --info
```

Under active development
