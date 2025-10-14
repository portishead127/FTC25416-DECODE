package org.firstinspires.ftc.teamcode.FSL.helper;

import org.firstinspires.ftc.teamcode.FSL.helper.colors.Colors;
import org.firstinspires.ftc.teamcode.FSL.helper.subsystems.CameraSwivel;
import org.firstinspires.ftc.teamcode.FSL.helper.subsystems.MecanumSet;
import org.firstinspires.ftc.teamcode.FSL.helper.subsystems.Shooter;
import org.firstinspires.ftc.teamcode.FSL.helper.subsystems.Storage;

import dev.nextftc.core.commands.Command;
import dev.nextftc.core.commands.conditionals.SwitchCommand;
import dev.nextftc.core.commands.delays.Delay;
import dev.nextftc.core.commands.groups.ParallelGroup;
import dev.nextftc.core.commands.groups.SequentialGroup;
import dev.nextftc.core.commands.utility.InstantCommand;
import dev.nextftc.core.commands.utility.LambdaCommand;
import dev.nextftc.core.subsystems.SubsystemGroup;
import dev.nextftc.ftc.ActiveOpMode;

public class Robot extends SubsystemGroup {
    public static final Robot INSTANCE = new Robot();
    private Robot(){
        super(
                Shooter.INSTANCE,
                Storage.INSTANCE,
                MecanumSet.INSTANCE,
                CameraSwivel.INSTANCE
        );
    };

    public final Command scorePurple = new SequentialGroup(
            Storage.INSTANCE.releaseSlot(Storage.INSTANCE.findSlotWithColor(Colors.PURPLE)),
            Shooter.INSTANCE.fire
    ).requires(Shooter.INSTANCE, Storage.INSTANCE);

    public final Command scoreGreen = new SequentialGroup(
            Storage.INSTANCE.releaseSlot(Storage.INSTANCE.findSlotWithColor(Colors.GREEN)),
            Shooter.INSTANCE.fire
    ).requires(Shooter.INSTANCE, Storage.INSTANCE);

    public final Command scoreMotif = new SwitchCommand<>(() -> CameraSwivel.INSTANCE.motifNumber)
            .withCase(1, new SequentialGroup(
                    scorePurple,
                    scorePurple,
                    scoreGreen
            ))
            .withCase(2, new SequentialGroup(
                    scorePurple,
                    scoreGreen,
                    scorePurple
            ))
            .withCase(3, new SequentialGroup(
                    scoreGreen,
                    scorePurple,
                    scorePurple
            ))
            .withDefault(new SequentialGroup(
                    scorePurple,
                    scorePurple,
                    scoreGreen
            ))
            .requires(scoreGreen.getRequirements(), CameraSwivel.INSTANCE);

    public final Command intake = new ParallelGroup(
            Storage.INSTANCE.reload
            //intake
    ).requires(Storage.INSTANCE);
    public Command manualMotifControl = new InstantCommand(() -> {
        if(CameraSwivel.INSTANCE.motifNumber < 3) CameraSwivel.INSTANCE.motifNumber++;
        else CameraSwivel.INSTANCE.motifNumber = 1;
    }).requires(CameraSwivel.INSTANCE);

    public Command telemetryMotif = new LambdaCommand()
            .setUpdate(() -> {
                ActiveOpMode.telemetry().addData("MOTIF", CameraSwivel.INSTANCE.motifNumber);
            }).perpetually();
    public Command auto = new SequentialGroup(
            //BLAH BLAH BLAH FIGURE THIS ALL OUT LATER
            CameraSwivel.INSTANCE.readMotif
    );
}
