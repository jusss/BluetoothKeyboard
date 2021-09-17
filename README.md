# BluetoothKeyboard
Use An Android Device As A Bluetooth Keyboard 


# :)

# Copy from
https://github.com/ginkage/wearmouse<br/>
https://github.com/domi1294/BluetoothHidDemo

# How to Use
1. iPhone connect to Android via Bluetooth<br/>
2. open BluetoothHidDemo APP on Android<br/>
3. iPhone click an area which can input, then press any key on this APP

# Requirement
Android Pie 9, API 28<br/>
Bluetooth HID Profile Enabled

# Test Device 
Redmi Note 7, LineageOS 18.1, Android 11, lineage-18.1-20210525-nightly-lavender-signed.zip<br/>
iPhone 11, iOS 14.2<br/>
Redmi 2, LineageOS 17.1, Android 10, lineage-17.1-20200225-UNOFFICIAL-wt88047.zip<br/>

# Issue
switch this app to background, bluetooth will disconnect, you need restart this app. fixed!<br/>
different layout for different screen size? fixed!<br/>
Specific target name? fixed!<br/>
Tab-A? for iOS<br/>
C-S? for Windows<br/>

# Release
see --> github release

# Note
Shift Del just send Shift, and Win Del just send Win.<br/>
it uses scan code, not key code,<br/>
for example, Delete 76, Backspace 42, Ctrl 224<br/>
check it on https://www.usb.org/sites/default/files/documents/hut1_12v2.pdf<br/>

there's a bluetooth connect issue in latest LineageOS ROM for Redmi 2, but the old one is fine.<br/>
if bluetooth keep disconnect, reboot the Android device, or forget the device from both side, then pair again.
