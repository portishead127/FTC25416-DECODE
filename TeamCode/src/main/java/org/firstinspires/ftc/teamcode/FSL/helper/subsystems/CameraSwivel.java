package org.firstinspires.ftc.teamcode.FSL.helper.subsystems;

import android.annotation.SuppressLint;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.teamcode.FSL.helper.configs.CameraDetectionConfig;
import org.firstinspires.ftc.vision.VisionPortal;
import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;
import org.firstinspires.ftc.vision.apriltag.AprilTagProcessor;

import java.util.List;

import dev.nextftc.core.commands.Command;
import dev.nextftc.core.commands.utility.InstantCommand;
import dev.nextftc.core.commands.utility.LambdaCommand;
import dev.nextftc.core.commands.utility.NullCommand;
import dev.nextftc.core.subsystems.Subsystem;
import dev.nextftc.ftc.ActiveOpMode;
import dev.nextftc.hardware.impl.ServoEx;
import dev.nextftc.hardware.positionable.SetPosition;

public class CameraSwivel implements Subsystem {
    public int motifNumber = 1;
    public static CameraSwivel INSTANCE = new CameraSwivel();
    private VisionPortal visionPortal;
    private final AprilTagProcessor aprilTagProcessor = AprilTagProcessor.easyCreateWithDefaults();
    private final ServoEx swivelServo = new ServoEx("SWS");
    private boolean lastScannedRight = false;

    @Override
    public void initialize() {
        visionPortal = VisionPortal.easyCreateWithDefaults(
                ActiveOpMode.hardwareMap().get(WebcamName.class, "Webcam 1"), aprilTagProcessor
        );
    }
    public Command stopStreaming = new InstantCommand(() -> {
        visionPortal.stopStreaming();
    });

    public Command resumeStreaming = new InstantCommand(() -> {
        visionPortal.resumeStreaming();
    });
    @SuppressLint("DefaultLocale")
    public Command focusOnAprilTag = new LambdaCommand()
            .setUpdate(() -> {
                ActiveOpMode.telemetry().addLine("Webcam\n");

                List<AprilTagDetection> currentDetections = aprilTagProcessor.getDetections();
                ActiveOpMode.telemetry().addData("# AprilTags Detected", currentDetections.size());

                // Step through the list of detections and display info for each one.
                for (AprilTagDetection detection : currentDetections) {
                    if (detection.id == 20 /*blue, 24 for red*/) {
                        ActiveOpMode.telemetry().addLine(String.format("\n==== (ID %d) %s", detection.id, detection.metadata.name));
                        ActiveOpMode.telemetry().addLine(String.format("XYZ %6.1f %6.1f %6.1f  (inch)", detection.ftcPose.x, detection.ftcPose.y, detection.ftcPose.z));
                        ActiveOpMode.telemetry().addLine(String.format("PRY %6.1f %6.1f %6.1f  (deg)", detection.ftcPose.pitch, detection.ftcPose.roll, detection.ftcPose.yaw));
                        ActiveOpMode.telemetry().addLine(String.format("RBE %6.1f %6.1f %6.1f  (inch, deg, deg)", detection.ftcPose.range, detection.ftcPose.bearing, detection.ftcPose.elevation));

                        evaluateBearing(detection.ftcPose.bearing).schedule();
                    }
                    // Add "key" information to telemetry
                    ActiveOpMode.telemetry().addLine("\nkey:\nXYZ = X (Right), Y (Forward), Z (Up) dist.");
                    ActiveOpMode.telemetry().addLine("PRY = Pitch, Roll & Yaw (XYZ Rotation)");
                    ActiveOpMode.telemetry().addLine("RBE = Range, Bearing & Elevation");

                    determineScan().schedule();
                }
            })
            .setStop((interrupted) -> {
                visionPortal.close();
            })
            .setInterruptible(true)
            .perpetually();

    public Command readMotif = new InstantCommand(() -> {
        AprilTagDetection currentDetection = aprilTagProcessor.getDetections().get(0);
        switch(currentDetection.id){
            case 21:
                motifNumber = 1;
                break;
            case 22:
                motifNumber = 2;
                break;
            case 23:
                motifNumber = 3;
                break;
            default:
                motifNumber = 0;
        }
    });

    public Command evaluateBearing(double tx){
        if(tx < -CameraDetectionConfig.CENTRALTOLERANCE) return focusRight;
        else if (tx > CameraDetectionConfig.CENTRALTOLERANCE) return focusLeft;
        else return new NullCommand();
    }
    public Command determineScan(){
        if(swivelServo.getPosition() == 1){
            lastScannedRight = true;
            return scanRight;
        }
        else if(swivelServo.getPosition() == 0){
            lastScannedRight = false;
            return scanLeft;
        }

        if(lastScannedRight) return scanLeft;
        return scanRight;
    }
    public Command focusLeft = new SetPosition(swivelServo, swivelServo.getPosition() - CameraDetectionConfig.FOCUSDX);
    public Command focusRight = new SetPosition(swivelServo, swivelServo.getPosition() + CameraDetectionConfig.FOCUSDX);
    public Command scanLeft = new SetPosition(swivelServo, swivelServo.getPosition() - CameraDetectionConfig.SCANDX);
    public Command scanRight = new SetPosition(swivelServo, swivelServo.getPosition() + CameraDetectionConfig.SCANDX);

}
