/*
 * Copyright 2014 Google Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/*
 * DISCLAIMER: This file was modified from a Github project by user r2DoesInc. The changes made were
 * the addition of comments and the renaming of certain fields for clarification. Certain sections,
 * such as variables and methods related to a TextView object were removed because they were not
 * necessary for this project. The original file can be found here:
 * https://github.com/cloudspace/ros_cardboard/blob/master/ros_cardboard_module/src/main/java/com/
 * cloudspace/cardboard/CardboardOverlayEyeView.java
 */

package com.example.ros_android_camera_controller_vr;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.ImageView;

import org.ros.android.BitmapFromCompressedImage;
import org.ros.android.view.RosImageView;

public class CardboardOverlayEyeView extends ViewGroup {

    // Fields
    private final RosImageView imageView;
    private float offset;

    // Constructors
    public CardboardOverlayEyeView(Context context, AttributeSet attrs,
                                   String topicName, String messageType) {
        super(context, attrs);
        imageView = new RosImageView(context, attrs);
        imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
        imageView.setAdjustViewBounds(true);  // Preserve aspect ratio.
        imageView.setTopicName(topicName);
        imageView.setMessageType(messageType);
        imageView.setMessageToBitmapCallable(new BitmapFromCompressedImage());
        addView(imageView);
    }

    // Public Methods
    public void setColor(int color) {
        // Do nothing
    }

    public void setOffset(float offset) {
        // Set the offset to be applied to the RosImageView component (see end of the onLayout
        // method)
        this.offset = offset;
    }

    public RosImageView getImageView() {
        return imageView;
    }

    // Protected Methods
    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        // Width and height of this ViewGroup.
        final int width = right - left;
        final int height = bottom - top;

        // The size of the image, given as a fraction of the dimension as a ViewGroup. We multiply
        // both width and heading with this number to compute the image's bounding box. Inside the
        // box, the image is the horizontally and vertically centered.
        final float imageSize = 1.0f;

        // The fraction of this ViewGroup's height by which we shift the image off the ViewGroup's
        // center. Positive values shift downwards, negative values shift upwards.
        final float verticalImageOffset = -0.07f;

        // Layout ImageView
        float imageMargin = (1.0f - imageSize) / 2.0f;
        float leftMargin = (int) (width * (imageMargin + offset));
        float topMargin = (int) (height * (imageMargin + verticalImageOffset));
        imageView.layout(
                (int) leftMargin, (int) topMargin,
                (int) (leftMargin + width * imageSize), (int) (topMargin + height * imageSize));
    }
}
