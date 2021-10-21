# BikeTracks


## About 

This project was to incentivise the use of eco-friendly methods of transport when commuting, such as bicycles and e-scooters. We created a GPS tracking app that records a user's commuting journey.

A report detailing our project can be found [here](https://docs.google.com/spreadsheets/d/1bhnV-CQRMy3Z0npQd9XSoTdkYxz0ew5e648S00qkJZ8).

Our project management tool was Trello. The board can be found [here](https://trello.com/b/W3KX3XdZ/capstone).


## Installing

###  - Android 
To install the app directly on to your smartphone:
1.  Open up this page on your phone browser. 
2.  From [releases](https://github.com/uoa-compsci399-s2-2021/Team9-BikeTracks-App-Development/releases), click `BikeTracks-v0.1.0.apk` and it should automatically download on to your phone.

Alternatively, to run this app through your PC: 
1.  Download and set up [Android Studio](https://developer.android.com/studio).
2.  Download `Source code (zip)` for Android from [releases](https://github.com/uoa-compsci399-s2-2021/Team9-BikeTracks-App-Development/releases), and extract it.
3.  In Android Studio, open `build.gradle` found in the extracted folder as an Android Studio project.
4. There are two ways to run the app:
    - Plug your Android device in to your PC, or
    - Set up a virtual device through AVD Manager: 
      - In Tools, select AVD Manager.
      - Click **Create Virtual Device**
      - Select Pixel 5 (you are free to select any device, but for the sake of simplicity we will choose this one).
      - Download and select Android Release `R`.
    
    For more information, refer to this [page](https://developer.android.com/training/basics/firstapp/running-app).
5. Run the app.


### - iOS

Unfortunately there is no way to directly download the app on to an iOS device, as the process for deploying a packaged app for iOS is very strict. This means we must run through a macOS device:
1. Download and set up [Xcode](https://developer.apple.com/xcode/resources/).
2. Download `Source code (zip)` for iOS from [releases](https://github.com/uoa-compsci399-s2-2021/Team9-BikeTracks-App-Development/releases), and extract it.
3. Open the `BikeTracks.xcodeproj` file in Xcode.
4. Change the Signing & Capabilities Team to your Team
5. Connect your iOS device.
6. Select your device and press run.

## Application Architecture

- Android technologies:
  - Java - language for Android backend and database
  - XML - language for Android frontend
  - [play-services-location](https://developer.android.com/training/location) -    accessing Android GPS location.
  - [play-services-maps](https://developers.google.com/maps/documentation/android-sdk/overview) - Android map view.
  - [android.hardware](https://developer.android.com/reference/android/hardware/package-summary) - accessing Android device sensor information.
  - [android.database.sqlite](https://developer.android.com/reference/android/database/sqlite/package-summary) - used for Android database.
- iOS technologies:
  - Swift - language for iOS backend and database
  - Storyboards - language for iOS frontend
  - [Core Location](https://developer.apple.com/documentation/corelocation) - accessing iOS GPS location.
  - [Core Motion](https://developer.apple.com/documentation/coremotion) - accessing iOS device sensor information. 
  - [MapKit](https://developer.apple.com/documentation/mapkit) - iOS map view.
  - [Sqlite.swift](https://github.com/stephencelis/SQLite.swift) - used for iOS database.

## Future Plans


### Within scope:

  - App currently does not track location when app is moved to background/device screen is off. This can be fixed with foreground services and a notification channel.
  - React-Native for streamlined frontend work between Android and iOS apps, as currently both apps have different frontends.
  
### Outsourcing/Collaboration required:

  - Credit/reward system for distance tracked.    
  - Machine learning model to automatically detect transport mode.

## Acknowledgements


### People

- Martin Urschler - Our client, who directed our team through the whole process.
- Asma Shakil - Course co-ordinator, who helped set up our meetings with Martin. 
- Tao Natalie Zhu - Teaching Assistant, who we communicated with weekly on our progress.

### Other
- Strava - Fitness App 
- MapyMyFitness - Fitness App
