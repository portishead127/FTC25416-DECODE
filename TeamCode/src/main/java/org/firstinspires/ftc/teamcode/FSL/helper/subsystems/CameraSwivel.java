package org.firstinspires.ftc.teamcode.FSL.helper.subsystems;

import android.annotation.SuppressLint;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.teamcode.FSL.helper.Motif;
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
    private final int targetID;

    public boolean locked = false;
    private double integral = 0.0;
    private double lastError = 0.0;
    private long lastTime;
    public CameraSwivel(HardwareMap hm, Telemetry telemetry, boolean isBlue) {
        swivelMotor = hm.get(DcMotorEx.class, "CSM");
        swivelMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        swivelMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);  // Or RUN_TO_POSITION if preferred
        swivelMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        // Optional: Fine-tune motor PIDF if needed
        // swivelMotor.setPositionPIDFCoefficients(10.0);  // P-only for position hold

        visionPortal = VisionPortal.easyCreateWithDefaults(
                hm.get(WebcamName.class, "Webcam 1"),
                aprilTagProcessor
        );

        this.telemetry = telemetry;
        lastTime = System.currentTimeMillis();

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
        locked = false;

        for (AprilTagDetection detection : currentDetections) {
            if (detection.id == targetID) {
                tagFound = true;
                if (sendTelemetry) {
                    telemetry.addLine(String.format("\n==== (ID %d) %s", detection.id, detection.metadata.name));
                    telemetry.addLine(String.format("XYZ %6.1f %6.1f %6.1f  (inch)", detection.ftcPose.x, detection.ftcPose.y, detection.ftcPose.z));
                    telemetry.addLine(String.format("PRY %6.1f %6.1f %6.1f  (deg)", detection.ftcPose.pitch, detection.ftcPose.roll, detection.ftcPose.yaw));
                    telemetry.addLine(String.format("RBE %6.1f %6.1f %6.1f  (inch, deg, deg)", detection.ftcPose.range, detection.ftcPose.bearing, detection.ftcPose.elevation));
                }
                trackBearing(detection.ftcPose.bearing);
            }
        }

        if (sendTelemetry) {
            telemetry.addLine("\nkey:\nXYZ = X (Right), Y (Forward), Z (Up) dist.");
            telemetry.addLine("PRY = Pitch, Roll & Yaw (XYZ Rotation)");
            telemetry.addLine("RBE = Range, Bearing & Elevation");
            telemetry.addData("Motor Pos", swivelMotor.getCurrentPosition());
            telemetry.addData("Motor Vel", swivelMotor.getVelocity());
            telemetry.addData("Locked", locked);
        }

        if (!tagFound) {
            stop();  // No tag → stop motor to save power
            integral = 0.0;
            lastError = 0.0;
        }
    }

    private void trackBearing(double bearing) {
        long now = System.currentTimeMillis();
        double dt = (now - lastTime) / 1000.0;
        lastTime = now;
        if (dt <= 0) return;

        // Integral (optional, usually 0 for vision)
        integral += bearing * dt;
        integral = Math.max(-30, Math.min(30, integral));

        double derivative = (bearing - lastError) / dt;
        lastError = bearing;

        double velocityDegPerSec =
                CameraDetectionConfig.KP * bearing +
                CameraDetectionConfig.KI * integral +
                CameraDetectionConfig.KD * derivative;

        // Clamp angular velocity
        velocityDegPerSec = Math.max(
                -CameraDetectionConfig.MAX_DEG_PER_SEC,
                Math.min(CameraDetectionConfig.MAX_DEG_PER_SEC, velocityDegPerSec)
        );

        // Convert to encoder velocity
        double velocityTicksPerSec = velocityDegPerSec * CameraDetectionConfig.TICKS_PER_DEGREE;

        // ----- APPLY -----
        swivelMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        swivelMotor.setVelocity(velocityTicksPerSec);

        // Lock detection
        locked = Math.abs(bearing) < CameraDetectionConfig.CENTRALTOLERANCE;
    }


    public void stop() {
        swivelMotor.setPower(0);
        swivelMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);  // Back to velocity if needed
    }

    // Optional: manual jog (for testing/tuning)
    public void jogLeft(double power) {
        swivelMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        swivelMotor.setPower(-power);  // Direction depends on wiring
    }

    public void jogRight(double power) {
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