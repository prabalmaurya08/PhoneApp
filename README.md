Phone App

Brief Description

The Phone App is a basic Android application that mimics the functionality of a phone's dialer and call log management system. It allows users to:

Make outgoing calls.
View call logs (incoming, outgoing, and missed calls).
Display contacts from the phone's contact list.
Use a dial pad to input phone numbers for making calls.
The app provides a clean and user-friendly interface with:

A PhoneFragment for managing calls and viewing call logs.
A ContactFragment to display and manage contacts.
The app also handles permission requests to access call logs and contacts.


Key Features:
Default Calling App: The app can be set as the default phone/dialer app.
Call Log Filters: Users can filter the call logs by incoming, outgoing, or missed calls.
Contacts List: Displays contacts from the user's phone book.
Dialer Pad: Allows dialing numbers manually.
Permission Management: Requests necessary permissions for reading call logs and contacts.
Steps to Set the App as the Default Calling App


1. Install the App
To set the app as the default calling app, you first need to install it on your Android device.

Download the APK file or install the app from the repository.
If installing from the APK, make sure to enable Install from Unknown Sources in your phone's settings.
2. Set as Default Dialer App
Once the app is installed, follow these steps to set it as your default calling app:

For Android 10 and above:

Open the Settings on your Android device.
Scroll down and tap on Apps.
Tap Default apps.
Look for Phone app or Dialer (it could be named differently depending on the device).
Select Phone App (this is the name of your app).
You may be prompted to confirm the change. Once confirmed, your app will be set as the default dialer.
For Android 9 and below:

Open Settings on your Android device.
Tap on Apps & notifications or Applications.
Scroll down and tap Advanced to open more options.
Tap on Default apps.
Select Phone and then choose your app as the default phone/dialer app.


3. Handling Permissions
Once your app is set as the default calling app, it will need to request permission to access:
Call Logs: To view the incoming and outgoing calls.
Contacts: To show the contacts in the phonebook.
On app launch, if these permissions are not granted, the app will request the necessary permissions. The user must grant these permissions for the app to function fully.

4. Testing the App
Once set as the default calling app, you can:

Open the app and make a call using the Dial Pad.
View recent calls in the Call Log.
View contacts in the Contacts Fragment.
Test call answering, rejecting, and managing call logs.


Installation

Clone the repository:
To start using the app, clone this repository using the following command:

git clone https://github.com/yourusername/phone-app.git


Build the app:
Open the project in Android Studio.
Make sure to sync the project with Gradle files.
Build the project by clicking on Build > Make Project.
Install the app on your Android device/emulator.


Technologies Used

Kotlin: Primary programming language for Android development.
Android SDK: For building the Android application.
Room Database (Optional, for future features like storing call logs locally).
Jetpack Libraries: For UI components and permission management.
