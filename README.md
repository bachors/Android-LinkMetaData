# Android-LinkMetaData
[![Android Arsenal](https://img.shields.io/badge/Android%20Arsenal-PrefixInput-red.svg?style=flat)](https://android-arsenal.com/details/1/6069)
[![Release](https://jitpack.io/v/bachors/Android-Prefix-Input.svg)](https://jitpack.io/#bachors/Android-Prefix-Input)

Get Link Meta Data Android.

![gif](http://i.giphy.com/xUNd9GYGbRUbmfojv2.gif)

Gradle
------
```
allprojects {
   repositories {
      ...
      maven { url 'https://jitpack.io' }
   }
}
```
```
dependencies {
    ...
    compile 'com.github.bachors:Android-LinkMetaData:1.0'
}
```

Usage
-----
```java
LinkMetaData LMD = new LinkMetaData();
LMD.url("http://.....");
LMD.setMetaDataListener(new LinkMetaData.Listener() {
	@Override
	public void onResponse(LinkMetaData.Model metaData) {
		// success: "getUrl", "getIcon", "getImage", "getTitle", "getDomain", "getDescription"
		Toast.makeText(getApplicationContext(), metaData.getDescription(), Toast.LENGTH_LONG).show();
	}
	@Override
	public void onFailure(String message) {
		// error
		Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
	}
});
```
  
MIT
-----
