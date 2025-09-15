import org.gradle.api.tasks.JavaExec

plugins {
    id("java")
    id("application")
    id("org.openjfx.javafxplugin") version "0.1.0"
    id("com.github.johnrengelman.shadow") version "8.1.1"
}

group = "cz.pexeso"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

javafx {
    version = "17.0.16" // Verze JavaFX, odpovídající LTS Javě 17
    modules("javafx.controls", "javafx.fxml")
}

dependencies {
    // SQLite JDBC ovladač pro databázi
    implementation("org.xerial:sqlite-jdbc:3.45.1.0")

    // Knihovna pro práci s JSON (ukládání/načítání hry)
    implementation("com.google.code.gson:gson:2.10.1")

    // JUnit Jupiter pro testování
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

application {
    // Definice hlavní třídy, která bude spouštět aplikaci
    mainClass.set("cz.pexeso.app.PexesoApp")
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
}

tasks.test {
    useJUnitPlatform()
}

tasks.wrapper {
    gradleVersion = "8.5"
}

tasks.shadowJar {
    manifest {
        attributes["Main-Class"] = application.mainClass.get()
    }
}
