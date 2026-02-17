package org.firstinspires.ftc.teamcode.FSL.helper.subsystems;

import android.annotation.SuppressLint;

import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.teamcode.FSL.helper.scoring.Motif;
import org.firstinspires.ftc.teamcode.FSL.helper.control.PIDController;
import org.firstinspires.ftc.teamcode.FSL.helper.configs.CameraSwivelConfig;
import org.firstinspires.ftc.vision.VisionPortal;
import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;
import org.firstinspires.ftc.vision.apriltag.AprilTagProcessor;

import java.util.List;

public class CameraSwivel {
    private final Telemetry telemetry;
    private final VisionPortal visionPortal;
    private final AprilTagProcessor aprilTagProcessor;
    private DcMotorEx motor;
    private PIDController pidController;
    public Motif motif;
    private final int targetID;
    public double x;
    public double y;
    public double range;
    public double camBearing;
    private double tickBearing;

    public boolean locked = false;
    public CameraSwivel(HardwareMap hm, Telemetry telemetry, boolean isBlue, boolean isGreedyAuto) {
//        motor = hm.get(DcMotorEx.class, "CSM");
//        motor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
//        motor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
//        motor.setDirection(DcMotorSimple.Direction.REVERSE); //SPINNING ACW NEEDS TO INCREASE TICKS
//        motor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
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

        pidController = new PIDController(CameraSwivelConfig.KP, CameraSwivelConfig.KI, CameraSwivelConfig.KD, CameraSwivelConfig.CENTRALTOLERANCE);

        camBearing = 0;
        tickBearing = 0;
        x = 0;
        y = 0;
        range = 0;
        if(isBlue){ targetID = 23;}
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

        locked = false;
        for (AprilTagDetection detection : currentDetections) {
            if(detection.id == targetID){
                locked = true;
                camBearing = Math.PI/2 + detection.ftcPose.bearing;
                x = detection.ftcPose.x;
                y = detection.ftcPose.y;
                break;
            }
        }
        camBearing = 0;
        x = 0;
        y = 0;
    }
    public void honeOnAprilTag() {
        List<AprilTagDetection> currentDetections = aprilTagProcessor.getDetections();

        for (AprilTagDetection detection : currentDetections) {
            if (detection.id == targetID) {
                locked = true;
                tickBearing = detection.ftcPose.bearing;
                range = detection.ftcPose.range;
            }
            else{
                locked = false;
            }
        }
        setPIDTarget(tickBearing, true);
    }

    public void setPIDTarget(double bearingInTicks, boolean append){
        double valToAssign = Math.max(-CameraSwivelConfig.MAX_OFFSET,Math.min(bearingInTicks, CameraSwivelConfig.MAX_OFFSET));
        pidController.setTarget(valToAssign, append);
    }

    public void update(double lateralOverride){
        if(Math.abs(lateralOverride) < 0.2){
            focusOnAprilTag();
            motor.setVelocity(CameraSwivelConfig.MAX_VEL * pidController.calculateScalar(motor.getCurrentPosition()));
        }
        else{
            jog(-lateralOverride);
            pidController.reset();
        }
        sendTelemetry();
    }

    public void update(){
        focusOnAprilTag();
        motor.setVelocity(CameraSwivelConfig.MAX_VEL * pidController.calculateScalar(motor.getCurrentPosition()));
        sendTelemetry();
    }

    public void simpleUpdate(double lateralOverride){
        if(Math.abs(lateralOverride) < 0.2){
            honeOnAprilTag();
            motor.setVelocity(CameraSwivelConfig.MAX_VEL * pidController.calculateScalar(motor.getCurrentPosition()));
        }
        else{
            jog(-lateralOverride);
            pidController.reset();
        }
        sendTelemetry();
    }
    public void simpleUpdate(){
        honeOnAprilTag();
        motor.setVelocity(CameraSwivelConfig.MAX_VEL * pidController.calculateScalar(motor.getCurrentPosition()));
        sendTelemetry();
    }

    public void stop() {
        motor.setVelocity(0);
    }
    public void jog(double scalar) {
        motor.setVelocity(CameraSwivelConfig.MAX_VEL * scalar);
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
//        telemetry.addLine("CAMERA SWIVEL - HARDWARE\n");
//        telemetry.addData("MOTOR POS", motor.getCurrentPosition());
//        telemetry.addData("MOTOR TARGET", pidController.target);
//        telemetry.addData("TARGET TOLERANCE", pidController.tolerance);
//        telemetry.addData("MOTOR VEL", motor.getVelocity());
//        telemetry.addData("CAMERA LOCKED", locked);

        telemetry.addLine("CAMERA SWIVEL - VISION\n");
        telemetry.addData("LOCKED", locked);
        telemetry.addData("MOTIF", motif);
        telemetry.addData("ROBOT X", x);
        telemetry.addData("ROBOT Y", y);
        telemetry.addData("TARGET BEARING (ticks)", tickBearing);
    }
}