package org.firstinspires.ftc.teamcode.FSL.helper;

import org.firstinspires.ftc.teamcode.FSL.helper.subsystems.CameraSwivel;
import org.firstinspires.ftc.teamcode.FSL.helper.subsystems.MecanumSet;
import org.firstinspires.ftc.teamcode.FSL.helper.subsystems.Shooter;
import org.firstinspires.ftc.teamcode.FSL.helper.subsystems.Storage;

import dev.nextftc.core.commands.Command;
import dev.nextftc.core.commands.conditionals.SwitchCommand;
import dev.nextftc.core.commands.groups.SequentialGroup;
import dev.nextftc.core.subsystems.SubsystemGroup;

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

    private final Command scorePurple = new SequentialGroup(
            Storage.INSTANCE.setColorPurple,
            Storage.INSTANCE.searchForBallColor,
            Shooter.INSTANCE.fire
    );

    private final Command scoreGreen = new SequentialGroup(
            Storage.INSTANCE.setColorGreen,
            Storage.INSTANCE.searchForBallColor,
            Shooter.INSTANCE.fire
    );

    private final Command scoreMotif = new SwitchCommand<>(() -> CameraSwivel.INSTANCE.motifNumber)
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
}
