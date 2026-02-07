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
    public Motif motif;
    private final int targetID;
    public double range;
    public double tickBearing;

    public boolean locked = false;
    public CameraSwivel(HardwareMap hm, Telemetry telemetry, boolean isBlue, boolean isAuto) {
        swivelMotor = hm.get(DcMotorEx.class, "CSM");
        swivelMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        swivelMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        swivelMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        this.telemetry = telemetry;

        visionPortal = VisionPortal.easyCreateWithDefaults(
                hm.get(WebcamName.class, "CAM"),
                aprilTagProcessor
        );
        pidController = new PIDController(CameraDetectionConfig.KP, CameraDetectionConfig.KI, CameraDetectionConfig.KD, CameraDetectionConfig.CENTRALTOLERANCE);

        tickBearing = 0;
        range = 0;
        if(isBlue){ targetID = 20;}
        else{ targetID = 24; }
        if(!isAuto){
            readMotif();
        }
    }

    public void stopStreaming() { visionPortal.stopStreaming(); }
    public void resumeStreaming() { visionPortal.resumeStreaming(); }

    @SuppressLint("DefaultLocale")
    public void focusOnAprilTag(boolean sendTelemetry) {
        List<AprilTagDetection> currentDetections = aprilTagProcessor.getDetections();

        boolean tagFound = false;
        for (AprilTagDetection detection : currentDetections) {
            if (detection.id == targetID) {
                tagFound = true;

                tickBearing = detection.ftcPose.bearing * CameraDetectionConfig.TICKSPERDEGREE;
                if(Math.abs(swivelMotor.getCurrentPosition() + tickBearing) <= CameraDetectionConfig.MAXOFFSET){
                    pidController.setTarget(tickBearing, true);
                }
                range = detection.ftcPose.range;
            }
        }

        if (!tagFound) {
            pidController.reset();
        }
    }

    public void update(boolean sendTelemetry, double lateralOverride){
        if(Math.abs(lateralOverride) < 0.2){
            focusOnAprilTag(sendTelemetry);
            swivelMotor.setVelocity(CameraDetectionConfig.MAXVEL * pidController.calculateScalar(swivelMotor.getCurrentPosition()));
        }
        else{
            jog(lateralOverride);
        }
        if(sendTelemetry){
            sendTelemetry();
        }
    }

    public void update(boolean sendTelemetry){
        focusOnAprilTag(sendTelemetry);
        swivelMotor.setVelocity(CameraDetectionConfig.MAXVEL * pidController.calculateScalar(swivelMotor.getCurrentPosition()));
        if(sendTelemetry){
            sendTelemetry();
        }
    }

    public void stop() {
        swivelMotor.setVelocity(0);
    }
    public void jog(double scalar) {
        swivelMotor.setVelocity(CameraDetectionConfig.MAXVEL * scalar);
    }

    public void readMotif() {
        List<AprilTagDetection> currentDetections = aprilTagProcessor.getDetections();
        for (AprilTagDetection a : currentDetections) {
            switch (a.id) {
                case 21:
                    motif = Motif.PPG;
                    return;
                case 22:
                    motif = Motif.PGP;
                    return;
                case 23:
                    motif = Motif.GPP;
                    return;
            }
        }
        motif = Motif.PPG;
    }

    public void sendTelemetry(){
        telemetry.addLine("HARDWARE\n");
        telemetry.addData("MOTOR POS", swivelMotor.getCurrentPosition());
        telemetry.addData("MOTOR TARGET", pidController.target);
        telemetry.addData("TARGET TOLERANCE", pidController.tolerance);
        telemetry.addData("MOTOR VEL", swivelMotor.getVelocity());
        telemetry.addData("CAMERA LOCKED", locked);

        telemetry.addLine("VISION\n");
        telemetry.addData("TARGET RANGE", range);
        telemetry.addData("TARGET BEARING (ticks)", tickBearing);
    }
}