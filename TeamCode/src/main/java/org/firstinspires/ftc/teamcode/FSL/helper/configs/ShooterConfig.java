package org.firstinspires.ftc.teamcode.FSL.helper.configs;

import com.acmerobotics.dashboard.config.Config;
import com.bylazar.configurables.annotations.Configurable;

@Config
@Configurable
public class ShooterConfig {
    public static int LIMIT_FOR_3_POINTER_RANGE = 100;
    public static double MOTOR_VEL_SCALAR_FOR_3_POINTER = 0.9;
    public static double MOTOR_VEL_SCALAR_FOR_LAYUP = 0.75;
    public static double SERVO_POS_FOR_3_POINTER = 0.3;
    public static double SERVO_POS_FOR_LAYUP = 0.7;
    public static final double WARM_UP_THRESHOLD = 0.90;
    public static final double VELOCITY_EPSILON = 20.0;


}
