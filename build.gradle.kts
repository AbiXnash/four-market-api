plugins {
	kotlin("jvm") version "2.2.21"
	kotlin("plugin.spring") version "2.2.21"
	id("org.springframework.boot") version "4.0.6"
	id("io.spring.dependency-management") version "1.1.7"
}

group = "in.abx"
version = "0.0.1-SNAPSHOT"

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(21)
	}
}

repositories {
	mavenCentral()
}

dependencies {
    implementation(
        "org.springframework.boot:spring-boot-starter-web"
    )

    implementation(
        "org.jetbrains.kotlin:kotlin-reflect"
    )

    implementation(
        "org.apache.commons:commons-lang3"
    )

    implementation(
        "com.dbeaver.jdbc:com.dbeaver.jdbc.driver.libsql:1.0.4"
    )

    implementation("io.jsonwebtoken:jjwt-api:0.13.0")
    runtimeOnly("io.jsonwebtoken:jjwt-impl:0.13.0")
    runtimeOnly("io.jsonwebtoken:jjwt-jackson:0.13.0")

    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.boot:spring-boot-starter-web")

    testImplementation(
        "org.springframework.boot:spring-boot-starter-test"
    )

    testImplementation(
        "org.jetbrains.kotlin:kotlin-test-junit5"
    )

    testRuntimeOnly(
        "org.junit.platform:junit-platform-launcher"
    )
}

kotlin {
	compilerOptions {
		freeCompilerArgs.addAll("-Xjsr305=strict", "-Xannotation-default-target=param-property")
	}
}

tasks.withType<Test> {
	useJUnitPlatform()
}
