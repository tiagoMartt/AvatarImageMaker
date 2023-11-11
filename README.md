# AvatarImageMaker
Simple library that allows you to create avatar patterns with the initials of a name with a colorful background

Gradle implementation:
```
repositories {
  mavenCentral()
  maven { url 'https://jitpack.io' }
}

dependencies {
  implementation 'com.github.tiagomart:AvatarImageMaker:Tag'
}
```
Usage:

```
val b = cl.build()
val cl = AvatarImageMaker.AvatarImageBuilder(context)
  .setBackgroundColor(Color.RED)
  .setCornerRadius(16f)
  .setInitials(1)
  .setText("A")

val b = cl.build()
```
