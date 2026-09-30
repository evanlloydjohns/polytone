plugins {
    id("com.possible-triangle.common")
}

// The 26.2 shader sources are retained for the later renderer port, but must
// not be loaded by Minecraft 26.3 in this limited alpha.
tasks.withType<org.gradle.language.jvm.tasks.ProcessResources>().configureEach {
    exclude("assets/polytone/shaders/**")
}

common {
    //pinned so the build doesn't need to hit maven.neoforged.net to list versions
    neoformVersion = "26.3-1"
    accessWidener()
}

val candlelight_version: String by extra
val exp4j_version: String by extra
val nexp_version: String by extra
val codecui_version: String by extra
val nautilus_studio_version: String by extra
val packed_packs_neoforge_version: String by extra
val packed_packs_api_version: String by extra


dependencies {
    compileOnly ("net.mehvahdjukaar:codecui-common:${codecui_version}")
    compileOnly ("net.mehvahdjukaar:nautilus_studio-common:${nautilus_studio_version}")

    implementation ("net.objecthunter:exp4j:${exp4j_version}")
    implementation ("hollowpoint:nexp:${nexp_version}")

    modCompileOnly("maven.modrinth:packed-packs:${packed_packs_neoforge_version}")
    compileOnly("io.github.fishstiz.packed_packs.api:packed_packs_api-neoforge:${packed_packs_api_version}")
    compileOnly(files(layout.buildDirectory.file("sodium/sodium-neoforge-mod.jar")).builtBy(tasks.named("extractSodiumNeoforge")))

    // modCompileOnly("curse.maven:entity-model-features-844662:7400754")
    // modCompileOnly("curse.maven:entity-texture-features-fabric-568563:7392425")
}
