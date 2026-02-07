package org.firstinspires.ftc.teamcode.FSL.helper.subsystems;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.FSL.helper.configs.MecanumConfig;

public class DriveTrain {
    private static final double MAX_MOTOR_VEL = 2800;
    private final DcMotorEx frontLeft;
    private final DcMotorEx frontRight;
    private final DcMotorEx backRight;
    private final DcMotorEx backLeft;
    private final Telemetry telemetry;
    public DriveTrain(HardwareMap hm, Telemetry telemetry){
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

    public void update(Gamepad gp, boolean isSlow){
        double scalar;
        if(isSlow){
            scalar = MecanumConfig.MECANUM_SLOW_POWER;
        }
        else{
            scalar = MecanumConfig.MECANUM_FULL_POWER;
        }

        double y = -gp.left_stick_y;
        double x = gp.left_stick_x * 1.1;
        double rx = gp.right_stick_x;

        double denominator = Math.max(Math.abs(y) + Math.abs(x) + Math.abs(rx), 1);
        double frontLeftPowerMod = (y + x + rx) / denominator;
        double backLeftPowerMod = (y - x + rx) / denominator;
        double frontRightPowerMod = (y - x - rx) / denominator;
        double backRightPowerMod = (y + x - rx) / denominator;

        frontLeft.setPower(frontLeftPowerMod * scalar);
        backLeft.setPower(backLeftPowerMod * scalar);
        frontRight.setPower(frontRightPowerMod * scalar);
        backRight.setPower(backRightPowerMod * scalar);

        sendTelemetry();
    }

    public void sendTelemetry(){
        telemetry.addLine("DRIVETRAIN\n");
        telemetry.addData("FRONT LEFT POWER", frontLeft.getPower());
        telemetry.addData("FRONT RIGHT POWER", frontRight.getPower());
        telemetry.addData("BACK LEFT POWER", backLeft.getPower());
        telemetry.addData("BACK RIGHT POWER", backRight.getPower());
    }
}
