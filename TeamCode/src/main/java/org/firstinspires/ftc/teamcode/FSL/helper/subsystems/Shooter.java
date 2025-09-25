package org.firstinspires.ftc.teamcode.FSL.helper.subsystems;


import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.Limelight3A;

import org.firstinspires.ftc.robotcore.external.navigation.Pose3D;

import dev.nextftc.core.commands.Command;
import dev.nextftc.core.commands.groups.ParallelGroup;
import dev.nextftc.core.commands.utility.LambdaCommand;
import dev.nextftc.core.commands.utility.NullCommand;
import dev.nextftc.core.subsystems.Subsystem;
import dev.nextftc.ftc.ActiveOpMode;
import dev.nextftc.hardware.controllable.MotorGroup;
import dev.nextftc.hardware.impl.MotorEx;
import dev.nextftc.hardware.impl.ServoEx;
import dev.nextftc.hardware.positionable.SetPosition;
import dev.nextftc.hardware.positionable.SetPositions;
import dev.nextftc.hardware.powerable.SetPower;

public class Shooter implements Subsystem {
    public static Shooter INSTANCE = new Shooter();
    private Shooter(){}
    private Limelight3A limelight3A;
    private final ServoEx swivelServo = new ServoEx("SS");
    private final MotorGroup shooterMotors = new MotorGroup(
            new MotorEx("SM1")
                    .reversed()
                    .brakeMode(),
            new MotorEx("SM2")
                    .reversed()
                    .brakeMode()
    );

    private boolean lastScannedRight = false;
    
    //TODO tune these values
    private static final double scanDx = 0.1;
    private static final double focusDx = 0.01;
    private static final int centralTolerance = 3; //Determines how lenient the center of the camera is

    public Command limelightDetection = new LambdaCommand()
            .setStart(() -> {
                limelight3A = ActiveOpMode.hardwareMap().get(com.qualcomm.hardware.limelightvision.Limelight3A.class, "limelight");
                limelight3A.pipelineSwitch(0);
                limelight3A.start();
            })
            .setUpdate(() -> {
                ActiveOpMode.telemetry().addLine("LimeLight3A\n");
                LLResult result = limelight3A.getLatestResult();
                if (result != null) {
                    if (result.isValid()) {
                        Pose3D botpose = result.getBotpose();
                        ActiveOpMode.telemetry().addData("tx", result.getTx());
                        ActiveOpMode.telemetry().addData("ty", result.getTy());
                        ActiveOpMode.telemetry().addData("Botpose", botpose.toString());

                        evaluateTx(result.getTx()).schedule();
                    }
                }else{
                    determineScan().schedule();
                }
            });
    public Command evaluateTx(double tx){
        if(tx < -centralTolerance) return focusRight;
        else if (tx > centralTolerance) return focusLeft;
        else return new NullCommand();
    }

    public Command determineScan(){
        if(swivelServo.getPosition() == 1){
            lastScannedRight = true;
            return scanRight;
        }
        else if(swivelServo.getPosition() == 0){
            lastScannedRight = false;
            return scanLeft;
        }

        if(lastScannedRight) return scanLeft;
        return scanRight;
    }
    public Command focusLeft = new SetPosition(swivelServo, swivelServo.getPosition() - focusDx);
    public Command focusRight = new SetPosition(swivelServo, swivelServo.getPosition() + focusDx);
    public Command scanLeft = new SetPosition(swivelServo, swivelServo.getPosition() - scanDx);
    public Command scanRight = new SetPosition(swivelServo, swivelServo.getPosition() + scanDx);

    public Command fire = new SetPower(shooterMotors,1);
    public Command fireHalf = new SetPower(shooterMotors, 0.5);
    public Command stop = new SetPower(shooterMotors, 0);
}