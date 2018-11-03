# RandomGenKt
>
> Initialize instances of any class with generated data.
>

[![Build Status](https://travis-ci.com/EranBoudjnah/RandomGenKt.svg?branch=master)](https://travis-ci.com/EranBoudjnah/RandomGenKt)

![Example](https://github.com/EranBoudjnah/RandomGen/raw/master/example/videocap.gif)

This is a Kotlin port of the Java library designed to generate random instances of any class.

This is great for demoing your app with interesting content, manually testing it with varying data, and even populating it with smart, random generated data in production.

## Install

In your `build.gradle`, add the following:

```groovy
dependencies {
	implementation 'com.mitteloupe:randomgen:1.4.0'
}
```

To include the default data generators, also include
```groovy
implementation 'com.mitteloupe:randomgen.datasource:1.0.0'
```

Note: To add the BinTray repository in your maven repositories, also add the following:
```groovy
repositories {
	maven {
		url "https://dl.bintray.com/shadowcra/RandomGen"
	}
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
* ofClass<Type>() instead of ofClass(Type::class.java)  

## Created by
[Eran Boudjnah](https://www.linkedin.com/in/eranboudjnah)

## License
MIT © [Eran Boudjnah](https://www.linkedin.com/in/eranboudjnah)
