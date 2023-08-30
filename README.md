![EventCademy](art/banner.png "EventCademy")
<a href="https://play.google.com/store/apps/details?id=com.yveskalume.eventcademy"><img src="https://play.google.com/intl/en_us/badges/static/images/badges/en_badge_web_generic.png" height="70"></a>

# EventCademy

## Description

Eventcademy is an event app designed for local tech and cultural communities. It provides a platform
for event organizers to publish their events, making it easy for you to discover and join local
gatherings. Whether you're into tech meetups, cultural festivals, workshops, or more, Eventcademy
connects you with events that matter in your community. Stay informed, engage with organizers, and
explore what's happening near you with Eventcademy.

Eventcademy is also very much a testing ground for things I personally dive into, from architecture,
libraries, patterns, API quirks, and more. It's been a very fun project to spike test new things.

## Features

- Event feed and advertisement
- Event details
- Event registration
- See booked events
- Publish and manage events
- Settings
- Profile

## Tech Stack

- 100% [Kotlin](https://kotlinlang.org/)
- [Coroutines](https://github.com/Kotlin/kotlinx.coroutines) + [Flow](https://kotlin.github.io/kotlinx.coroutines/kotlinx-coroutines-core/kotlinx.coroutines.flow/)
  to simplify code that executes asynchronously.
- Firebase
    - [Authentication](https://firebase.google.com/docs/auth) - to authenticate users.
    - [Firestore](https://firebase.google.com/docs/firestore) - a flexible, scalable NoSQL cloud
      database to store and sync data.
    - [Storage](https://firebase.google.com/docs/storage) - to store files.
    - [Crashlytics](https://firebase.google.com/docs/crashlytics) - to track crashes.
    - [Analytics](https://firebase.google.com/docs/analytics) - to track app usage.
    - [Remote Config](https://firebase.google.com/docs/remote-config) - to remotely configure the
      app.
    - [Cloud Messaging](https://firebase.google.com/docs/cloud-messaging) - to send push
      notifications.
- JetPack
    - [Compose](https://developer.android.com/jetpack/compose) - A modern toolkit for building
      native Android UI.
    - [Hilt](https://developer.android.com/training/dependency-injection/hilt-android) - for
      dependency injection.
    - [Lifecycle](https://developer.android.com/jetpack/androidx/releases/lifecycle) - dispose
      observing data when lifecycle state changes.
    - [ViewModel](https://developer.android.com/topic/libraries/architecture/viewmodel) - UI related
      data holder, lifecycle aware.
- [Material Design 3](https://developer.android.com/jetpack/compose/designsystems/material3) -
  Material 3 includes Material You personalization features like dynamic color, and is designed to
  be cohesive with the new visual style and system UI on Android 12 and above
- [Lottie Android](https://github.com/airbnb/lottie-android) - Render After Effects animations
  natively on Android
- [KSP](https://github.com/google/ksp) - Kotlin Symbol Processing API.
- [Coil](https://coil-kt.github.io/coil/compose/) - An image loading library for Android backed by
  Kotlin Coroutines.
- [JUnit](https://developer.android.com/training/testing/local-tests) - a “Unit Testing” framework

## Development and Contribution

### Setup firebase

To setup this project, you need to :

- create a Firebase project and add an Android app to the project. When prompted, use the package
  name `com.yveskalume.eventcademy`.
- Download the generated `google-services.json` file and place it in the `app/` directory.

Bug fixes are always welcome. Tests are too if you're into that kinda thing, but I'm not actively
trying to make this project a shining icon of TDD. For new features or otherwise significant work,
please discuss in an issue first.

License
-------

	Copyright (C) 2023 Yves Kalume

	Licensed under the Apache License, Version 2.0 (the "License");
	you may not use this file except in compliance with the License.
	You may obtain a copy of the License at

	http://www.apache.org/licenses/LICENSE-2.0

	Unless required by applicable law or agreed to in writing, software
	distributed under the License is distributed on an "AS IS" BASIS,
	WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
	See the License for the specific language governing permissions and
	limitations under the License.
