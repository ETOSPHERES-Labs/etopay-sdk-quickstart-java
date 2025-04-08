# ETOPay SDK Java Quickstart

[![ci](https://github.com/ETOSPHERES-Labs/etopay-sdk-quickstart-java/actions/workflows/ci.yml/badge.svg)](https://github.com/ETOSPHERES-Labs/etopay-sdk-quickstart-java/actions/workflows/ci.yml)

This repository contains three quickstart examples on how to integrate the [ETOPay SDK](https://github.com/ETOSPHERES-Labs/etopay-sdk) into your [Java](#native-java) or [Android](#android) project.

## Minimum Version Support

The following versions of the toolchain are used to build and compile the Java SDK and should be used as minimum versions for integrating the SDK. Versions lower than the mentioned might work, however are not guaranteed by the team. In case of issues, please contact the team with specific build or compile errors.

- **Java Compiler**: `23.0.2`
- **gradle**: `Gradle 8.13`
- **Android SDK Command-line Tools**:  `12.0`
- **Android SDK Platform**: `Android SDK Platform 33`
- **Android API Level**: `33`
- **Android NDK**: `26.3.11579264` (`r26d`)
- **Android Build Tools**: `34.0.0`

## Native Java

We currently only package native binaries for 64-bit Linux. Please open an [Issue](https://github.com/ETOSPHERES-Labs/etopay-sdk-quickstart-java/issues/new) if you need other platforms.

### [Maven](./maven-example)

To use the SDK with Maven add the following to your `pom.xml` to add the SDK as a compilation dependency (change the `version` to the one you want):

```xml
<dependencies>
  <dependency>
    <groupId>com.etospheres.etopay</groupId>
    <artifactId>etopaysdk</artifactId>
    <scope>compile</scope>
    <version>0.14.0-SNAPSHOT</version>
  </dependency>
  ...
</dependencies>
```
If you want to use `SNAPSHOT` versions, make sure to add the Central Snapshots repository:
```xml
<repositories>
  <!-- Add Maven Central Snapshots repository -->
  <repository>
    <name>Central Portal Snapshots</name>
    <id>central-portal-snapshots</id>
    <url>https://central.sonatype.com/repository/maven-snapshots/</url>
    <releases>
      <enabled>false</enabled>
    </releases>
    <snapshots>
      <enabled>true</enabled>
    </snapshots>
  </repository>
</repositories>
```

Since the SDK contains native libraries, we need to extract them so that they can be found at runtime. To do this automatically, use the `maven-dependency-plugin`:
```xml
<plugins>
  <!-- Automatically extract the native libs from the dependency -->
  <plugin>
    <groupId>org.apache.maven.plugins</groupId>
    <artifactId>maven-dependency-plugin</artifactId>
    <version>3.8.1</version>
    <executions>
      <execution>
        <id>unpack</id>
        <phase>generate-resources</phase>
        <goals>
          <goal>unpack</goal>
        </goals>
        <configuration>
          <artifactItems>
            <artifactItem>
              <groupId>com.etospheres.etopay</groupId>
              <artifactId>etopaysdk</artifactId>
              <!-- Select the binaries using the correct classifier tag -->
              <classifier>natives-linux</classifier>
              <type>jar</type>
              <overWrite>true</overWrite>
              <outputDirectory>${project.build.directory}/natives</outputDirectory>
            </artifactItem>
          </artifactItems>
        </configuration>
      </execution>
    </executions>
  </plugin>
  ...
</plugins>
```

Optionally, if you are using the `maven-surefire-plugin`, you can automatically add the natives to the `java.library.path` when running your tests:
```xml
<plugins>
  <!-- Add the native libs automatically when executing tests -->
  <plugin>
    <groupId>org.apache.maven.plugins</groupId>
    <artifactId>maven-surefire-plugin</artifactId>
    <configuration>
        <argLine>-Djava.library.path=${project.build.directory}/natives/linux/x64</argLine>
    </configuration>
  </plugin>
  ...
</plugins>
```

You can now run your tests as usual with `mvn clean test`. If you want to run your pacakged `jar` file, you need to manually specify the `java.library.path` to point at the correct folder:
```bash
java -Djava.library.path=target/natives/linux/x64 <your jar file>
```


### [Gradle](./gradle-java)

To use `gradle` first make sure you have the right repositories setup, especially if you want to use `SNAPSHOT` versions.
```groovy
repositories {
	// Use Maven Central Snapshots for resolving the etopay sdk
	maven {
		name = 'Central Portal Snapshots'
		url = 'https://central.sonatype.com/repository/maven-snapshots/'

		// Only search this repository for the specific dependency
		content {
		    includeModule("com.etospheres.etopay", "etopaysdk")
		}
	}
    // Use Maven Central for resolving dependencies.
    mavenCentral()
}
```

To automatically unzip and add the location of the natives to the `java.library.path` you can use the following `dependency` and helper tasks:
```groovy
// Create custom configuration group so that we can reference the natives and extract them.
configurations {
	natives
}

dependencies {
    // <Other dependencies here>
   
    // This dependency is used by the application.
	implementation 'com.etospheres.etopay:etopaysdk:0.14.0-SNAPSHOT'
	natives 'com.etospheres.etopay:etopaysdk:0.14.0-SNAPSHOT:natives-linux'
}

// Task that will automatically unzip the file added to the natives configuration
task unzipNatives(type: Copy) {
	from zipTree(configurations.natives.singleFile)
	into 'natives'
}

application {
    // <Other settings here>

	// Add natives to java.library.path
	applicationDefaultJvmArgs += ["-Djava.library.path=${projectDir}/natives/linux/x64"]
}

tasks.named('run') {
	dependsOn unzipNatives
}

tasks.named('test') {
    // Use JUnit Platform for unit tests.
    useJUnitPlatform()

	// Add natives to java.library.path
	dependsOn unzipNatives
	systemProperty(
        "java.library.path",
        "${projectDir}/natives/linux/x64"
    )

}
```

## [Android](./gradle-android)


For Android, we are currently packaging native libraries for the `armeabi-v7a`, `arm64-v8a`, `x86` and `x86_64` architectures. Please open an [Issue](https://github.com/ETOSPHERES-Labs/etopay-sdk-quickstart-java/issues/new) if you need other architectures.

In you Android Studio project, add the version of the SDK you want to use to your `libs.versions.toml`:
```toml
[versions]
etopaysdk = "0.14.0-SNAPSHOT"

[libraries]
etopaysdk = { group = "com.etospheres.etopay", name = "etopaysdk", version.ref = "etopaysdk" }
```

If you want to use a `SNAPSHOT` version, you need to include the Maven Central Snapshot repository in `settings.gradle.kts`
```kotlin
dependencyResolutionManagement {
    repositories {
        // <Other repositories>

        // Use Maven Central Snapshots for resolving the etopay sdk
        maven {
            name = "Central Portal Snapshots"
            url = uri("https://central.sonatype.com/repository/maven-snapshots/")
            content {
                includeModule("com.etospheres.etopay", "etopaysdk")
            }
            mavenContent {
                snapshotsOnly()
            }
        }

    }
}
```

And then you can define the dependencies in the `build.gradle.kts` and add a job that will extract the native libraries to the `jniLibs` folder expectect by the Android SDK:
```kotlin

// Create a configuration to add the native dependencies to
val natives = configurations.create("natives")

dependencies {
    implementation(libs.etopaysdk)
    natives(variantOf(libs.etopaysdk) { classifier("natives-android") })

    // ...
}

// Task to unzip the natives into the jniLibs folder
tasks.register<Copy>("unzipNatives") {
    from(zipTree(natives.singleFile)) {
        include("android/**")
        eachFile {
            relativePath = RelativePath(true, *relativePath.segments.drop(1).toTypedArray())
        }
        includeEmptyDirs = false
    }
    into("src/main/jniLibs")
}

// Make the unzipNatives task run before building
tasks.named("preBuild") {
    dependsOn("unzipNatives")
}

```

You should now be able to use the ETOPay SDK in your Android Studio project!
