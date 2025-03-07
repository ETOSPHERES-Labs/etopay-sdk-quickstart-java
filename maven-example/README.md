# Maven Java Example

Run tests:
```
mvn clean test
```

Create Jar and execute it, providing the correct path to the native libraries:
```
mvn clean package
java -Djava.library.path=target/native-libs/wallet-0.14.0-SNAPSHOT/native/x86_64/linux -cp target/maven-example-1.0-SNAPSHOT-jar-with-dependencies.jar com.example.App
```


