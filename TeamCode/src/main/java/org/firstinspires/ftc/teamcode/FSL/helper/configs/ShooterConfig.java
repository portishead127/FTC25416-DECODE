package org.firstinspires.ftc.teamcode.FSL.helper.configs;

import com.acmerobotics.dashboard.config.Config;
import com.bylazar.configurables.annotations.Configurable;

@Config
@Configurable
public class ShooterConfig {
    public static double KP = 12;
    public static double KI = 0;
    public static double KD = 0;
    public static double KF = 13;
    public static int LIMIT_FOR_3_POINTER_RANGE = 100;
    public static double MOTOR_VEL_SCALAR_FOR_3_POINTER = 0.9;
    public static double MOTOR_VEL_SCALAR_FOR_LAYUP = 0.75;
    public static double SERVO_POS_FOR_3_POINTER = 0.3;
    public static double SERVO_POS_FOR_LAYUP = 0.7;
    public static double WARM_UP_THRESHOLD = 0.95;
    public static double VELOCITY_EPSILON = 20.0;


}
