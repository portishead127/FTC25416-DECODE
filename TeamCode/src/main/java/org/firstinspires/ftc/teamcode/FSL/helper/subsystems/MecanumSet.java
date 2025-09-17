package org.firstinspires.ftc.teamcode.FSL.helper.subsystems;

import androidx.annotation.NonNull;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Gamepad;

import dev.nextftc.core.commands.Command;
import dev.nextftc.core.commands.utility.LambdaCommand;
import dev.nextftc.core.subsystems.Subsystem;
import dev.nextftc.core.subsystems.SubsystemGroup;
import dev.nextftc.ftc.Gamepads;
import dev.nextftc.hardware.driving.DriverControlledCommand;
import dev.nextftc.hardware.driving.HolonomicMode;
import dev.nextftc.hardware.driving.MecanumDriverControlled;
import dev.nextftc.hardware.impl.MotorEx;

public class MecanumSet implements Subsystem {
    public final MotorEx frontLeftMotor = new MotorEx("FLW")
            .brakeMode();
    public final MotorEx frontRightMotor = new MotorEx("FRW")
            .brakeMode()
            .reversed();
    public final MotorEx backLeftMotor = new MotorEx("BLW")
            .brakeMode();
    public final MotorEx backRightMotor = new MotorEx("BRW")
            .brakeMode()
            .reversed();
    private MecanumSet(){}
    public static final MecanumSet INSTANCE = new MecanumSet();
    public final MecanumDriverControlled driverController = new MecanumDriverControlled(
            frontLeftMotor,
            frontRightMotor,
            backLeftMotor,
            backRightMotor,
            Gamepads.gamepad1().leftStickY(),
            Gamepads.gamepad1().leftStickX(),
            Gamepads.gamepad1().rightStickX()
    );

    public void setSlow(){driverController.setScalar(0.4);}
    public void setMedium(){driverController.setScalar(0.7);}
    public void setFast(){driverController.setScalar(1);}
}
