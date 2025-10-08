package org.firstinspires.ftc.teamcode.FSL.helper;

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
            Storage.INSTANCE.setColorPurple,
            new ParallelGroup(
                    Storage.INSTANCE.searchForBallColor,
                    Shooter.INSTANCE.fire
            ),
            new Delay(1),
            Shooter.INSTANCE.stop,
            Storage.INSTANCE.setColorNone,
            Storage.INSTANCE.searchForBallColor
    );

    public final Command scoreGreen = new SequentialGroup(
            Storage.INSTANCE.setColorGreen,
            new ParallelGroup(
                    Storage.INSTANCE.searchForBallColor,
                    Shooter.INSTANCE.fire
            ),
            new Delay(1),
            Shooter.INSTANCE.stop,
            Storage.INSTANCE.setColorNone,
            Storage.INSTANCE.searchForBallColor
    );

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
            ));

    public Command manualMotifControl = new InstantCommand(() -> {
        if(CameraSwivel.INSTANCE.motifNumber < 3) CameraSwivel.INSTANCE.motifNumber++;
        else CameraSwivel.INSTANCE.motifNumber = 1;
    });

    public Command telemetryMotif = new LambdaCommand()
            .setUpdate(() -> {
                ActiveOpMode.telemetry().addData("MOTIF", CameraSwivel.INSTANCE.motifNumber);
            }).perpetually();
    public Command auto = new SequentialGroup(
            //BLAH BLAH BLAH FIGURE THIS ALL OUT LATER
            CameraSwivel.INSTANCE.readMotif
    );
}
