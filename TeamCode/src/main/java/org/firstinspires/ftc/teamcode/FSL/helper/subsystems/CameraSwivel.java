package org.firstinspires.ftc.teamcode.FSL.helper.subsystems;

import android.annotation.SuppressLint;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.teamcode.FSL.helper.Motif;
import org.firstinspires.ftc.teamcode.FSL.helper.PIDController;
import org.firstinspires.ftc.teamcode.FSL.helper.configs.CameraDetectionConfig;
import org.firstinspires.ftc.vision.VisionPortal;
import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;
import org.firstinspires.ftc.vision.apriltag.AprilTagProcessor;

import java.util.ArrayList;
import java.util.List;

public class CameraSwivel {
    private final Telemetry telemetry;
    private final VisionPortal visionPortal;
    private final AprilTagProcessor aprilTagProcessor = AprilTagProcessor.easyCreateWithDefaults();
    private final DcMotorEx swivelMotor;
    private final PIDController pidController;
    private final int targetID;
    public double range;

    public boolean locked = false;
    public CameraSwivel(HardwareMap hm, Telemetry telemetry, boolean isBlue) {
        swivelMotor = hm.get(DcMotorEx.class, "CSM");
        swivelMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        swivelMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);  // Or RUN_TO_POSITION if preferred
        swivelMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        this.telemetry = telemetry;

        visionPortal = VisionPortal.easyCreateWithDefaults(
                hm.get(WebcamName.class, "Webcam 1"),
                aprilTagProcessor
        );

        pidController = new PIDController(CameraDetectionConfig.KP, CameraDetectionConfig.KI, CameraDetectionConfig.KD, CameraDetectionConfig.CENTRALTOLERANCE);
        range = 0;

        if(isBlue){ targetID = 20;}
        else{ targetID = 24; }
    }

    public void stopStreaming() { visionPortal.stopStreaming(); }
    public void resumeStreaming() { visionPortal.resumeStreaming(); }

    @SuppressLint("DefaultLocale")
    public void focusOnAprilTag(boolean sendTelemetry) {
        List<AprilTagDetection> currentDetections = aprilTagProcessor.getDetections();
        if (sendTelemetry) {
            telemetry.addLine("Webcam\n");
            telemetry.addData("# AprilTags Detected", currentDetections.size());
        }

        boolean tagFound = false;
        for (AprilTagDetection detection : currentDetections) {
            if (detection.id == targetID) {
                tagFound = true;

                double tickBearing = detection.ftcPose.bearing * CameraDetectionConfig.TICKSPERDEGREE;
                if (sendTelemetry) {
                    telemetry.addLine(String.format("\n==== (ID %d) %s", detection.id, detection.metadata.name));
                    telemetry.addLine(String.format("XYZ %6.1f %6.1f %6.1f  (inch)", detection.ftcPose.x, detection.ftcPose.y, detection.ftcPose.z));
                    telemetry.addLine(String.format("PRY %6.1f %6.1f %6.1f  (deg)", detection.ftcPose.pitch, detection.ftcPose.roll, detection.ftcPose.yaw));
                    telemetry.addLine(String.format("RBE %6.1f %6.1f %6.1f  (inch, deg, deg)", detection.ftcPose.range, detection.ftcPose.bearing, detection.ftcPose.elevation));

                    telemetry.addData("\nTICKS TO ADJUST", tickBearing);
                }
                if(Math.abs(swivelMotor.getCurrentPosition() + tickBearing) <= CameraDetectionConfig.MAXOFFSET){
                    pidController.setTarget(tickBearing, true);
                }
                range = detection.ftcPose.range;
            }
        }

        if (sendTelemetry) {
            telemetry.addLine("\nkey:\nXYZ = X (Right), Y (Forward), Z (Up) dist.");
            telemetry.addLine("PRY = Pitch, Roll & Yaw (XYZ Rotation)");
            telemetry.addLine("RBE = Range, Bearing & Elevation");


            telemetry.addLine("\nDIAGNOSTICS");
            telemetry.addData("Motor Pos", swivelMotor.getCurrentPosition());
            telemetry.addData("Motor Vel", swivelMotor.getVelocity());
            telemetry.addData("Locked", locked);
        }

        if (!tagFound) {
            pidController.reset();
        }
    }

    public void update(boolean sendTelemetry){
        focusOnAprilTag(sendTelemetry);
        swivelMotor.setVelocity(CameraDetectionConfig.MAXVEL * pidController.calculateScalar(swivelMotor.getCurrentPosition()));
    }

    public void stop() {
        swivelMotor.setPower(0);
        swivelMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);  // Back to velocity if needed
    }
    public void jog(double power) {
        swivelMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        swivelMotor.setPower(power);
    }

    public Motif readMotif() {
        List<AprilTagDetection> currentDetections = aprilTagProcessor.getDetections();
        for (AprilTagDetection a : currentDetections) {
            switch (a.id) {
                case 21: return Motif.PPG;
                case 22: return Motif.PGP;
                case 23: return Motif.GPP;
            }
        }
        return Motif.PPG;
    }
}