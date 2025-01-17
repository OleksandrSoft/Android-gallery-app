Mobile Gallery App(Android)
=======

A simple and useful Android Gallery.

# How to Use

#### Gradle

```gradle
allprojects  {
    repositories {
        maven { url 'https://jitpack.io' }
    }
}
dependencies {
    implementation "com.github.jdsdhp:kutil:0.1.9"
    implementation 'com.github.jdsdhp:gallery-droid:0.1.4'
}
```

## Basic Sample
Gallery Screen|Picture Screen |Inmersive Screen
:-:|:-:|:-:
![](art/art-01.jpg) | ![](art/art-02.jpg) | ![](art/art-03.jpg)

#### Kotlin
Images in this example have been loaded from [Lorem Picsum](https://picsum.photos).

Usage from Activity or Fragment. The fragment (GalleryFragment) is usually added through a transition or directly from XML.
```kotlin
supportFragmentManager.beginTransaction()
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE) //Optional
            .replace(R.id.container, GalleryFragment.newInstance())
            .commit()
```
All that remains is to add elements to the gallery. It can be done using the onAttachFragment method in the Activity or Fragment where you have the GalleryFragment.
```kotlin
override fun onAttachFragment(fragment: Fragment) {
        super.onAttachFragment(fragment)
        if (fragment is GalleryFragment) {
            fragment.injectGallery(
                GalleryDroid(
                    listOf(
                        Picture(
                            fileURL = "https://picsum.photos/id/15/720/1000",
                            fileThumbURL = "https://picsum.photos/id/15/200",
                            fileName = "Lorem Ipsum 1", //Optional
                        ),
                        Picture(
                            fileURL = "https://picsum.photos/id/16/720/1000",
                            fileThumbURL = "https://picsum.photos/id/16/200",
                            fileName = "Lorem Ipsum 2", //Optional
                        ),
                        Picture(
                            fileURL = "https://picsum.photos/id/14/720/1000",
                            fileThumbURL = "https://picsum.photos/id/14/200",
                            fileName = "Lorem Ipsum 3", //Optional
                        )
                    )
                )
            )
        }
    }
```
#### XML
Add this to your AndroidManifest.xml file if you are using the default activity provided by the library. In case GalleryFragment is being used directly, it would not be necessary.
```xml
<activity
  android:name="com.jesusd0897.gallerydroid.view.activity.PictureDetailActivity"
  android:configChanges="orientation|screenSize"
  android:theme="@style/Theme.MaterialComponents.NoActionBar" />
```
## Advanced Sample
Custom Gallery | Custom Layout | Custom Transformation
:-:|:-:|:-:
![](art/art-04.jpg) | ![](art/art-05.jpg) | ![](art/art-06.jpg)
#### Kotlin
By overwriting onAttachFragment you can add some customizations to the gallery.
```kotlin
override fun onAttachFragment(fragment: Fragment) {
        super.onAttachFragment(fragment)
        if (fragment is GalleryFragment) {
            fragment.injectGallery(
                GalleryDroid(
                    //...your pictures
                )
                .layoutManager(GalleryDroid.LAYOUT_STAGGERED_GRID)
                .loadingPlaceholder(
                    ContextCompat.getDrawable(this, R.drawable.ic_custom_loading_placeholder)
                )
                .errorPlaceholder(
                    ContextCompat.getDrawable(this, R.drawable.ic_custom_error_placeholder)
                )
                .pictureCornerRadius(16f)
                .pictureElevation(8f)
                .transformer(GalleryDroid.TRANSFORMER_CUBE_OUT)
                .spacing(12)
                .portraitColumns(2)
                .landscapeColumns(4)
                .emptyTitle("Upps")
                .emptySubTitle(getString(R.string.no_items_found))
                .emptyIcon(ContextCompat.getDrawable(this, R.drawable.ic_round_find_in_page))
                .decoratorLayout(R.layout.custom_decorator)
                .autoClickHandler(true)
                .useLabels(false)
            )
        }
    }
```
##### Adding click listeners
If you want to hear the clicks on the elements of the gallery you can add an OnPictureItemClickListener to the GalleryFragment. If the "autoClickHandler" property has been used to true, PictureDetailActivity will still be opened, so that this does not happen it must be set to false.
```kotlin
fragment.onItemClickListener = object : OnPictureItemClickListener {
                override fun onClick(picture: Picture, position: Int) {
                    Toast.makeText(this@MainActivity, "onClick = $picture", Toast.LENGTH_SHORT)
                        .show()
                }

                override fun onLongClick(picture: Picture, position: Int) {
                    Toast.makeText(this@MainActivity, "onLongClick = $picture", Toast.LENGTH_SHORT)
                        .show()
                }
            }
```
##### Using just fragment detail
It's a way to use your own activity or fragment instead of using PictureDetailActivity as the default detail view handler. PicturePagerFragment is the one that contains the image viewer and can be used directly through a transition after having created your own OnPictureItemClickListener and having set "autoClickHandler" to false.

In your own onClick method:
```kotlin
override fun onClick(picture: Picture, position: Int){
     supportFragmentManager
        .beginTransaction()
        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE) //Optional
        .replace(R.id.container, 
            PicturePagerFragment.newInstance(
                pictures,
                position,
                transformerId
            )
        )
        .commit()  
}
```
