package com.example.ros_android_camera_controller_vr;

import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.WindowManager;

import org.ros.address.InetAddressFactory;
import org.ros.android.RosActivity;
import org.ros.android.view.RosImageView;
import org.ros.node.NodeConfiguration;
import org.ros.node.NodeMainExecutor;

public class MainActivity extends RosActivity {

    // Fields
    private OrientationPublisher mOrientationPublisher;
    private SensorManager mSensorManager;
    private CardboardOverlayView mCardboardOverlayView;
    private RosImageView mRightRosImageView, mLeftRosImageView;

    // Constructors
    public MainActivity() {
        super("Camera Controller",
                "Camera Controller");
    }

    // Protected Methods
    @SuppressWarnings("unchecked")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        // Set up the sensor manager for the orientation data
        mSensorManager = (SensorManager)this.getSystemService(SENSOR_SERVICE);

        // Set up stuff for the image subscriber node. This CardboardOverlayView is a view that
        // contains two offset RosImageView objects displayed next to each other for stereo vision
        mCardboardOverlayView = (CardboardOverlayView) findViewById(R.id.overlay);
        mCardboardOverlayView.setTopicInformation(
                "webcam/image_raw/compressed",
                sensor_msgs.CompressedImage._TYPE);
        // Get the right and left RosImageView objects from the overlay
        mRightRosImageView = mCardboardOverlayView.getRosImageView(CardboardOverlayView.Side.RIGHT);
        mLeftRosImageView = mCardboardOverlayView.getRosImageView(CardboardOverlayView.Side.LEFT);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void init(NodeMainExecutor nodeMainExecutor) {
        // Sensor delay is 50 Hz
        int sensorDelay = 20000;

        // Compressed image subscriber nodes
        if (mRightRosImageView != null && mLeftRosImageView != null && nodeMainExecutor != null) {
            // Configurations for the left and right image nodes
            NodeConfiguration rightImageViewConfig =
                    NodeConfiguration.newPublic(InetAddressFactory.newNonLoopback().getHostName())
                            .setMasterUri(getMasterUri());
            rightImageViewConfig.setNodeName("right_eye");
            NodeConfiguration leftImageViewConfig =
                    NodeConfiguration.newPublic(InetAddressFactory.newNonLoopback().getHostName())
                            .setMasterUri(getMasterUri());
            leftImageViewConfig.setNodeName("left_eye");

            // Run the subscribers
            nodeMainExecutor.execute(mRightRosImageView, rightImageViewConfig);
            nodeMainExecutor.execute(mLeftRosImageView, leftImageViewConfig);
        }

        // Orientation publisher node configuration
        NodeConfiguration orientationPublisherNodeConfiguration =
                NodeConfiguration.newPublic(InetAddressFactory.newNonLoopback().getHostAddress());
        orientationPublisherNodeConfiguration.setMasterUri(getMasterUri());
        orientationPublisherNodeConfiguration.setNodeName("orientation_publisher");

        // Create the publisher class
        this.mOrientationPublisher = new OrientationPublisher(mSensorManager, sensorDelay);

        // Run the orientation publisher
        nodeMainExecutor.execute(this.mOrientationPublisher, orientationPublisherNodeConfiguration);
    }
}
