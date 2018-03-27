# Anroid Call Recorder

[![](https://jitpack.io/v/aykuttasil/CallRecorder.svg)](https://jitpack.io/#aykuttasil/CallRecorder)
[![Android Arsenal](https://img.shields.io/badge/Android%20Arsenal-Call%20Recorder-brightgreen.svg?style=flat)](http://android-arsenal.com/details/1/4544)

Android incomig and outgoing call recorder at any time.

# How to Use

```java
callRecord = new CallRecord.Builder(this)
       .setRecordFileName("RecordFileName")
       .setRecordDirName("RecordDirName")
       .setRecordDirPath(Environment.getExternalStorageDirectory().getPath()) // optional & default value
       .setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB) // optional & default value
       .setOutputFormat(MediaRecorder.OutputFormat.AMR_NB) // optional & default value
       .setAudioSource(MediaRecorder.AudioSource.VOICE_COMMUNICATION) // optional & default value
       .setShowSeed(true) // optional & default value ->Ex: RecordFileName_incoming.amr || RecordFileName_outgoing.amr
       .build();


callRecord.startCallReceiver();
```

OR

```java
callRecord = CallRecord.init(this);
```


**Stop CallRecord**

```java
callRecord.stopCallReceiver();
```


---

If you wish run in Service;

```java
callRecord = new CallRecord.Builder(this)
   .setRecordFileName("RecordFileName")
   .setRecordDirName("RecordDirName")
   .setRecordDirPath(Environment.getExternalStorageDirectory().getPath()) // optional & default value
   .setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB) // optional & default value
   .setOutputFormat(MediaRecorder.OutputFormat.AMR_NB) // optional & default value
   .setAudioSource(MediaRecorder.AudioSource.VOICE_COMMUNICATION) // optional & default value
   .setShowSeed(true) // optional & default value ->Ex: RecordFileName_incoming.amr || RecordFileName_outgoing.amr
   .buildService();


callRecord.startCallRecordService();
```


# Optimize

If you wish stop save CallRecord file;

```java
callRecord.disableSaveFile();
```

and

```java
callRecord.enableSaveFile();
```



If you wish change save file name after initialize CallRecorder,

```java
callRecord.changeRecordFileName("NewFileName");
```
or
```java
callRecord.changeRecordDirName("NewDirName");
```
or
```java
callRecord.changeRecordDirPath("NewDirPath");
```


---

**Custom CallRecordReceiver**

```java
callRecord.changeReceiver(new MyCallRecordReceiver(callRecord));
```


# Installation

Gradle

Add it as a dependency in your app's build.gradle file

```groovy
allprojects {

	repositories {

	maven { url 'https://jitpack.io' }
	}
}
```

```groovy
compile 'com.github.aykuttasil:CallRecorder:$lastVersion'
```

# Sample

You can see sample project in app folder.


# Thank You
[Luong Vo](https://github.com/luongvo)


# License 

```
Copyright 2016 aykuttasil - Aykut Asil

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




