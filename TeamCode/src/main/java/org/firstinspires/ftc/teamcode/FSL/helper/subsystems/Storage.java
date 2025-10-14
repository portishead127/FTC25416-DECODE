package org.firstinspires.ftc.teamcode.FSL.helper.subsystems;

import com.qualcomm.robotcore.hardware.ColorRangeSensor;
import com.qualcomm.robotcore.hardware.ColorSensor;

import org.firstinspires.ftc.teamcode.FSL.helper.colors.ColorMethods;
import org.firstinspires.ftc.teamcode.FSL.helper.colors.Colors;

import java.util.HashMap;

import dev.nextftc.control.ControlSystem;
import dev.nextftc.control.KineticState;
import dev.nextftc.core.commands.Command;
import dev.nextftc.core.commands.delays.Delay;
import dev.nextftc.core.commands.groups.SequentialGroup;
import dev.nextftc.core.commands.utility.InstantCommand;
import dev.nextftc.core.commands.utility.LambdaCommand;
import dev.nextftc.core.commands.utility.NullCommand;
import dev.nextftc.core.subsystems.Subsystem;
import dev.nextftc.ftc.ActiveOpMode;
import dev.nextftc.hardware.impl.CRServoEx;
import dev.nextftc.hardware.impl.MotorEx;
import dev.nextftc.hardware.powerable.SetPower;

public class Storage implements Subsystem {
    public final HashMap<Integer, Colors> bankedArties = new HashMap<>(3);
    private ColorSensor colorSensor;
    private final MotorEx spinMotor = new MotorEx("SPM");
    private final ControlSystem motorController = ControlSystem.builder()
            .posPid(0.5)
            .build();
    private final CRServoEx flickServo = new CRServoEx("FLS");

    private Storage(){}
    public static Storage INSTANCE = new Storage();

    @Override
    public void initialize() {
        colorSensor = ActiveOpMode.hardwareMap().get(ColorRangeSensor.class, "CS");
        colorSensor.enableLed(true);
        bankedArties.put(0, Colors.NONE);
        bankedArties.put(1, Colors.NONE);
        bankedArties.put(2, Colors.NONE);
    }
    public final Command stop = new SetPower(spinMotor, 0).requires(this);
    public Command runToSlot(int slot){
        if(slot == -1) return new NullCommand().requires(this);
        double encoderTicksPerThirdRev = 46.7;
        KineticState destination = new KineticState(slot * encoderTicksPerThirdRev);
        return new LambdaCommand()
            .setStart(() -> motorController.setGoal(destination))
            .setIsDone(() -> motorController.isWithinTolerance(new KineticState(2)))
            .setUpdate(new SetPower(spinMotor, motorController.calculate(spinMotor.getState()))
        ).requires(this);
    }
    public final Command reload = new SequentialGroup(
            updateArtySlot(0),
            updateArtySlot(1),
            updateArtySlot(2),
            stop,
            new InstantCommand(spinMotor::zero).requires(this)
    ).requires(this);

    public final Command flickBall = new SequentialGroup(
        new SetPower(flickServo, 1),
        new Delay(0.1),
        new SetPower(flickServo, 0)
    ).requires(this);

    public final Command updateArtySlot(int slot){
        return new SequentialGroup(
                runToSlot(slot),
                new InstantCommand(() -> {
                    bankedArties.replace(slot, ColorMethods.fromSensor(colorSensor));
                }).requires(this)
        ).requires(this);
    }
    public final int findSlotWithColor(Colors color){
        int slotWithAColor = -1;
        for(int i = 0; i < bankedArties.size(); i++){
            if(bankedArties.get(i) == color) return i;
            else if(bankedArties.get(i) != Colors.NONE) slotWithAColor = 1;
        }
        return slotWithAColor;
    }
    public final Command releaseSlot(int slot){
        return new SequentialGroup(
                runToSlot(slot),
                flickBall
        ).requires(this);
    }
}
