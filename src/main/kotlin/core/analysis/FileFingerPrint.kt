package com.eyuppastirmaci.core.analysis

import com.eyuppastirmaci.extensions.computeRawChecksum
import com.eyuppastirmaci.model.HashAlgorithm
import java.io.File

object FileFingerPrint {

    /**
     * Generates a unique fingerprint for the given file based on the selected algorithm.
     * Defaults to MD5 if no algorithm is specified.
     */
    suspend fun generate(file: File, algorithm: HashAlgorithm = HashAlgorithm.MD5): String {
        return file.computeRawChecksum(algorithm)
    }

}
