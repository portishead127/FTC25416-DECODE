package org.firstinspires.ftc.teamcode.FSL.helper.subsystems;

import android.annotation.SuppressLint;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.teamcode.FSL.helper.scoring.Motif;
import org.firstinspires.ftc.teamcode.FSL.helper.control.PIDController;
import org.firstinspires.ftc.teamcode.FSL.helper.configs.CameraDetectionConfig;
import org.firstinspires.ftc.vision.VisionPortal;
import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;
import org.firstinspires.ftc.vision.apriltag.AprilTagProcessor;

import java.util.List;

public class CameraSwivel {
    private final Telemetry telemetry;
    private final VisionPortal visionPortal;
    private final AprilTagProcessor aprilTagProcessor = AprilTagProcessor.easyCreateWithDefaults();
    private final DcMotorEx motor;
    private final PIDController pidController;
    public Motif motif;
    private final int targetID;
    public double x;
    public double y;
    private double tickBearing;

    public boolean locked = false;
    public CameraSwivel(HardwareMap hm, Telemetry telemetry, boolean isBlue, boolean isGreedyAuto) {
        motor = hm.get(DcMotorEx.class, "CSM");
        motor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        motor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        this.telemetry = telemetry;

        visionPortal = VisionPortal.easyCreateWithDefaults(
                hm.get(WebcamName.class, "CAM"),
                aprilTagProcessor
        );
        pidController = new PIDController(CameraDetectionConfig.KP, CameraDetectionConfig.KI, CameraDetectionConfig.KD, CameraDetectionConfig.CENTRALTOLERANCE);

        tickBearing = 0;
        x = 0;
        y = 0;
        if(isBlue){ targetID = 20;}
        else{ targetID = 24; }
        if(!isGreedyAuto){
            readMotif();
        }
    }

    public void stopStreaming() { visionPortal.stopStreaming(); }
    public void resumeStreaming() { visionPortal.resumeStreaming(); }

    @SuppressLint("DefaultLocale")
    public void focusOnAprilTag() {
        List<AprilTagDetection> currentDetections = aprilTagProcessor.getDetections();

        for (AprilTagDetection detection : currentDetections) {
            if (detection.id == targetID) {
                locked = true;
                tickBearing = detection.ftcPose.bearing * CameraDetectionConfig.TICKS_PER_DEGREE;
                setPIDTarget(tickBearing);
                x = detection.ftcPose.x;
                y = detection.ftcPose.y;
            }
            else{
                locked = false;
                tickBearing = 0;
                x = 0;
                y = 0;
            }
        }
    }

    public void setPIDTarget(double bearingToAdd){
        if(Math.abs(motor.getCurrentPosition() + bearingToAdd) <= CameraDetectionConfig.MAX_OFFSET){
            pidController.setTarget(bearingToAdd, true);
        }
    }

    public void update(double lateralOverride){
        if(Math.abs(lateralOverride) < 0.2){
            focusOnAprilTag();
            motor.setVelocity(CameraDetectionConfig.MAX_VEL * pidController.calculateScalar(motor.getCurrentPosition()));
        }
        else{
            jog(lateralOverride);
            pidController.reset();
        }
        sendTelemetry();

    }

    public void update(){
        focusOnAprilTag();
        motor.setVelocity(CameraDetectionConfig.MAX_VEL * pidController.calculateScalar(motor.getCurrentPosition()));
        sendTelemetry();
    }

    public void stop() {
        motor.setVelocity(0);
    }
    public void jog(double scalar) {
        motor.setVelocity(CameraDetectionConfig.MAX_VEL * scalar);
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
        telemetry.addLine("CAMERA SWIVEL - HARDWARE\n");
        telemetry.addData("MOTOR POS", motor.getCurrentPosition());
        telemetry.addData("MOTOR TARGET", pidController.target);
        telemetry.addData("TARGET TOLERANCE", pidController.tolerance);
        telemetry.addData("MOTOR VEL", motor.getVelocity());
        telemetry.addData("CAMERA LOCKED", locked);

        telemetry.addLine("CAMERA SWIVEL - VISION\n");
        telemetry.addData("MOTIF", motif);
        telemetry.addData("TARGET X", x);
        telemetry.addData("TARGET Y", y);
        telemetry.addData("TARGET BEARING (ticks)", tickBearing);
    }
}