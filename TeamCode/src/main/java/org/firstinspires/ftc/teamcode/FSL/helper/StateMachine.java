package org.firstinspires.ftc.teamcode.FSL.helper;

public class StateMachine {
    public enum StorageStates{
        INTAKING,
        ROTATING,
        AWAITING_FLICK,
        FLICKING,
        FLICKING_ALL,
        ROTATING_ALL
    }
    public enum IntakeStates{
        INTAKING,
        TRANSFERING,
        OFF
    }
    public enum ShooterStates{
        OFF,
        WARMING_UP,
        ON
    }
}
