plugins {
	kotlin("jvm") version "1.9.25"
	kotlin("plugin.spring") version "1.9.25"
	id("org.springframework.boot") version "3.3.4"
	id("io.spring.dependency-management") version "1.1.6"
}

group = "org.example.dependent.kts"
version = "0.0.1-SNAPSHOT"

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(17)
	}
}

repositories {
	mavenCentral()
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")

	implementation("org.jetbrains.kotlin:kotlin-scripting-common")
	implementation("org.jetbrains.kotlin:kotlin-scripting-jvm")
	implementation("org.jetbrains.kotlin:kotlin-scripting-jvm-host")
	implementation("org.jetbrains.kotlin:kotlin-scripting-dependencies")
	implementation("org.jetbrains.kotlin:kotlin-scripting-dependencies-maven")
	implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.8.1")
}

kotlin {
	compilerOptions {
		freeCompilerArgs.addAll("-Xjsr305=strict")
	}

	jvmToolchain(17)
}

tasks.withType<Test> {
	useJUnitPlatform()
}
