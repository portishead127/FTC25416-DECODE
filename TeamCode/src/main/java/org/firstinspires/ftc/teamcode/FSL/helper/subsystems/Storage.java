package org.firstinspires.ftc.teamcode.FSL.helper.subsystems;

import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.Gamepad;

import org.firstinspires.ftc.teamcode.FSL.helper.colors.ColorMethods;
import org.firstinspires.ftc.teamcode.FSL.helper.colors.Colors;

import dev.nextftc.core.commands.Command;
import dev.nextftc.core.commands.delays.Delay;
import dev.nextftc.core.commands.groups.SequentialGroup;
import dev.nextftc.core.commands.utility.InstantCommand;
import dev.nextftc.core.commands.utility.LambdaCommand;
import dev.nextftc.core.subsystems.Subsystem;
import dev.nextftc.ftc.ActiveOpMode;
import dev.nextftc.hardware.impl.CRServoEx;
import dev.nextftc.hardware.powerable.SetPower;
import kotlin.Unit;

public class Storage implements Subsystem {
    private Colors searchColor = Colors.NONE; //default
    public ColorSensor colorSensor;
    public final CRServoEx spinServo = new CRServoEx("SPS");
    public final CRServoEx flickServo = new CRServoEx("FLS");
    private Storage(){}
    public static Storage INSTANCE = new Storage();

    @Override
    public void initialize() {
        colorSensor = ActiveOpMode.hardwareMap().get(ColorSensor.class, "CS");
        colorSensor.enableLed(true);
        setColorNone.schedule();
    }
    public final Command spin = new SetPower(spinServo, 0.2);
    public final Command stop = new SetPower(spinServo, 0);
    public final Command flickBall = new SequentialGroup(
        new SetPower(flickServo, 1),
        new Delay(100),
        new SetPower(flickServo, 0)
    );
    public final Command searchForBallColor = new LambdaCommand()
            .setIsDone(() -> ColorMethods.fromSensor(colorSensor) == searchColor)
            .setUpdate(spin)
            .setStop((interrupted) -> {
                stop.schedule();
                flickBall.schedule();
            });
    public final Command setColorNone = new InstantCommand(() -> {
        searchColor = Colors.NONE;
        ActiveOpMode.gamepad2().setLedColor(0,0,0, Gamepad.LED_DURATION_CONTINUOUS);
    });
    public final Command setColorGreen = new InstantCommand(() -> {
        searchColor = Colors.GREEN;
        ActiveOpMode.gamepad2().setLedColor(0,255,0, Gamepad.LED_DURATION_CONTINUOUS);
    });
    public final Command setColorPurple = new InstantCommand(() -> {
        searchColor = Colors.PURPLE;
        ActiveOpMode.gamepad2().setLedColor(255,0,255, Gamepad.LED_DURATION_CONTINUOUS);
    });
}
