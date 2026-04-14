package org.firstinspires.ftc.teamcode.FSL.helper.configs;

import com.acmerobotics.dashboard.config.Config;
import com.bylazar.configurables.annotations.Configurable;

import org.firstinspires.ftc.teamcode.FSL.helper.scoring.Scoring;

@Config
@Configurable
public class ShooterConfig {
    public static double KP = 190;
    public static double KI = 0;
    public static double KD = 0;
    public static double KF = 13;
    public static int LIMIT_FOR_3_POINTER_RANGE = 100;
    public static double MOTOR_VEL_SCALAR_FOR_3_POINTER = 2100;
    public static double MOTOR_VEL_SCALAR_FOR_LAYUP = 1600;
    public static double MOTOR_VEL_SCALAR_FOR_TIP = 1900;
    public static double SERVO_POS_FOR_3_POINTER = 0.85;
    public static double SERVO_POS_FOR_TIP = 1;
    public static double SERVO_POS_FOR_LAYUP = 0.7;
    public static double WARM_UP_THRESHOLD = 0.95;
//
//    // ====================== SCORING / SHOOTER COMMANDS ======================
//        if (gamepad2.triangleWasPressed()) {
//        storage.setQueue(Scoring.convertToScoringPattern(motif));
//        shooter.fire(1700);
//        shooter.setServo(0.92);
//    }if (gamepad2.squareWasPressed()) {
//        storage.setQueue(Scoring.convertToScoringPattern(motif));
//        shooter.fire(1900);
//        shooter.setServo(0.9);
//    }
//        if (gamepad2.crossWasPressed()) {
//        storage.setQueue(Scoring.convertToScoringPattern(motif));
//        shooter.fire(2100);
//        shooter.setServo(0.85);
//    }

    public static double BLOCKER_OPEN = 0.7;
    public static double BLOCKER_CLOSED = 0.5;

}
