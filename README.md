Android Version Checker [ ![Download](https://api.bintray.com/packages/statickidz/maven/VersionChecker/images/download.svg) ](https://bintray.com/statickidz/maven/VersionChecker/_latestVersion)
=======

VersionChecker is an Android library to check the version of the application with Google Play Store and force update.

How to integrate
================

Add this dependency in your `build.gradle`:

```xml
dependencies {
    compile 'com.statickidz.versionchecker:versionchecker:1.0.2'
}
```

How to use
==========

The simplest integration:

```java
public class MainActivity extends AppCompatActivity {

    VersionChecker versionChecker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        versionChecker = new VersionChecker(this);
        versionChecker.check();
    }
}
```

Have a look at the sample project for other examples.

License
=======

Licensed under the Apache License. See the [LICENSE.md](LICENSE.md) file for more details.
