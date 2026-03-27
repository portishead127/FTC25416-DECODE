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

    public void manageStorage(){
        switch(storageState){
            case INTAKING:
                storage.intake();
                if(storage.foundColor()) {
                    storage.cycleSlot(true);
                    storageState = StateMachine.StorageStates.ROTATING_WHILE_INTAKING;
                }
                break;
            case ROTATING_WHILE_FLICKING:
                if(!storage.atPosition(true)){
                    break;
                }
                if(/*IGNORE THIS - I HAVEN'T GOT A METHOD YET*/true){
                    storageState = StateMachine.StorageStates.AWAITING_FLICK;
                }
        }
    }
}
