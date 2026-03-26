package org.firstinspires.ftc.teamcode.FSL.helper;

public class StateMachine {
    public enum StorageStates{
        INTAKING,
        ROTATING_WHILE_INTAKING,
        AWAITING_FLICK,
        FLICKING,
        ROTATING_WHILE_FLICKING,
    }
    public enum IntakeStates{
        INTAKING,
        TRANSFERING,
        OFF
    }
    public enum SHOOTER{
        OFF,
        WARMING_UP,
        ON
    }
}
