# BluetoothKeyboard
Use An Android Device As A Bluetooth Keyboard 


# :)

# Copy from
https://github.com/ginkage/wearmouse
https://github.com/domi1294/BluetoothHidDemo

# Bluetooth Name
Specific Name for auto reconnect: IPC6308

# How to use
1. iPhone Rename as IPC6308
2. Android kill BluetoothHidDemo APP
3. iPhone connect to Android via Bluetooth
4. open BluetoothHidDemo APP on Android

# Requirement
Android Pie 9, API 28
Bluetooth HID Profile Enabled


# Test Device 
Redmi Note 7, LineageOS 18.1, Android 11
iPhone 11, iOS 14.7

# Issue
switch this app to background, bluetooth will disconnect, you need restart this app.

# Note
Shift Del just send Shift, and Win Del just send Win.
it uses scan code, not key code,
for example, Delete 76, Backspace 42, Ctrl 224
check it on https://www.usb.org/sites/default/files/documents/hut1_12v2.pdf
