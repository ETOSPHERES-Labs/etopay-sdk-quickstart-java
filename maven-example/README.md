# Maven Java Example

Run tests:
```
mvn clean test
```

Create Jar and execute it, providing the correct path to the native libraries:
```
mvn clean package
java -Djava.library.path=target/natives/linux/x64 -cp target/maven-example-1.0-SNAPSHOT-jar-with-dependencies.jar com.example.App
```


