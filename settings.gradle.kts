rootProject.name = "better-fishing"

include(":better-fishing-plugin")

dependencyResolutionManagement {
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
    versionCatalogs {
        create("libs") {
            library("taboolib","com.github.TabooLib:TabooLib:6.0.12-20")
            library("spigot-api", "org.spigotmc:spigot-api:1.12.2-R0.1-SNAPSHOT")
            library("vault-api", "com.github.MilkBowl:VaultAPI:1.7.1")
            library("placeholder-api", "me.clip:placeholderapi:2.11.3")
            version("redprotect", "7.7.2")
            library("redprotect-core", "br.net.fabiozumbi12.RedProtect","RedProtect-Core").versionRef("redprotect")
            library("redprotect-spigot", "br.net.fabiozumbi12.RedProtect","RedProtect-Spigot-1.12").versionRef("redprotect")

            library("itemsadder-api", "com.github.LoneDev6:API-ItemsAdder:3.5.0b")
            library("nbt-api", "de.tr7zw:item-nbt-api:2.11.3")
            library("denizens-api", "com.denizenscript:denizen:1.2.5-SNAPSHOT") // We must use 1.2.6 until we upgrade to java 17
            library("oraxen", "com.github.oraxen:oraxen:1.159.0")

            library("ecoitems-api", "com.willfp:EcoItems:5.6.1")
            library("ecoitems-libreforge", "com.willfp:libreforge:4.21.1")
            library("ecoitems-eco", "com.willfp:eco:6.65.1")

            library("commons-lang3", "org.apache.commons:commons-lang3:3.13.0")
            library("commons-codec", "commons-codec:commons-codec:1.16.0")
            library("annotations", "org.jetbrains:annotations:24.0.1")
            library("lombok","org.projectlombok:lombok:1.18.28")

            version("flyway", "9.19.4")
            library("flyway-core", "org.flywaydb","flyway-core").versionRef("flyway")
            library("flyway-mysql", "org.flywaydb","flyway-mysql").versionRef("flyway")

            library("friendlyid", "com.devskiller.friendly-id:friendly-id:1.1.0")
            library("hikaricp", "com.zaxxer:HikariCP:4.0.3") //We must use 4.0.3 until we upgrade to java 17
            library("json-simple", "com.googlecode.json-simple:json-simple:1.1.1")
        }
    }
}

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.5.0"
}