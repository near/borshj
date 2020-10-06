# BorshJ

[![Project license](https://img.shields.io/badge/license-Public%20Domain-blue.svg)](https://unlicense.org)

## Examples

The following examples are predicated on this data class definition:

```java
public class Point2D {
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

```java
Point2D point = new Point2D(123.0, 456.0);

byte[] bytes = Borsh.serialize(point);
```

### Deserializing an object

```java
Point2D point = Borsh.deserialize(bytes, Point2D.class);
```
