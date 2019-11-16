# Bitmap Converter

Basically, the Emoticon set that we use are stored as Vector Assets. Google Maps doesn't like Vector Assets when using a Map View and for some reason Google has no intention of ever fixing the issue of being able to have custom markers use Vector Asserts (see [here](https://issuetracker.google.com/issues/35827905).)

Luckily, there is a workaround that is up on Google's open-source sample repository. The specific functionality that we have used is taken from [this](https://github.com/googlemaps/android-samples/blob/c6c725061616045ee08ca612aa8bae17136a198d/ApiDemos/java/app/src/main/java/com/example/mapdemo/MarkerDemoActivity.java#L362) sample. The code shown below essentially converts our Vector Asset Emoticons into BitMap objects which Google Maps is happy with. Our actually usage and implementation of the vector to bitmap conversion is slightly modified to not include tinting. 

```java
    private BitmapDescriptor vectorToBitmap(@DrawableRes int id, @ColorInt int color) {
        Drawable vectorDrawable = ResourcesCompat.getDrawable(getResources(), id, null);
        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(),
                vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        vectorDrawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        DrawableCompat.setTint(vectorDrawable, color);
        vectorDrawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }
```
