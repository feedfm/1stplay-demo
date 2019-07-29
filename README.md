# 1stplay-demo
A barebones app that shows how to use 1st Play stations.


## 1st Play Usage
1st play stations guarantee in order playback of music at least the first time a station is played. These stations are usually played only the first time a workout is being done by the user. On all subsequent plays of this station the music playback order would be random or you could simply elect to play a different station.

## Setting up a first play station.

In the admin console select the station you intend to use as first play station and add a new parameter "first_play" -> true to the options.
This will help you identify the station as a first play station in the app. You could also add a unique stationID to the parameters and directly refer to the station with this ID.

## Usage in App

In the app to identify first play stations from the stationList use getStationWithOptions(). Here you would pass "first_play" -> true in the parameters or "stationID" -> "YOUR_ID".
You can check if the station has never been played by this user by calling hasNewMusic() on the station. If the result is true, this station can be successfully used for the associated workout while ensuring in order playback.

## Seeking

In first play stations, it becomes important to align the music with the workout. Therefore Feed.fm provides SeekStation api. To make sure the music is aligned with the workout it is recommended to check parameter "maxSeekableLength" in FeedPlayer, the value you try to seek should be less than "maxSeekableLength" as FeedPlayer can only seek upto two songs at this time. The maxSeekableLength's value should usually be somewhere between 300 to 700 Seconds. We recommend you limit max seeking time to max maxSeekableLength or less.
