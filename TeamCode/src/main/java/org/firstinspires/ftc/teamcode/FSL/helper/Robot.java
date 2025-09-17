package org.firstinspires.ftc.teamcode.FSL.helper;

import org.firstinspires.ftc.teamcode.FSL.helper.subsystems.MecanumSet;
import org.firstinspires.ftc.teamcode.FSL.helper.subsystems.Shooter;

import dev.nextftc.core.subsystems.SubsystemGroup;

public class Robot extends SubsystemGroup {
    public static final Robot INSTANCE = new Robot();
    private Robot(){
        super(
                Shooter.INSTANCE,
                MecanumSet.INSTANCE
        );
    };

//    private final Command win;

}
