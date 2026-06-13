import org.ajoberstar.grgit.Grgit
import org.gradle.jvm.toolchain.JavaLanguageVersion
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

/*
 * Iris is a World Generator for Minecraft Bukkit Servers
 * Copyright (c) 2021 Arcane Arts (Volmit Software)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 *
 * Changes (YYYY-MM-DD):
 *  - 2026-06-13 @xIRoXaSx: Removed Kotlin scripting system (security: packs must not execute arbitrary code).
 */

plugins {
    java
    `java-library`
    alias(libs.plugins.shadow)
    alias(libs.plugins.grgit)
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.kotlin.lombok)
}

val apiVersion = "1.19"
val main = "com.volmit.iris.Iris"
val lib = "com.volmit.iris.util"

/**
 * Dependencies.
 *
 * Provided or classpath dependencies are not shaded and are available on the runtime classpath
 *
 * Shaded dependencies are not available at runtime, nor are they available on mvn central so they
 * need to be shaded into the jar (increasing binary size)
 *
 * Dynamically loaded dependencies are defined in the plugin.yml (updating these must be updated in the
 * plugin.yml also, otherwise they wont be available). These do not increase binary size). Only declare
 * these dependencies if they are available on mvn central.
 */
dependencies {
    // Provided or Classpath
    compileOnly(libs.spigot)
    compileOnly(libs.log4j.api)
    compileOnly(libs.log4j.core)

    // Third Party Integrations
    compileOnly(libs.nexo)
    compileOnly(libs.itemsadder)
    compileOnly(libs.placeholderApi)
    compileOnly(libs.score)
    compileOnly(libs.mmoitems)
    compileOnly(libs.ecoitems)
    compileOnly(libs.mythic)
    compileOnly(libs.mythicChrucible)
    compileOnly(libs.kgenerators) {
        isTransitive = false
    }
    compileOnly(libs.multiverseCore)
    compileOnly(libs.craftengine.core)
    compileOnly(libs.craftengine.bukkit)
    //compileOnly(libs.sparrowNbt)

    // Shaded into the plugin jar. Keep runtime dependency loading out of server startup.
    implementation(libs.paralithic)
    implementation(libs.paperlib)
    implementation(libs.adventure.api)
    implementation(libs.adventure.minimessage)
    implementation(libs.adventure.platform)
    implementation(libs.bstats)

    implementation(libs.commons.io)
    implementation(libs.commons.lang)
    implementation(libs.commons.lang3)
    implementation(libs.commons.math3)
    implementation(libs.oshi)
    implementation(libs.lz4)
    implementation(libs.fastutil)
    implementation(libs.lru)
    implementation(libs.zip)
    implementation(libs.gson)
    implementation(libs.asm)
    implementation(libs.caffeine)
    implementation(libs.byteBuddy.core)
    implementation(libs.byteBuddy.agent)
    implementation(libs.dom4j)
    implementation(libs.jaxen)

    implementation(libs.kotlin.stdlib)
    implementation(libs.kotlin.coroutines)
}

java {
    disableAutoTargetJvm()
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
    }
}

kotlin {
    jvmToolchain(21)
    compilerOptions {
        jvmTarget.set(JvmTarget.JVM_21)
    }
}

tasks {
    /**
     * We need parameter meta for the decree command system
     */
    compileJava {
        options.compilerArgs.add("-parameters")
        options.encoding = "UTF-8"
    }

    /**
     * Expand properties into plugin yml
     */
    processResources {
        inputs.properties(
            "name" to rootProject.name,
            "version" to rootProject.version,
            "apiVersion" to apiVersion,
            "main" to main,
        )
        filesMatching("**/plugin.yml") {
            expand(inputs.properties)
        }
    }

    shadowJar {
        mergeServiceFiles()
        //minimize()
        relocate("com.dfsek.paralithic", "$lib.paralithic")
        relocate("io.papermc.lib", "$lib.paper")
        relocate("net.kyori", "$lib.kyori")
        relocate("org.bstats", "$lib.metrics")
        relocate("org.dom4j", "$lib.dom4j")
        relocate("org.jaxen", "$lib.jaxen")
        relocate("com.github.benmanes.caffeine", "$lib.caffeine")
        exclude("modules/loader-agent.isolated-jar")
    }

}

val templateSource = file("src/main/templates")
val templateDest = layout.buildDirectory.dir("generated/sources/templates")!!
val generateTemplates = tasks.register<Copy>("generateTemplates") {
    inputs.properties(
        "environment" to when {
            project.hasProperty("release") -> "production"
            project.hasProperty("argghh") -> "Argghh!"
            else -> "development"
        },
        "commit" to provider {
            val res = runCatching { project.extensions.getByType<Grgit>().head().id }
            res.getOrDefault("")
                .takeIf { it.length == 40 } ?: run {
                this.logger.error("Git commit hash not found", res.exceptionOrNull())
                "unknown"
            }
        },
    )

    from(templateSource)
    into(templateDest)
    rename { "com/volmit/iris/$it" }
    expand(inputs.properties)
}

rootProject.tasks.named("prepareKotlinBuildScriptModel") {
    dependsOn(generateTemplates)
}

sourceSets.main {
    java.srcDir(generateTemplates.map { it.outputs })
}
