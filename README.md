<h1 align=center>
<img src="logo/1024.svg" width=100%>
</h1>

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




