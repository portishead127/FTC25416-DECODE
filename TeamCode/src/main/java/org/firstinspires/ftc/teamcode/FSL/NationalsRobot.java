package org.firstinspires.ftc.teamcode.FSL;

import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.FSL.helper.colors.Color;
import org.firstinspires.ftc.teamcode.FSL.helper.scoring.Motif;
import org.firstinspires.ftc.teamcode.FSL.helper.scoring.Scoring;
import org.firstinspires.ftc.teamcode.FSL.subsystems.DriveTrain;
import org.firstinspires.ftc.teamcode.FSL.subsystems.NationalsIntake;
import org.firstinspires.ftc.teamcode.FSL.subsystems.NationalsShooter;
import org.firstinspires.ftc.teamcode.FSL.subsystems.ServoStorage;

public class NationalsRobot {
    private final ServoStorage storage;
    private final NationalsShooter shooter;
    private final NationalsIntake intake;
    private final DriveTrain driveTrain;
    private boolean intakeRequested;

    public NationalsRobot(HardwareMap hm, Telemetry telemetry, boolean empty) {
        shooter = new NationalsShooter(hm, telemetry);
        storage = new ServoStorage(hm, telemetry, empty, shooter);
        intake = new NationalsIntake(hm);
        driveTrain = new DriveTrain(hm, telemetry);

        intakeRequested = false;
    }

    public NationalsRobot(HardwareMap hm, Telemetry telemetry) {
        this(hm, telemetry, true);
    }

    public void update() {
        storage.update();

        handleIntake();
        handleShooter();

        shooter.update();
        intake.update();
        driveTrain.update();
    }

    private void handleShooter() {
        if (storage.isIntaking()) {
            shooter.stop();
        } else {
            shooter.prepareForShot(0);
        }
    }

    public void setIntakeRequested(boolean value) {
        intakeRequested = value;
    }

    public void setDriveTrain(double x, double y, double rx, boolean slow, boolean fast){
        driveTrain.setDriveCoefficients(x,y,rx);
        driveTrain.setFast(fast);
        driveTrain.setSlow(slow);
    }

    private void switchToShootingMode() {
        if (storage.isIntaking()) {
            storage.forceShoot();
        }
    }

    public void fireGreen() {
        switchToShootingMode();
        storage.setQueue(Color.GREEN);
    }

    public void firePurple() {
        switchToShootingMode();
        storage.setQueue(Color.PURPLE);
    }

    public void fireMotif() {
        switchToShootingMode();
        storage.setQueue(Scoring.convertToScoringPattern(Motif.PPG));
    }

    public void fireAny() {
        switchToShootingMode();
        storage.allowAny();
    }

    public void forceIntake(){
        if(!storage.isIntaking()){
            storage.forceIntake();
        }
    }

    private void handleIntake() {
        if (storage.isIntaking()) {
            if (intakeRequested) {
                intake.runIntake();
            } else {
                intake.stop();
            }
        }
        else{
            if (intakeRequested){
                intake.stop();
            } else {
                intake.runTransfer();
            }
        }
    }
}
