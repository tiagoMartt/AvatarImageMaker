# AvatarImageMaker
Simple library that allows you to create avatar patterns with the initials of a name with a colorful background

Implementation:

dependencies {
  implementation 'com.github.tiagomart:AvatarImageMaker:Tag'
}

usage:

val cl = AvatarImageMaker.AvatarImageBuilder(context)
                .setBackgroundColor(Color.RED)
                .setCornerRadius(16f)
                .setInitials(1)
                .setText("A")

            val b = cl.build()
