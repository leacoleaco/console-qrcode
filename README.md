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

___

#### Print Qrcode in console
use these code (java or kotlin):

```kotlin
ConsoleQrcode.print("https://www.github.com");
```

the console will print the qrcode like this:

![example](doc/sample.png)


___

#### Get the unicode qrcode string

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
Then we can see the unicode qrcode string like this:
```html
                                 
                                 
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

But because of the line-high, we can not scan the qrcode.
if we want to use the unicode qrcode, we should adjust the screen printer's
line-high.

e.g.
```html
<pre style="line-height: 12px">
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
</pre>
```

then,we can get the correct qrcode in web browser:

<pre style="line-height: 12px">
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
</pre>
