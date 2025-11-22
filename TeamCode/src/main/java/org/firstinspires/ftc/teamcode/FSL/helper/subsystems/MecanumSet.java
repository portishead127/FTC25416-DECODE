package org.firstinspires.ftc.teamcode.FSL.helper.subsystems;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class MecanumSet {
    private static final double MAX_MOTOR_VEL = 2800;
    private final DcMotorEx frontLeft;
    private final DcMotorEx frontRight;
    private final DcMotorEx backRight;
    private final DcMotorEx backLeft;
    private final Telemetry telemetry;
    public MecanumSet(HardwareMap hm, Telemetry telemetry){
        frontLeft = hm.get(DcMotorEx.class, "FLW");
        frontLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        frontRight = hm.get(DcMotorEx.class, "FRW");
        frontRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        frontRight.setDirection(DcMotorSimple.Direction.REVERSE);

        backLeft = hm.get(DcMotorEx.class, "BLW");
        backLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        backRight = hm.get(DcMotorEx.class, "BRW");
        backRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backRight.setDirection(DcMotorSimple.Direction.REVERSE);

        this.telemetry = telemetry;
    }

    public void drive(Gamepad gp, double scalar){
        scalar = Math.max(0, Math.min(1, scalar));
        double velocity = scalar * frontLeft.getMotorType().getAchieveableMaxTicksPerSecond();

        double y = -gp.left_stick_y;
        double x = gp.left_stick_x * 1.1;
        double rx = gp.right_stick_x;

        double denominator = Math.max(Math.abs(y) + Math.abs(x) + Math.abs(rx), 1);
        double frontLeftPowerMod = (y + x + rx) / denominator;
        double backLeftPowerMod = (y - x + rx) / denominator;
        double frontRightPowerMod = (y - x - rx) / denominator;
        double backRightPowerMod = (y + x - rx) / denominator;

        frontLeft.setVelocity(frontLeftPowerMod * velocity);
        backLeft.setVelocity(backLeftPowerMod * velocity);
        frontRight.setVelocity(frontRightPowerMod * velocity);
        backRight.setVelocity(backRightPowerMod * velocity);
    }

    public void sendTelemetry(){
        telemetry.addLine("DRIVETRAIN\n");
        telemetry.addData("FRONT LEFT VEL", frontLeft.getVelocity());
        telemetry.addData("FRONT LEFT VEL", frontLeft.getVelocity());
        telemetry.addData("FRONT LEFT VEL", frontLeft.getVelocity());
        telemetry.addData("FRONT LEFT VEL", frontLeft.getVelocity());
    }
}
