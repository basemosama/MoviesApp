<h1 align="center">Movies App</h1>

<p align="center">
  <a href="https://opensource.org/licenses/Apache-2.0"><img alt="License" src="https://img.shields.io/badge/License-Apache%202.0-blue.svg"/></a>
  <a href="https://android-arsenal.com/api?level=21"><img alt="API" src="https://img.shields.io/badge/API-21%2B-brightgreen.svg?style=flat"/></a>
</p>

<p align="center">  
Movies app utilze modern Android development with Hilt, Coroutines, Flow, Retrofit, Room, Paging3, Glide, DataStore, Navigation Component,
ViewModel based on MVVM architecture built on kotlin.
</p>
</br>

##Screens

[![Screens](http://img.youtube.com/vi/ObCQ1YDDIeY/0.jpg)](http://www.youtube.com/watch?v=ObCQ1YDDIeY "Screens")


## Tech stack & Open-source libraries
- Minimum SDK level 21
- [Kotlin](https://kotlinlang.org/) based, [Coroutines](https://github.com/Kotlin/kotlinx.coroutines) + [Flow](https://kotlin.github.io/kotlinx.coroutines/kotlinx-coroutines-core/kotlinx.coroutines.flow/) for asynchronous.
- [Hilt](https://dagger.dev/hilt/) for dependency injection.
- Jetpack
  - Lifecycle - Observe Android lifecycles and handle UI states upon the lifecycle changes.
  - ViewModel - Manages UI-related data holder and lifecycle aware. Allows data to survive configuration changes such as screen rotations.
  - DataBinding - Binds UI components in your layouts to data sources in your app using a declarative format rather than programmatically.
  - Room Persistence - Constructs Database by providing an abstraction layer over SQLite to allow fluent database access.
  - Navigation Component - allow users to navigate across, into, and back out from the different pieces of content within the app.
  - DataStore - is a data storage solution that allows you to store key-value pairs or typed objects with protocol buffers.
  - Paging3 - helps loading and displaying pages of data from a larger dataset from local storage or over network.
- Architecture
  - MVVM Architecture (View - ViewModel - Model)
  - Repository Pattern
- [Retrofit2 & OkHttp3](https://github.com/square/retrofit) - Construct the REST APIs.
- [Gson](https://github.com/square/moshi/) - A JSON library.
- [Timber](https://github.com/JakeWharton/timber) - A logger with a small, extensible API.
- [Material-Components](https://github.com/material-components/material-components-android) - Material design components for building ripple animation, and CardView.
- [Lottie](https://github.com/airbnb/lottie) - library that parses Adobe After Effects animations exported as json with Bodymovin and renders them natively on mobile.


## Architecture
The app is based on the MVVM architecture and the Repository pattern.

![architecture](https://user-images.githubusercontent.com/24237865/77502018-f7d36000-6e9c-11ea-92b0-1097240c8689.png)

## TMDB API

<img src="https://user-images.githubusercontent.com/24237865/83422649-d1b1d980-a464-11ea-8c91-a24fdf89cd6b.png" align="right" width="21%"/>

The app uses The Movie DB API in order to fetch movies data.
To run this application on your machine you have to issue an API KEY from The Movie DB and place it in your gradle.properties file.

```API_KEY= "API KEY HERE"```

## Find this repository useful? :heart:
Support it by starring the repository. :star: and leaving feedback for me. <br>
Also, [follow me](https://github.com/basemosama)__ on GitHub for my next creations! 🤩

# License
```xml
Designed and developed by 2022 Basem Osama 

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```
