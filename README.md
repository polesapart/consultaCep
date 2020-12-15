# ConsultaCep
[![Release](https://jitpack.io/v/polesapart/consultaCep.svg)](https://jitpack.io/#polesapart/consultaCep)

A small library for consulting (Brazillian only for the moment) postal codes.

Current backend provided by [ViaCep](https://viacep.com.br/)

This is made in kotlin but should be java/jvm compatible.

Didn't try with Android, but I assume it works - all dependencies are Android compatible.

Gradle example:
```gradle
    allprojects {
        repositories {
            jcenter()
            maven { url "https://jitpack.io" }
        }
   }
   dependencies {
        implementation 'com.github.polesapart:consultaCep:Version'
   }
