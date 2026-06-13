package com.volmit.iris.core.scripting.kotlin.runner.resolver

import java.io.File
import java.nio.file.InvalidPathException
import java.nio.file.Path
import java.util.concurrent.ConcurrentHashMap
import kotlin.script.experimental.api.ResultWithDiagnostics
import kotlin.script.experimental.api.SourceCode
import kotlin.script.experimental.api.asSuccess
import kotlin.script.experimental.dependencies.ExternalDependenciesResolver
import kotlin.script.experimental.dependencies.RepositoryCoordinates
import kotlin.script.experimental.dependencies.impl.makeResolveFailureResult
import kotlin.script.experimental.dependencies.impl.toRepositoryUrlOrNull

class FileDependenciesResolver(
    private val baseDir: File,
) : DependenciesResolver {
    private val localRepos = ConcurrentHashMap.newKeySet<File>(1).also { it.add(baseDir) }

    private fun String.toRepositoryFileOrNull(): File? =
        safeRelativePath()
            ?.let { baseDir.resolveSecure(it) }
            ?.takeIf { it.exists() && it.isDirectory }

    private fun RepositoryCoordinates.toFilePath() =
        (this.toRepositoryUrlOrNull()?.takeIf { it.protocol == "file" }?.path ?: string).toRepositoryFileOrNull()

    private fun String.safeRelativePath(): Path? = try {
        takeIf { it.isNotBlank() && !it.contains('\u0000') }
            ?.let(Path::of)
            ?.normalize()
            ?.takeIf { !it.isAbsolute && it.none { part -> part.toString() == ".." } }
    } catch (_: InvalidPathException) {
        null
    }

    private fun File.resolveSecure(path: Path): File? {
        val root = canonicalFile
        val resolved = root.toPath().resolve(path).normalize().toFile().canonicalFile
        return resolved.takeIf { it.toPath().startsWith(root.toPath()) }
    }

    override fun addRepository(
        repositoryCoordinates: RepositoryCoordinates,
        options: ExternalDependenciesResolver.Options,
        sourceCodeLocation: SourceCode.LocationWithId?
    ): ResultWithDiagnostics<Boolean> {
        if (!acceptsRepository(repositoryCoordinates)) return false.asSuccess()

        val repoDir = repositoryCoordinates.toFilePath()
            ?: return makeResolveFailureResult("Invalid repository location: '${repositoryCoordinates}'", sourceCodeLocation)

        localRepos.add(repoDir)

        return true.asSuccess()
    }

    override suspend fun resolve(
        artifactCoordinates: String,
        options: ExternalDependenciesResolver.Options,
        sourceCodeLocation: SourceCode.LocationWithId?
    ): ResultWithDiagnostics<List<File>> {
        val artifactPath = artifactCoordinates.safeRelativePath()
            ?: return makeResolveFailureResult("Invalid dependency path: '$artifactCoordinates'", sourceCodeLocation)
        if (!artifactCoordinates.endsWith(".jar", ignoreCase = true)) {
            return makeResolveFailureResult("Only local .jar script dependencies are allowed: '$artifactCoordinates'", sourceCodeLocation)
        }

        val messages = mutableListOf<String>()

        for (repo in localRepos) {
            val file = repo.resolveSecure(artifactPath)
                ?: run {
                    messages.add("Path '$artifactCoordinates' escapes repository '$repo'")
                    continue
                }
            when {
                !file.exists() -> messages.add("File '$file' not found")
                !file.isFile -> messages.add("Path '$file' is not a file")
                else -> return ResultWithDiagnostics.Success(listOf(file))
            }
        }
        return makeResolveFailureResult(messages.joinToString("; "), sourceCodeLocation)
    }

    override fun acceptsArtifact(artifactCoordinates: String) =
        artifactCoordinates.safeRelativePath() != null && artifactCoordinates.endsWith(".jar", ignoreCase = true)

    override fun acceptsRepository(repositoryCoordinates: RepositoryCoordinates): Boolean = repositoryCoordinates.toFilePath() != null

    override fun addPack(directory: File) {
        localRepos.add(directory)
    }

}
