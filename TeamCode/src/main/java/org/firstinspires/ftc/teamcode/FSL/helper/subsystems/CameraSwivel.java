package org.firstinspires.ftc.teamcode.FSL.helper.subsystems;

import android.annotation.SuppressLint;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.teamcode.FSL.helper.Motif;
import org.firstinspires.ftc.teamcode.FSL.helper.configs.CameraDetectionConfig;
import org.firstinspires.ftc.vision.VisionPortal;
import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;
import org.firstinspires.ftc.vision.apriltag.AprilTagProcessor;

import java.util.ArrayList;
import java.util.List;

public class CameraSwivel{
    private final Telemetry telemetry;
    private final VisionPortal visionPortal;
    private final AprilTagProcessor aprilTagProcessor = AprilTagProcessor.easyCreateWithDefaults();
    private final Servo swivelServo;
    private boolean lastScannedRight = false;
    public boolean locked = false;
    public CameraSwivel(HardwareMap hm, Telemetry telemetry){
        swivelServo = hm.get(Servo.class,"SWS");
        visionPortal = VisionPortal.easyCreateWithDefaults(hm.get(WebcamName.class, "Webcam 1"), aprilTagProcessor);
        this.telemetry = telemetry;
    }
    public void stopStreaming(){visionPortal.stopStreaming();}
    public void resumeStreaming(){visionPortal.resumeStreaming();}
    @SuppressLint("DefaultLocale")
    public void focusOnAprilTag(boolean sendTelemetry) {
        List<AprilTagDetection> currentDetections = aprilTagProcessor.getDetections();

        if (sendTelemetry) {
            telemetry.addLine("Webcam\n");
            telemetry.addData("# AprilTags Detected", currentDetections.size());
        }

        for (AprilTagDetection detection : currentDetections) {
            if (detection.id == 20 /*blue, 24 for red*/) {
                if (sendTelemetry) {
                    telemetry.addLine(String.format("\n==== (ID %d) %s", detection.id, detection.metadata.name));
                    telemetry.addLine(String.format("XYZ %6.1f %6.1f %6.1f  (inch)", detection.ftcPose.x, detection.ftcPose.y, detection.ftcPose.z));
                    telemetry.addLine(String.format("PRY %6.1f %6.1f %6.1f  (deg)", detection.ftcPose.pitch, detection.ftcPose.roll, detection.ftcPose.yaw));
                    telemetry.addLine(String.format("RBE %6.1f %6.1f %6.1f  (inch, deg, deg)", detection.ftcPose.range, detection.ftcPose.bearing, detection.ftcPose.elevation));
                }
                evaluateBearing(detection.ftcPose.bearing);
            }
            if (sendTelemetry) {
                telemetry.addLine("\nkey:\nXYZ = X (Right), Y (Forward), Z (Up) dist.");
                telemetry.addLine("PRY = Pitch, Roll & Yaw (XYZ Rotation)");
                telemetry.addLine("RBE = Range, Bearing & Elevation");
            }
            determineScan();
        }
    }
    public void evaluateBearing(double tx) {
        if (tx < -CameraDetectionConfig.CENTRALTOLERANCE) {
            focusRight();
            locked = false;
        }
        else if (tx > CameraDetectionConfig.CENTRALTOLERANCE) {
            focusLeft();
            locked = false;
        }
        else{
            locked = true;
        }
    }
    public void determineScan () {
        double pos = swivelServo.getPosition();

        if (pos == 1) {
            lastScannedRight = true;
            scanLeft();
        } else if (pos == 0) {
            lastScannedRight = false;
            scanRight();
        }

        if (lastScannedRight) { scanLeft(); }
        scanRight();
    }
    public void focusLeft () { swivelServo.setPosition(swivelServo.getPosition() - CameraDetectionConfig.FOCUSDX); }
    public void focusRight () { swivelServo.setPosition(swivelServo.getPosition() + CameraDetectionConfig.FOCUSDX); }
    public void scanLeft () { swivelServo.setPosition(swivelServo.getPosition() - CameraDetectionConfig.SCANDX); }
    public void scanRight () { swivelServo.setPosition(swivelServo.getPosition() + CameraDetectionConfig.SCANDX); }
    public Motif readMotif(){
        ArrayList<AprilTagDetection> currentDetections = aprilTagProcessor.getDetections();
        for (AprilTagDetection a: currentDetections) {
            switch(a.id){
                case 21:
                    return Motif.PPG;
                case 22:
                    return Motif.PGP;
                default:
                    return Motif.GPP;
            }
        }
        return Motif.PPG;
    }
}
