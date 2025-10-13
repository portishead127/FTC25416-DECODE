package org.firstinspires.ftc.teamcode.FSL.helper.subsystems;

import com.pedropathing.control.PIDFCoefficients;
import com.pedropathing.control.PIDFController;
import com.qualcomm.robotcore.hardware.ColorRangeSensor;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.Gamepad;

import org.firstinspires.ftc.teamcode.FSL.helper.colors.ColorMethods;
import org.firstinspires.ftc.teamcode.FSL.helper.colors.Colors;

import java.util.LinkedList;

import dev.nextftc.control.ControlSystem;
import dev.nextftc.control.KineticState;
import dev.nextftc.control.feedback.PIDCoefficients;
import dev.nextftc.control.feedback.PIDController;
import dev.nextftc.core.commands.Command;
import dev.nextftc.core.commands.delays.Delay;
import dev.nextftc.core.commands.groups.ParallelGroup;
import dev.nextftc.core.commands.groups.SequentialGroup;
import dev.nextftc.core.commands.utility.InstantCommand;
import dev.nextftc.core.commands.utility.LambdaCommand;
import dev.nextftc.core.subsystems.Subsystem;
import dev.nextftc.ftc.ActiveOpMode;
import dev.nextftc.hardware.impl.CRServoEx;
import dev.nextftc.hardware.impl.MotorEx;
import dev.nextftc.hardware.positionable.SetPosition;
import dev.nextftc.hardware.powerable.SetPower;

public class Storage implements Subsystem {
    private Colors searchColor = Colors.NONE; //default
    private ColorSensor colorSensor;
    private final MotorEx spinMotor = new MotorEx("SPM");
    private final ControlSystem motorController = ControlSystem.builder()
            .posPid(0.5)
            .build();
    private final CRServoEx flickServo = new CRServoEx("FLS");
    private final double encoderTicksPerThirdRev = 46.7;
    private Storage(){}
    public static Storage INSTANCE = new Storage();

    @Override
    public void initialize() {
        colorSensor = ActiveOpMode.hardwareMap().get(ColorRangeSensor.class, "CS");
        colorSensor.enableLed(true);
        setColorNone.schedule();
    }
    public final Command reload = new SequentialGroup(
            new SetPower(spinMotor, motorController.calculate(new KineticState(spinMotor.getCurrentPosition(), 0, 0)))
    );

    public final Command stop = new SetPower(spinMotor, 0).requires(this);
    public final Command flickBall = new SequentialGroup(
        new SetPower(flickServo, 1),
        new Delay(0.1),
        new SetPower(flickServo, 0)
    ).requires(this);
    public final Command searchForBallColor = new LambdaCommand()
            .setIsDone(() -> ColorMethods.fromSensor(colorSensor) == searchColor)
            .setUpdate(reload)
            .setStop((interrupted) -> new SequentialGroup(
                stop,
                flickBall
            )).requires();

    public final Command setColorNone = new InstantCommand(() -> {
        searchColor = Colors.NONE;
        ActiveOpMode.gamepad2().setLedColor(0,0,0, Gamepad.LED_DURATION_CONTINUOUS);
    }).requires(this);
    public final Command setColorGreen = new InstantCommand(() -> {
        searchColor = Colors.GREEN;
        ActiveOpMode.gamepad2().setLedColor(0,255,0, Gamepad.LED_DURATION_CONTINUOUS);
    }).requires(this);
    public final Command setColorPurple = new InstantCommand(() -> {
        searchColor = Colors.PURPLE;
        ActiveOpMode.gamepad2().setLedColor(255,0,255, Gamepad.LED_DURATION_CONTINUOUS);
    }).requires(this);
}
