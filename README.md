# flutter_native_pedometer

## Step Checker

### TODO
- [X] 안드로이드 서비스 돌고있는지
- [X] 안드로이드 노티피케이션 실행하는거 따로빼기(시작 시 초기값 넘겨주기)
- [X] 안드로이드 서비스 죽이기 만들기
- [ ] 안드로이드 권한 핸들링 => Permission Handler
- [ ] 안드로이드 시작값 기준시간으로 받기!(브로드캐스트용)

## IOS Setting
##### min : IOS 10
```
<key>NSMotionUsageDescription</key>
<string>This application tracks your steps</string>
<key>UIBackgroundModes</key>
<array>
    <string>processing</string>
</array>
```

## Android Setting
##### project -> build.gradle
```
ext.kotlin_version = '1.5.31'
```

#### AndroidManifest.xml
```
    <uses-permission android:name="android.permission.FOREGROUND_SERVICE" />
    <uses-permission android:name="android.permission.ACTIVITY_RECOGNITION" />

    ...

    </activity>

        <service android:name="net.halowd.flutter_native_pedometer.walker.WalkerService"></service>

```

```
    <!-- if you want auto start when reboot -->
    <uses-permission android:name="android.permission.RECEIVE_BOOT_COMPLETED" />
    <uses-permission android:name="android.permission.WAKE_LOCK" />

    ...

    <receiver
        android:name="net.halowd.flutter_native_pedometer.walker.BootReceiver"
        android:exported="true"
        android:enabled="true" >
        <intent-filter>
            <action android:name="android.intent.action.BOOT_COMPLETED" />
        </intent-filter>
    </receiver>

```


#### main/res/layout/walker_notification.xml 파일 만들기
```
// 해당 아이디의 텍스트뷰 필수!
android:id="@+id/tv_walker_km"
android:id="@+id/tv_walker_count"
android:id="@+id/tv_walker_cal"
```



