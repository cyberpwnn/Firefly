# Firefly
Cross platform scripting for builds &amp; other utilities in JavaScript

### Running Firefly
In CMD
```
start fly <script>.js
```

In Java
```
java -jar Firefly.jar <script>.js
```

### Access Java Classes
```js
importClass("java.awt.Frame");
var obj = new java.awt.Frame("Window");
```

### Access Static Members
```js
var tk = Java.type("java.awt.Toolkit");
tk.getDefaultToolkit().beep();
```

### Other Utilities
```js
// Log stuff
Fly.log("Log");

// Execute script sync
Fly.excute("script.js");

// Thread a script
var thread = Fly.executeAsync("script.js");
thread.start();
thread.join();

// FS
FS.copy("from", "to");
FS.delete("file or folder");
FS.mkdirs("folder path");
FS.touch("create a file");
FS.writefile("filename", "string to write"); // does not append
FS.writefile("filename", "string to write", bool); //append bool
```
