# BluetoothKeyboard
Use An Android Device As A Bluetooth Keyboard 


# :)

# Copy from
https://github.com/ginkage/wearmouse<br/>
https://github.com/domi1294/BluetoothHidDemo

# Bluetooth Name
Specific Name for auto reconnect: IPC6308

# How to use
1. iPhone Rename as IPC6308<br/>
2. Android kill BluetoothHidDemo APP<br/>
3. iPhone connect to Android via Bluetooth<br/>
4. open BluetoothHidDemo APP on Android<br/>

# Requirement
Android Pie 9, API 28<br/>
Bluetooth HID Profile Enabled


# Test Device 
Redmi Note 7, LineageOS 18.1, Android 11<br/>
iPhone 11, iOS 14.7

# Issue
switch this app to background, bluetooth will disconnect, you need restart this app. fixed!
Specific target name?

# Note
Shift Del just send Shift, and Win Del just send Win.<br/>
it uses scan code, not key code,<br/>
for example, Delete 76, Backspace 42, Ctrl 224<br/>
check it on https://www.usb.org/sites/default/files/documents/hut1_12v2.pdf
