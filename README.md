# ParkedTextView
An EditText with a constant text in the start.

<!--![](https://github.com/gotokatsuya/ParkedTextView/blob/master/doc/demo-gif.gif)-->


## Differences from main repo

  1. Parked text is at the start of the rest of the text
  2. Typing is smoother. You can long press backspace to delete paragraph of text.


## How to use

```xml

<com.goka.parkedtextview.ParkedTextView
    xmlns:app="http://schemas.android.com/apk/res-auto"
    android:id="@+id/parked_text_view"
    android:layout_width="wrap_content"
    android:layout_height="wrap_content"
    android:background="@android:color/transparent"
    android:layout_centerInParent="true"
    android:textSize="24sp"
    app:parkedText="In a hole in the ground "
    app:parkedHint="there lived a hobbit"
    app:parkedTextColor="FFFFFF"
    app:parkedHintColor="CCCCCC"
    app:parkedTextBold="true"
    />

```

## Gradle


```java
allprojects {
    repositories {
        ...
        maven { url "https://jitpack.io" }
    }
}

```

```java

dependencies {
    compile 'com.github.AdeelTariq:ParkedTextView:2.0.2'
}
```



## Reference
[ParkedTextField for iOS](https://github.com/gmertk/ParkedTextField)
