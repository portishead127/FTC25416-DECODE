package org.firstinspires.ftc.teamcode.FSL;

import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.FSL.helper.StateMachine;
import org.firstinspires.ftc.teamcode.FSL.subsystems.ServoStorage;

public class NationalsRobot {
    ServoStorage storage;
    StateMachine.StorageStates storageState;
    public NationalsRobot(HardwareMap hm, Telemetry telemetry){
        storage = new ServoStorage(hm,telemetry);

        storageState = StateMachine.StorageStates.INTAKING;
    }
}
