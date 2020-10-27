# Lewis

Lewis is an app which display realtime LUAS information which is obtained from https://luasforecasts.rpa.ie/.

## Installation

Import the code into Android Studio and build.
Then install on you device.

## Usage

Run the app.

When you open the app it displays a forcast of arrival times.
 
Between 00:00 and 12:00, it displays Outbound forecasts from Marlborough LUAS stop.

And between 12:01 and 23:59, it displays Inbound forecasts from Stillorgan LUAS stop.

What happens for the other two minutes of the day is undefined.

## Code

The code is written in Kotlin.

It uses OkHttp3 to retrieve the data.

Uncommenting the addInterceptor() line in getStopInfoAsync() will cause timing information for the web service call to be displayed in Logcat.

If the webservice is unavailable, a message to that effect is displayed.

Clicking on the update FAB causes the app to refresh the data.

## License
[GPL-3.0]https://choosealicense.com/licenses/gpl-3.0/