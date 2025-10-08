package org.firstinspires.ftc.teamcode.FSL.helper.subsystems;

import com.qualcomm.robotcore.hardware.ColorSensor;

import org.firstinspires.ftc.teamcode.FSL.helper.colors.ColorMethods;
import org.firstinspires.ftc.teamcode.FSL.helper.colors.Colors;

import dev.nextftc.core.commands.Command;
import dev.nextftc.core.commands.groups.SequentialGroup;
import dev.nextftc.core.commands.utility.InstantCommand;
import dev.nextftc.core.commands.utility.LambdaCommand;
import dev.nextftc.core.subsystems.Subsystem;
import dev.nextftc.ftc.ActiveOpMode;
import dev.nextftc.hardware.impl.CRServoEx;
import dev.nextftc.hardware.powerable.SetPower;

public class SortingSystem implements Subsystem {
    private Colors searchColor = Colors.NONE; //default
    public ColorSensor colorSensor;
    public final CRServoEx spinServo = new CRServoEx("SPS");
    public final CRServoEx flickServo = new CRServoEx("FLS");
    private SortingSystem(){
        initColourSensor.schedule();
    }
    public static SortingSystem INSTANCE = new SortingSystem();
    public final Command initColourSensor = new InstantCommand(() -> {
       colorSensor = ActiveOpMode.hardwareMap().get(ColorSensor.class, "CS");
       colorSensor.enableLed(true);
    });
    public final Command searchForBallColor = new LambdaCommand()
            .setIsDone(() -> ColorMethods.fromSensor(colorSensor) == searchColor)
            .setUpdate(() -> {
                new SetPower(spinServo, 0.7);
            })
            .setStop((interrupted) -> new SetPower(flickServo, 1));

    public final Command setColorNone = new InstantCommand(() -> searchColor = Colors.NONE);
    public final Command setColorGreen = new InstantCommand(() -> searchColor = Colors.GREEN);
    public final Command setColorPurple = new InstantCommand(() -> searchColor = Colors.PURPLE);
}
