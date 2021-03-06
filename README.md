# RandomGenKt
>
> Initialize instances of any class with generated data.
>

[![Version - RandomGenKt](https://img.shields.io/maven-central/v/com.mitteloupe.randomgenkt/randomgenkt?label=RandomGenKt+|+MavenCentral)](https://mvnrepository.com/artifact/com.mitteloupe.randomgenkt/randomgenkt)
[![Version - DataSource](https://img.shields.io/maven-central/v/com.mitteloupe.randomgenkt/randomgenkt.datasource?label=datasource+|+MavenCentral)](https://mvnrepository.com/artifact/com.mitteloupe.randomgenkt/randomgenkt.datasource)
[![Build Status](https://travis-ci.com/EranBoudjnah/RandomGenKt.svg?branch=master)](https://travis-ci.com/EranBoudjnah/RandomGenKt)
[![License](https://img.shields.io/github/license/EranBoudjnah/RandomGenKt)](https://github.com/EranBoudjnah/RandomGenKt/blob/master/LICENSE)

![Example](https://github.com/EranBoudjnah/RandomGenKt/raw/master/example/videocap.gif)

This is a Kotlin port of the Java library designed to generate random instances of any class.

This is great for demoing your app with interesting content, manually testing it with varying data, and even populating it with smart, random generated data in production.

## Install

In your `build.gradle`, add the following:

```groovy
dependencies {
    implementation("com.mitteloupe.randomgenkt:randomgenkt:1.0.1")
}
```

To include the default data generators, also include
```groovy
dependencies {
    implementation("com.mitteloupe.randomgenkt:randomgenkt.datasource:1.0.1")
}
```

## Usage

### Kotlin
```kotlin
val randomGen = RandomGen.Builder<ObjectClass>()
	.ofClass<ObjectClass>()
	.withField("id")
	.returningSequentialInteger()
	.withField("uuid")
	.returningUuid()
	.build()
```

### Java
```java
RandomGen<ObjectClass> randomGen = new RandomGen.Builder<ObjectClass>()
	.ofClass(ObjectClass.class)
	.withField("id")
	.returningSequentialInteger()
	.withField("uuid")
	.returningUuid()
	.build();
```

This will create a `RandomGen` instance producing `ObjectClass` instances with sequential IDs and random UUIDs.

To use the newly generated `RandomGen`, simply call:

### Kotlin
```kotlin
val instance = randomGen.generate()
```

### Java
```java
ObjectClass instance = randomGen.generate();
```

### What's New?

The Kotlin version adds the following:

* Support for fields with lazy init
* Lambdas
* `ofClass<Type>()` instead of `ofClass(Type::class.java)`

## Created by
[Eran Boudjnah](https://www.linkedin.com/in/eranboudjnah)

## License
MIT © [Eran Boudjnah](https://www.linkedin.com/in/eranboudjnah)
