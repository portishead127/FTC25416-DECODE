package org.firstinspires.ftc.teamcode.FSL.helper.subsystems;

import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.Limelight3A;

import org.firstinspires.ftc.robotcore.external.navigation.Pose3D;
import org.firstinspires.ftc.teamcode.FSL.helper.configs.Limelight3ADetectionConfig;

import dev.nextftc.core.commands.Command;
import dev.nextftc.core.commands.utility.InstantCommand;
import dev.nextftc.core.commands.utility.LambdaCommand;
import dev.nextftc.core.commands.utility.NullCommand;
import dev.nextftc.core.subsystems.Subsystem;
import dev.nextftc.ftc.ActiveOpMode;
import dev.nextftc.hardware.impl.ServoEx;
import dev.nextftc.hardware.positionable.SetPosition;

public class CameraSwivel implements Subsystem {
    public static CameraSwivel INSTANCE = new CameraSwivel();
    private Limelight3A limelight3A;
    private final ServoEx swivelServo = new ServoEx("SS");
    private boolean lastScannedRight = false;

    public Command initLimeLight3A = new InstantCommand(() -> {
        limelight3A = ActiveOpMode.hardwareMap().get(com.qualcomm.hardware.limelightvision.Limelight3A.class, "limelight");
    });
    public Command focusOnAprilTag = new LambdaCommand()
            .setStart(() -> {
                limelight3A.pipelineSwitch(0);
                limelight3A.start();
            })
            .setUpdate(() -> {
                ActiveOpMode.telemetry().addLine("LimeLight3A\n");
                LLResult result = limelight3A.getLatestResult();
                if (result != null) {
                    if (result.isValid()) {
                        //CAREFUL! THIS WILL ONLY RECEIVE ONE APRIL TAG'S DATA SO MAY REQUIRE ID CHECKER!
                        Pose3D botPose = result.getBotpose();
                        ActiveOpMode.telemetry().addData("tx", result.getTx());
                        ActiveOpMode.telemetry().addData("ty", result.getTy());
                        ActiveOpMode.telemetry().addData("Bot Pose", botPose.toString());

                        evaluateTx(result.getTx()).schedule();
                    }
                }else{
                    determineScan().schedule();
                }
            });
    public Command evaluateTx(double tx){
        if(tx < -Limelight3ADetectionConfig.CENTRALTOLERANCE) return focusRight;
        else if (tx > Limelight3ADetectionConfig.CENTRALTOLERANCE) return focusLeft;
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
    public Command focusLeft = new SetPosition(swivelServo, swivelServo.getPosition() - Limelight3ADetectionConfig.FOCUSDX);
    public Command focusRight = new SetPosition(swivelServo, swivelServo.getPosition() + Limelight3ADetectionConfig.FOCUSDX);
    public Command scanLeft = new SetPosition(swivelServo, swivelServo.getPosition() - Limelight3ADetectionConfig.SCANDX);
    public Command scanRight = new SetPosition(swivelServo, swivelServo.getPosition() + Limelight3ADetectionConfig.SCANDX);

}
