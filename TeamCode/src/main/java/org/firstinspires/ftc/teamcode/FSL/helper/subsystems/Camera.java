package org.firstinspires.ftc.teamcode.FSL.helper.subsystems;

import android.annotation.SuppressLint;

import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.teamcode.FSL.helper.configs.CameraConfig;
import org.firstinspires.ftc.teamcode.FSL.helper.scoring.Motif;
import org.firstinspires.ftc.teamcode.FSL.helper.configs.TurretConfig;
import org.firstinspires.ftc.vision.VisionPortal;
import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;
import org.firstinspires.ftc.vision.apriltag.AprilTagProcessor;

import java.util.List;

public class Camera {
    private final Telemetry telemetry;
    private final VisionPortal visionPortal;
    private final AprilTagProcessor aprilTagProcessor;
    public Motif motif;
    private final int targetID;
    public double x;
    public double y;
    public double range;
    public double camBearing;

    public boolean locked = false;
    public Camera(HardwareMap hm, Telemetry telemetry, boolean isBlue) {
        this.telemetry = telemetry;
        aprilTagProcessor = new AprilTagProcessor.Builder()
                .setDrawAxes(true)
                .setOutputUnits(DistanceUnit.INCH, AngleUnit.RADIANS)
                .setLensIntrinsics(236.887, 236.887, 298.024, 138.613)
                .build();
        visionPortal = new VisionPortal.Builder()
                .setCamera(hm.get(WebcamName.class, "CAM"))
                .addProcessor(aprilTagProcessor)
                .enableLiveView(true)
                .setStreamFormat(VisionPortal.StreamFormat.MJPEG)
                .build();

        camBearing = 0;
        x = 0;
        y = 0;
        range = 0;

        if(isBlue){targetID = CameraConfig.BLUE_TARGET_ID;}
        else{ targetID = CameraConfig.RED_TARGET_ID; }
    }

    public void stopStreaming() { visionPortal.stopStreaming(); }
    public void resumeStreaming() { visionPortal.resumeStreaming(); }

    public void focusOnAprilTag() {
        List<AprilTagDetection> detections = aprilTagProcessor.getDetections();

        locked = false;

        for (AprilTagDetection detection : detections) {
            if (detection.id == targetID) {

                locked = true;

                // AprilTag pose is robot-centric:
                // x = right
                // y = forward
                x = detection.ftcPose.x;
                y = detection.ftcPose.y;

                range = detection.ftcPose.range;

                // Bearing is robot-relative (radians already)
                camBearing = detection.ftcPose.bearing;

                return; // IMPORTANT
            }
        }

        // Only zero if not found
        x = 0;
        y = 0;
        range = 0;
        camBearing = 0;
    }


    public void update(){
        focusOnAprilTag();
        sendTelemetry();
    }
    public boolean readMotif() {
        List<AprilTagDetection> currentDetections = aprilTagProcessor.getDetections();
        for (AprilTagDetection a : currentDetections) {
            switch (a.id) {
                case 21:
                    motif = Motif.PPG;
                    return true;
                case 22:
                    motif = Motif.PGP;
                    return true;
                case 23:
                    motif = Motif.GPP;
                    return true;
            }
        }
        motif = Motif.PPG;
        return false;
    }
    public void sendTelemetry(){
        telemetry.addLine("CAMERA SWIVEL - VISION\n");
        telemetry.addData("LOCKED", locked);
        telemetry.addData("MOTIF", motif);
        telemetry.addData("ROBOT X", x);
        telemetry.addData("ROBOT Y", y);
        telemetry.addData("CAM BEARING (rad)", camBearing);
    }
}