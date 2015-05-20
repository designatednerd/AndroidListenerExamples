#AndroidListenerExamples

A repository for examples for my talks at the [AndroidListener](http://twitter.com/AndroidListener) meetup in Chicago. 

Examples are backwards-compatible to API 10 (aka 2.3.3 Gingerbread) except as noted. 

###CommonLetterFinder, JUnit4TestExample and ParameterTestExample
From my May 2015 talk about JUnit4 testing. 

###EspressoUITestExample
From my April 2015 talk on UI testing with Espresso 2.0. Also ~~uses [Jake Wharton's ActivityRule](https://gist.github.com/JakeWharton/1c2f2cadab2ddd97f9fb) for JUnit4 testing and~~ has some custom ViewActions in the test package. Has been updated to use Espresso 2.1's `ActivityTestRule`. 

###NotificationFragment
From my May 2014 talk on Notifications, examples of several of the types of notifications introduced in Jellybean (backwards compatible using `NotificationCompat` and its assorted friends).

###ImmersiveFragment
From my January 2014 talk on Kit Kat Immersive Mode. Not backwards-compatible beyond API 14, has some issues with resizing the window at the moment. *[Note: Temporarily disabled in display, but is still available for your code perusal]*

###SpannedTextFragment
From my December 2013 talk on clickable `TextSpans`. 

###KittensFragment
This was created to provide a second fragment so I couldshow off the `DrawerLayout` stuff from my November 2013 talk about the `DrawerLayout`. 

###FullscreenVideoWebviewFragment

From my September 2013 talk "Why is this so hard?!: Playing Full-Screen YouTube Videos From An Internal WebView", an example of how to play video full-screen from an embedded `WebView`. Uses the `MainActivity` to handle swapping out the content view. 

