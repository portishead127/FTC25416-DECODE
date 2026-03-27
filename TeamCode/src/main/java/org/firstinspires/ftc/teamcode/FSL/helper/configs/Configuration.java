package org.firstinspires.ftc.teamcode.FSL.helper.configs;

import com.acmerobotics.dashboard.config.Config;

public class Configuration {
    @Config
    public static class CameraConfig {
        public static int BLUE_TARGET_ID = 20;
        public static int RED_TARGET_ID = 24;
    }
    @Config
    public static class IntakeConfig {
        public static double FORWARD_SCALAR = 0.7;
        public static double BACKWARD_SCALAR = 0.6;
    }
    @Config
    public static class MecanumConfig {
        public static double MECANUM_FULL_POWER = 1;
        public static double MECANUM_MED_POWER = 0.67;
        public static double MECANUM_SLOW_POWER = 0.2;
    }
    @Config
    public static class ShooterConfig {
        public static double KP = 12;
        public static double KI = 0;
        public static double KD = 0;
        public static double KF = 13;
        public static double WARM_UP_THRESHOLD = 0.95;
    }
    @Config
    public static class StorageConfig {
        public static double INTAKE_AT_SLOT1_SERVO_POS = 0;
        public static double INTAKE_AT_SLOT2_SERVO_POS;
        public static double INTAKE_AT_SLOT3_SERVO_POS;
        public static double INTAKE_AT_SLOT1_ENCODER_POS = 0;
        public static double INTAKE_AT_SLOT2_ENCODER_POS;
        public static double INTAKE_AT_SLOT3_ENCODER_POS;
        public static double SHOOTING_FROM_SLOT1_SERVO_POS;
        public static double SHOOTING_FROM_SLOT2_SERVO_POS;
        public static double SHOOTING_FROM_SLOT3_SERVO_POS;
        public static double SHOOTING_FROM_SLOT1_ENCODER_POS;
        public static double SHOOTING_FROM_SLOT2_ENCODER_POS;
        public static double SHOOTING_FROM_SLOT3_ENCODER_POS;
        public static double ENCODER_TOLERANCE;
        public static double FLICK_SPEED;
    }
    @Config
    public static class TurretConfig {
        public static double KP = 0.005;
        public static double KI = 0;
        public static double KD = 0;
        public static double ENCODER_RES = 780;
        public static double TICK_TOLERANCE = 20;
        public static double TICKS_PER_DEGREE = (ENCODER_RES / 360);
        public static double TICKS_PER_RADIAN = (ENCODER_RES / (2 * Math.PI));
        public static double MAX_OFFSET = TICKS_PER_DEGREE * 180;
    }
}
