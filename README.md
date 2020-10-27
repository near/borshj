# BorshJ

[![Project license](https://img.shields.io/badge/license-Public%20Domain-blue.svg)](https://unlicense.org)
[![Discord](https://img.shields.io/discord/490367152054992913?label=discord)](https://discord.gg/Vyp7ETM)

**BorshJ** is an implementation of the [Borsh] binary serialization format for
Java (and Kotlin, Scala, Clojure, Groovy, Jython, JRuby, etc.) projects.

Borsh stands for _Binary Object Representation Serializer for Hashing_. It is
meant to be used in security-critical projects as it prioritizes consistency,
safety, speed, and comes with a strict specification.

## Features

- Implements [`BorshBuffer`] on top of Java's [`ByteBuffer`].

- Implements [`BorshReader`] on top of any Java [`InputStream`].

- Implements [`BorshWriter`] on top of any Java [`OutputStream`].

- Based on Java NIO, enabling high-performance, zero-copy interoperability
  with native code via JNI.

- GC friendly: avoids unnecessary copying wherever possible.

## Prerequisites

- [Java] 8+ (this library is compatible with Android)

- [Gradle] (when building from source code)

## Installation

We are working on building release binaries. They will be available here soon.

In the meantime, if you wish to try out BorshJ, you will need to build the JAR
file from source code yourself:

```bash
git clone https://github.com/near/borshj.git

cd borshj

gradle jar

ls -l build/libs/borshj-$(cat VERSION).jar
```

## Usage

To use the Borsh object serializer/deserializer, you need add just one import:

```java
import org.near.borshj.Borsh;
```

## Examples

The following code examples further below are all predicated on this simple
data class definition:

```java
public class Point2D implements Borsh {
  public float x;
  public float y;

  public Point2D() {}

  public Point2D(float x, float y) {
    this.x = x;
    this.y = y;
  }
}
```

### Serializing an object

To serialize a [POJO], use the `Borsh.serialize()` method:

```java
Point2D point = new Point2D(123.0, 456.0);

byte[] bytes = Borsh.serialize(point);
```

### Deserializing an object

To deserialize a [POJO], use the `Borsh.deserialize()` method:

```java
Point2D point = Borsh.deserialize(bytes, Point2D.class);
```

## Type Mappings

Borsh                 | Java           | TypeScript
--------------------- | -------------- | ----------
`u8` integer          | `byte`         | `number`
`u16` integer         | `short`        | `number`
`u32` integer         | `int`          | `number`
`u64` integer         | `long`         | `BN`
`u128` integer        | [`BigInteger`] | `BN`
`f32` float           | `float`        | N/A
`f64` float           | `double`       | N/A
fixed-size byte array | `byte[]`       | `Uint8Array`
UTF-8 string          | `String`       | `string`
option                | [`Optional`]   | `null` or type
map                   | [`Map`]        | N/A
set                   | [`Set`]        | N/A
structs               | `Object`       | `any`

## Frequently Asked Questions

### Q: Why does my class need a default constructor?

Classes used with `Borsh.deserialize()` must have a nullary default constructor
because instances of the class will be instantiated through Java's
[reflection API](https://www.baeldung.com/java-reflection).

[Borsh]:          https://borsh.io
[Gradle]:         https://gradle.org
[Java]:           https://java.com
[POJO]:           https://en.wikipedia.org/wiki/Plain_old_Java_object

[`BigInteger`]:   https://docs.oracle.com/javase/10/docs/api/java/math/BigInteger.html
[`BorshBuffer`]:  https://github.com/near/borshj/blob/master/src/main/java/org/near/borshj/BorshBuffer.java
[`BorshReader`]:  https://github.com/near/borshj/blob/master/src/main/java/org/near/borshj/BorshReader.java
[`BorshWriter`]:  https://github.com/near/borshj/blob/master/src/main/java/org/near/borshj/BorshWriter.java
[`ByteBuffer`]:   https://docs.oracle.com/javase/10/docs/api/java/nio/ByteBuffer.html
[`InputStream`]:  https://docs.oracle.com/javase/10/docs/api/java/io/InputStream.html
[`Map`]:          https://docs.oracle.com/javase/10/docs/api/java/util/Map.html
[`Optional`]:     https://docs.oracle.com/javase/10/docs/api/java/util/Optional.html
[`OutputStream`]: https://docs.oracle.com/javase/10/docs/api/java/io/OutputStream.html
[`Set`]:          https://docs.oracle.com/javase/10/docs/api/java/util/Set.html
