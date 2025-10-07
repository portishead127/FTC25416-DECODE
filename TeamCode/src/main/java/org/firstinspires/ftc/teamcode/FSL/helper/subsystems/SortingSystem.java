package org.firstinspires.ftc.teamcode.FSL.helper.subsystems;

import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.internal.hardware.android.GpioPin;
import org.firstinspires.ftc.teamcode.FSL.helper.ColorMethods;

import dev.nextftc.core.commands.Command;
import dev.nextftc.core.commands.delays.WaitUntil;
import dev.nextftc.core.commands.groups.CommandGroup;
import dev.nextftc.core.commands.groups.ParallelGroup;
import dev.nextftc.core.commands.groups.ParallelRaceGroup;
import dev.nextftc.core.commands.groups.SequentialGroup;
import dev.nextftc.core.commands.utility.InstantCommand;
import dev.nextftc.core.commands.utility.LambdaCommand;
import dev.nextftc.core.commands.utility.NullCommand;
import dev.nextftc.core.subsystems.Subsystem;
import dev.nextftc.ftc.ActiveOpMode;
import dev.nextftc.hardware.controllable.RunToPosition;
import dev.nextftc.hardware.controllable.RunToVelocity;
import dev.nextftc.hardware.impl.CRServoEx;
import dev.nextftc.hardware.impl.ServoEx;
import dev.nextftc.hardware.powerable.SetPower;

public class SortingSystem implements Subsystem {
    private ColorMethods.Colors searchColor = ColorMethods.Colors.NONE; //default
    public ColorSensor colorSensor;
    public final CRServoEx spinServo = new CRServoEx("SPS");
    public final CRServoEx flickServo = new CRServoEx("FLS");
    private SortingSystem(){
        initColourSensor.schedule();
    }
    public static SortingSystem INSTANCE = new SortingSystem();
    public Command initColourSensor = new InstantCommand(() -> {
       colorSensor = ActiveOpMode.hardwareMap().get(ColorSensor.class, "CS");
       colorSensor.enableLed(true);
    });
    public Command searchForBallColor = new LambdaCommand()
            .setIsDone(() -> ColorMethods.fromSensor(colorSensor) == searchColor)
            .setUpdate(() -> {
                new SetPower(spinServo, 0.7);
            })
            .setStop((interrupted) -> new SetPower(flickServo, 1));

    public Command setColorNone = new InstantCommand(() -> searchColor = ColorMethods.Colors.NONE);
    public Command setColorGreen = new InstantCommand(() -> searchColor = ColorMethods.Colors.GREEN);
    public Command setColorPurple = new InstantCommand(() -> searchColor = ColorMethods.Colors.PURPLE);
    public Command playMacro = new SequentialGroup(setColorPurple, searchForBallColor, setColorPurple, searchForBallColor, setColorGreen, searchForBallColor, setColorNone);
}
