plugins {
    id("java")
    id("maven-publish")
    id("net.minecrell.plugin-yml.bukkit") version "0.6.0"
    id("com.github.johnrengelman.shadow") version "8.1.0"
}

group = "top.zhangsiyao.betterfishing"
version = "1.0.0"

description = "A fishing extension bringing an exciting new experience to fishing."

repositories {
    maven("https://lss233.littleservice.cn/repositories/minecraft")
    maven("https://maven.aliyun.com/repository/jcenter")
    maven("https://maven.aliyun.com/repository/google")
    maven("https://maven.aliyun.com/repository/central")
    maven("https://maven.aliyun.com/repository/gradle-plugin")
    mavenCentral()
    maven("https://hub.spigotmc.org/nexus/content/groups/public/")
    maven("https://github.com/deanveloper/SkullCreator/raw/mvn-repo/")
    maven("https://jitpack.io")
    maven("https://maven.enginehub.org/repo/")
    maven("https://repo.extendedclip.com/content/repositories/placeholderapi/")
    maven("https://raw.githubusercontent.com/FabioZumbi12/RedProtect/mvn-repo/")
    maven("https://libraries.minecraft.net/")
    maven("https://nexus.neetgames.com/repository/maven-releases/")
    maven("https://repo.codemc.org/repository/maven-public/")
    maven("https://repo.spongepowered.org/maven/")
    maven("https://repo.essentialsx.net/releases/")
    maven("https://repo.auxilor.io/repository/maven-public/")
}

dependencies {
    compileOnly(libs.spigot.api)
    compileOnly(libs.vault.api)
    compileOnly(libs.placeholder.api)
    compileOnly(libs.authlib)

    compileOnly(libs.worldguard.core) {
        exclude("com.sk89q.worldedit", "worldedit-core")
    }
    compileOnly(libs.worldguard.bukkit)
    compileOnly(libs.worldedit.core)
    compileOnly(libs.worldedit.bukkit)

    compileOnly(libs.redprotect.core) {
        exclude("net.ess3", "EssentialsX")
        exclude("org.spigotmc", "spigot-api")
    }
    compileOnly(libs.redprotect.spigot) {
        exclude("net.ess3", "EssentialsX")
        exclude("org.spigotmc", "spigot-api")
        exclude("com.destroystokyo.paper", "paper-api")
        exclude("de.keyle", "mypet")
        exclude("com.sk89q.worldedit", "worldedit-core")
        exclude("com.sk89q.worldedit", "worldedit-bukkit")
        exclude("com.sk89q.worldguard", "worldguard-bukkit")
    }
    compileOnly(libs.aurelium.skills)
    compileOnly(libs.griefprevention)
    compileOnly(libs.mcmmo)
    compileOnly(libs.headdatabase.api)

    compileOnly(libs.lombok)

    implementation(libs.nbt.api)
    implementation(libs.bstats)

    annotationProcessor(libs.lombok)

    library(libs.friendlyid)
    library(libs.flyway.core)
    library(libs.flyway.mysql)
    library(libs.hikaricp)
    library(libs.caffeine)
    library(libs.commons.lang3)
    library(libs.commons.codec)
    library(libs.json.simple)
}

bukkit {
    name = "BetterFishing"
    author = "ZhangSiYao"
    main = "top.zhangsiyao.betterfishing.BetterFishing"
    version = project.version.toString()
    description = project.description.toString()
    website = "https://github.com/mc23101/BetterFishing/tree/main"

    depend = listOf("Vault")
    softDepend = listOf(
        "WorldGuard",
        "PlaceholderAPI",
        "RedProtect",
        "mcMMO",
        "AureliumSkills",
        "ItemsAdder",
        "Denizens",
        "EcoItems",
        "Oraxen",
        "HeadDatabase"
    )
    loadBefore = listOf("AntiAC")
    apiVersion = "1.16"

    commands {
        register("betterfishing") {
            usage = "/<command> [name]"
            aliases = listOf("fishing")
        }
    }
}

tasks {
    build {
        dependsOn(shadowJar)
    }

    shadowJar {
        minimize()

        exclude("META-INF/**")

        archiveFileName.set("better-fishing-${project.version}.jar")
        archiveClassifier.set("shadow")

        relocate("de.tr7zw.changeme.nbtapi", "top.zhangsiyao.betterfishing.fish.utils.nbt")
        relocate("org.bstats", "top.zhangsiyao.betterfishing.bstats")
    }

}


java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(8))
        vendor.set(JvmVendorSpec.ADOPTIUM)
    }
}

