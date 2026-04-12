package org.firstinspires.ftc.teamcode.FSL.subsystems;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.FSL.helper.configs.Configuration;
import org.firstinspires.ftc.teamcode.FSL.helper.configs.MecanumConfig;

public class DriveTrain {
    private final DcMotorEx frontLeft;
    private final DcMotorEx frontRight;
    private final DcMotorEx backRight;
    private final DcMotorEx backLeft;
    private final Telemetry telemetry;
    private final DriveCoefficients driveCoefficients;
    private double scalar;
    private static class DriveCoefficients{
        public double y;
        public double x;
        public double rx;
        public void setDriveCoefficients(double x, double y, double rx){
            this.x = x;
            this.y = y;
            this.rx = rx;
        }
    }
    public DriveTrain(HardwareMap hm, Telemetry telemetry){
        frontLeft = hm.get(DcMotorEx.class, "FLW");
        frontLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        frontLeft.setDirection(DcMotorSimple.Direction.FORWARD);

        frontRight = hm.get(DcMotorEx.class, "FRW");
        frontRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        frontRight.setDirection(DcMotorSimple.Direction.FORWARD);

        backLeft = hm.get(DcMotorEx.class, "BLW");
        backLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backLeft.setDirection(DcMotorSimple.Direction.REVERSE);

        backRight = hm.get(DcMotorEx.class, "BRW");
        backRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backRight.setDirection(DcMotorSimple.Direction.FORWARD); //idek

        driveCoefficients = new DriveCoefficients();
        scalar = Configuration.MecanumConfig.MECANUM_MED_POWER;

        this.telemetry = telemetry;
    }

    public void update(){
        double denominator = Math.max(Math.abs(driveCoefficients.y) + Math.abs(driveCoefficients.x) + Math.abs(driveCoefficients.rx), 1);
        double frontLeftPowerMod = (driveCoefficients.y + driveCoefficients.x + driveCoefficients.rx) / denominator;
        double backLeftPowerMod = (driveCoefficients.y - driveCoefficients.x + driveCoefficients.rx) / denominator;
        double frontRightPowerMod = (driveCoefficients.y - driveCoefficients.x - driveCoefficients.rx) / denominator;
        double backRightPowerMod = (driveCoefficients.y + driveCoefficients.x - driveCoefficients.rx) / denominator;

        frontLeft.setPower(frontLeftPowerMod * scalar);
        backLeft.setPower(backLeftPowerMod * scalar);
        frontRight.setPower(frontRightPowerMod * scalar);
        backRight.setPower(backRightPowerMod * scalar);
    }
    public void setDriveCoefficients(double x, double y, double rx){
        driveCoefficients.setDriveCoefficients(x,y,rx);
    }
    public void setSlow(boolean slow){
        if(slow) scalar = Configuration.MecanumConfig.MECANUM_SLOW_POWER;
    }
    public void setFast(boolean fast){
        if(fast) scalar = Configuration.MecanumConfig.MECANUM_FULL_POWER;
    }
    public void setMed(boolean medium){
        if(medium) scalar = Configuration.MecanumConfig.MECANUM_MED_POWER;
    }
    public void auto(){
        ElapsedTime timer = new ElapsedTime();
        timer.reset();
        while(timer.milliseconds() < 1000){
            frontLeft.setPower(0.4);
            backLeft.setPower(0.4);
            backRight.setPower(0.4);
            frontRight.setPower(0.4);
        }
        frontLeft.setPower(0);
        backLeft.setPower(0);
        backRight.setPower(0);
        frontRight.setPower(0);
    }

    public void sendTelemetry(){
        telemetry.addLine("DRIVETRAIN\n");
        telemetry.addData("FRONT LEFT POWER", frontLeft.getPower());
        telemetry.addData("FRONT RIGHT POWER", frontRight.getPower());
        telemetry.addData("BACK LEFT POWER", backLeft.getPower());
        telemetry.addData("BACK RIGHT POWER", backRight.getPower());
    }
}
