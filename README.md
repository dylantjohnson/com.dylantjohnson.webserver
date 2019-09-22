# com.dylantjohnson.webserver
A simple wrapper for [Java's Built-In HTTPS Server](https://docs.oracle.com/en/java/javase/12/docs/api/jdk.httpserver/com/sun/net/httpserver/HttpsServer.html "com.sun.net.httpserver.HttpsServer"). This server will help easily get HTTPS running, along with HTTP requests getting redirected to HTTPS.
## Usage
module-info.java
```java
module my.module {
    requires com.dylantjohnson.webserver;
}
```
App.java
```java
import com.dylantjohnson.webserver.WebServerBuilder;
import com.dylantjohnson.webserver.WebServerCreationException;
import java.io.File;

public class App {
    public static void main(String[] args) throws WebServerCreationException {
        var domain = "dylantjohnson.com";
        var keystore = new File(args[0]);
        var password = args[1];
        var server = new WebServerBuilder()
            .setDomain(domain)
            .useHttp()
            .useHttps(keystore, password)
            .build();
        server.start();
    }
}
```
This example takes the path to the keystore file and its password from command-line arguments. Setting the domain to `dylantjohnson.com` means that any requests to `http://dylantjohnson.com/blah` will be redirected to `https://dylantjohnson.com/blah`. This assumes that `dylantjohnson.com` points to this HTTP server of course.
## Keystore
This server expects to be given a keystore file containing the certificates for HTTPS. Here's an example of creating a keystore from certificate files given by [Let's Encrypt](https://letsencrypt.org/ "Let's Encrypt Homepage").
```
openssl pkcs12 -export -in <path/to/fullchain.pem> -inkey <path/to/privkey.pem> -name <myCertName> -out <path/to/keystore.pkcs12>
```
Use the generated `pkcs12` file as your keystore file when creating the server.
## Building
### Prerequisites
- I build this with OpenJDK 12, although it might build with earlier versions. I use `var` a lot, so whenever that was introduced.
### Directory Structure
This is written as a simple Java module. No need for complicated build tools like Maven or Gradle. I recommend having a Java workspace directory, as described in [Project Jigsaw Documentation](https://openjdk.java.net/projects/jigsaw/quick-start "OpenJDK Project Jigsaw Quick-Start").
```
java
|_classes
|_mods
|_src
  |_com.dylantjohnson.webserver
    |_module-info.java, source files, etc.
```

To compile, from your workspace directory:
```
javac -d ./classes --module-path ./mods --module-source-path ./src --module com.dylantjohnson.webserver
```

To package, from your workspace directory:
```
jar --create --file ./mods/com.dylantjohnson.webserver.jar --module-version <version> -C ./classes/com.dylantjohnson.webserver .
```
