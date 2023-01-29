## Print QrCode In Console

### usages

add maven dependency in pom.xml:

```xml
<dependency>
    <groupId>pro.leaco.qrcode</groupId>
    <artifactId>console-qrcode</artifactId>
    <version>1.0.1</version>
</dependency>
```

##### Print Qrcode in console
use these code (java or kotlin):

```kotlin
ConsoleQrcode.print("https://www.github.com");
```

the console will print the qrcode like this:

![example](doc/sample.png)




##### Get the unicode qrcode string

We can use:
```kotlin
//for kotlin
val code:String = ConsoleQrcode.print("https://www.github.com")
println(code);
```
```java
//for java
String code = ConsoleQrcode.print("https://www.github.com");
System.out.println(code);
```
then we can see the unicode qrcode string like this:
```text
                                 
                                 
    █▀▀▀▀▀█ ██▀▀ █▀▀▀ █▀▀▀▀▀█    
    █ ███ █   ▄▄ ██▄  █ ███ █    
    █ ▀▀▀ █ ▄█▀▀█▄▄▄  █ ▀▀▀ █    
    ▀▀▀▀▀▀▀ █▄█ ▀▄█ █ ▀▀▀▀▀▀▀    
    ▄ ▄▀▀▄▀▄▄▀█ ▄█  ▄▀█▀ ▄ ██    
    █▄██▀▀▀ ▀▀▄█▀▀▀▀  ▀▄▄▀▀ ▄    
    ▄█  ▀▄▀▀█ █▄ ▀▄▀█ ▀▄█▀██▄    
    ██▀ █▀▀▄▄▀ ▄▄▄██▄▀ ▄▀ █▄     
    ▀ ▀▀▀▀▀▀▀█▄█ █▄▄█▀▀▀█▄▄█▀    
    █▀▀▀▀▀█  ▄▀ ▀▄▀ █ ▀ █▀ █▄    
    █ ███ █ █▄▀▄▄█▀███▀▀█▄▀▀▀    
    █ ▀▀▀ █  ▀▀ ▀ ██     █▀▄     
    ▀▀▀▀▀▀▀  ▀  ▀ ▀▀▀▀▀  ▀▀ ▀    
                                 
```