/*
 * DISCLAIMER: This file was modified from a Github project by user r2DoesInc. The changes made were
 * the addition of comments and the renaming of certain fields for clarification. The original file
 * can be found here:
 * https://github.com/cloudspace/ros_cardboard/blob/master/ros_cardboard_module/src/main/java/com/
 * cloudspace/cardboard/CardboardOverlayView.java
 */

package com.example.ros_android_camera_controller_vr;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import org.ros.android.view.RosImageView;

/**
 * Contains two sub-views to provide a simple stereo HUD.
 */
public class CardboardOverlayView extends LinearLayout {

    // Fields
    private CardboardOverlayEyeView mLeftView;
    private CardboardOverlayEyeView mRightView;
    AttributeSet mAttributes;

    // Enumerations
    public enum Side {
        LEFT(0), RIGHT(1);

        int side;

        Side(int side) {
            this.side = side;
        }
    }

    // Public Methods
    public RosImageView getRosImageView(Side side) {
        if (mRightView == null || mLeftView == null) {
            throw new IllegalStateException("Remember to call CardboardOverlayView.setTopicInformation(String topicName, String messageType)");
        }
        return side == Side.RIGHT ? mRightView.getImageView() : mLeftView.getImageView();
    }

    public void setTopicInformation(String topicName, String messageType) {
        LayoutParams params = new LayoutParams(
                LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, 1.0f);
        params.setMargins(0, 0, 0, 0);

        mLeftView = new CardboardOverlayEyeView(getContext(), mAttributes, topicName, messageType);
        mLeftView.setLayoutParams(params);
        addView(mLeftView);

        mRightView = new CardboardOverlayEyeView(getContext(), mAttributes, topicName, messageType);
        mRightView.setLayoutParams(params);
        addView(mRightView);

        // Set some reasonable defaults.
        setDepthOffset(0.016f);
        setColor(Color.rgb(150, 255, 180));
        setVisibility(View.VISIBLE);
    }

    public CardboardOverlayView(Context context, AttributeSet attributes) {
        super(context, attributes);
        this.mAttributes = attributes;
        setOrientation(HORIZONTAL);
    }

    private void setDepthOffset(float offset) {
        mLeftView.setOffset(offset);
        mRightView.setOffset(-offset);
    }

    private void setColor(int color) {
        mLeftView.setColor(color);
        mRightView.setColor(color);
    }
}
