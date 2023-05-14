plugins {
	java
	id("org.springframework.boot") version "3.0.5"
	id("io.spring.dependency-management") version "1.1.0"
	id("com.epages.restdocs-api-spec") version "0.17.1"
}

group = "com.tnc.study"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_17

configurations {
	compileOnly {
		extendsFrom(configurations.annotationProcessor.get())
	}
}

repositories {
	mavenCentral()
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	implementation("org.springframework.boot:spring-boot-starter-validation")
	implementation("org.springframework.boot:spring-boot-starter-web")

	// https://github.com/gavlyukovskiy/spring-boot-data-source-decorator
	implementation("com.github.gavlyukovskiy:p6spy-spring-boot-starter:1.9.0")

	compileOnly("org.projectlombok:lombok")
	runtimeOnly("com.h2database:h2")
	annotationProcessor("org.projectlombok:lombok")
	testImplementation("org.springframework.boot:spring-boot-starter-test")

	testImplementation("org.springframework.restdocs:spring-restdocs-mockmvc")
	testImplementation("com.epages:restdocs-api-spec-mockmvc:0.17.1")

	// QueryDSL 추가
	// QueryDSL 라이브러리
	implementation("com.querydsl:querydsl-jpa:5.0.0:jakarta")
	// 프로젝트의 @Entity들을 QType으로 뽑아줌
	annotationProcessor("com.querydsl:querydsl-apt:5.0.0:jakarta")
	annotationProcessor("jakarta.annotation:jakarta.annotation-api")
	annotationProcessor("jakarta.persistence:jakarta.persistence-api")
}

tasks.withType<Test> {
	useJUnitPlatform()
}

// OAS 파일 기본 생성 경로는 /build/api-spec
openapi3 {
	this.setServer("http://localhost:8080") // OpenAPI 스펙에 명시할 URL 경로
	title = "Tennis Store API"
	description = "Tennis Store API description"
	version = "${project.version}" // build.gradle.kts 파일의 version
	format = "yaml" // or json
}

// 새로 만든 OpenAPI 스펙 파일을 static 디렉토리 안으로 이동하는 Task
tasks.register<Copy>("copyOasToSwagger") {
	delete("src/main/resources/static/swagger-ui/openapi3.yaml") // 기존 OAS 파일 삭제
	from("$buildDir/api-spec/openapi3.yaml") // 복제할 OAS 파일 지정
	into("src/main/resources/static/swagger-ui/.") // 타겟 디렉토리로 파일 복제
	dependsOn("openapi3") // openapi3 Task가 먼저 실행되도록 설정
}

// clean task 수행 시 src/main/generated 디렉토리도 삭제
tasks.clean {
	delete("src/main/generated")
}