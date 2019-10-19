# Spotlight kotlin edition

Fork of [Spotlight](https://github.com/TakuSemba/Spotlight) library that is rewritten in Kotlin with added support of continue button and different shapes. Also the min api was raised to 21 to ensure all features work as intended on every supported API. Examples are kept in Java to properly test and show how it can be used without Kotlin. Kotlin usage is usually simplified and can be derived from the Java code.

[![Download](https://api.bintray.com/packages/adsamcik/android-forks/spotlight/images/download.svg)](https://bintray.com/adsamcik/android-forks/spotlight/_latestVersion)
![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)
![API](https://img.shields.io/badge/API-21%2B-brightgreen.svg?style=flat)

## Gradle

```groovy
dependencies {
    implementation 'com.adsamcik:spotlight:2.0.0'
}
```

## Usage

```java

Spotlight.with(this)
        .setOverlayColor(ContextCompat.getColor(MainActivity.this, R.color.background)) // background overlay color
        .setDuration(1000L) // duration of Spotlight emerging and disappearing in ms
        .setAnimation(new DecelerateInterpolator(2f)) // animation of Spotlight
        .setTargets(firstTarget, secondTarget, thirdTarget ...) // set targets. see below for more info
        .setClosedOnTouchedOutside(false) // set if target is closed when touched outside
        .setOnSpotlightStartedListener(new OnSpotlightStartedListener() { // callback when Spotlight starts
            @Override
            public void onStarted() {
                Toast.makeText(context, "spotlight is started", Toast.LENGTH_SHORT).show();
            }
        })
        .setOnSpotlightEndedListener(new OnSpotlightEndedListener() { // callback when Spotlight ends
            @Override
            public void onEnded() {
                Toast.makeText(context, "spotlight is ended", Toast.LENGTH_SHORT).show();
            }
        })
        .start(); // start Spotlight

```

if you want to show Spotlight immediately, use `addOnGlobalLayoutListener` to wait until views are drawn.

```java
view.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
    @Override public void onGlobalLayout() {
        view.getViewTreeObserver().removeOnGlobalLayoutListener(this);
        Spotlight.with(this)...start();
    }
});
```

<img src="https://github.com/TakuSemba/Spotlight/blob/master/arts/simpleTarget.gif" align="left" width="30%">

## Simple Target

simply set a title and description, these position will be automatically calculated.

```java

SimpleTarget simpleTarget = new SimpleTarget.Builder(this)
    .setPoint(100f, 340f) // position of the Target. setPoint(Point point), setPoint(View view) will work too.
    .setRadius(80f) // radius of the Target
    .setTitle("the title") // title
    .setDescription("the description") // description
    .setOnSpotlightStartedListener(new OnTargetStateChangedListener<SimpleTarget>() {
        @Override
        public void onStarted(SimpleTarget target) {
            // do something
        }
        @Override
        public void onEnded(SimpleTarget target) {
            // do something
        }
    })
    .build();

```

<img src="https://github.com/TakuSemba/Spotlight/blob/master/arts/customTarget.gif" align="left" width="30%">

## Custom Target

use your own custom view.

```java

CustomTarget customTarget = new CustomTarget.Builder(this)
    .setPoint(100f, 340f) // position of the Target. setPoint(Point point), setPoint(View view) will work too.
    .setRadius(80f) // radius of the Target
    .setView(view) // custom view
    .setOnSpotlightStartedListener(new OnTargetStateChangedListener<CustomTarget>() {
        @Override
        public void onStarted(CustomTarget target) {
            // do something
        }
        @Override
        public void onEnded(CustomTarget target) {
            // do something
        }
    })
    .build();

```

### Sample

Clone this repo and check out the [app](https://github.com/TakuSemba/Spotlight/tree/master/app) module.

## Change Log

### Version: 2.2.2

* Custom view is guaranteed to have a background for readability purposes

### Version: 2.0.0, 2.1.0

* Rewrite to Kotlin
* Support for shapes

### Version: 1.3.0

* click handling added

### Version: 1.2.0

* overlay color added

### Version: 1.0.3

* add listener to target

### Version: 1.0.1, 1.0.2

* bug fix

## Author

* **Taku Semba**
* **Github** - (https://github.com/takusemba)
* **Twitter** - (https://twitter.com/takusemba)
* **Facebook** - (https://www.facebook.com/takusemba)

## Licence

```
Copyright 2018 Adsamcik.
Copyright 2017 Taku Semba.

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
