package org.firstinspires.ftc.teamcode.FSL.helper.configs;

import com.acmerobotics.dashboard.config.Config;
import com.bylazar.configurables.annotations.Configurable;

@Config
@Configurable
public class TurretConfig {
    public static double KP = 0.005;
    public static double KI = 0;
    public static double KD = 0;
    public static double ENCODER_RES = 780;
    public static double TICK_TOLERANCE = 20;
    public static double TICKS_PER_DEGREE = (ENCODER_RES / 360);
    public static double TICKS_PER_RADIAN = (ENCODER_RES / (2 * Math.PI));
    public static double MAX_VEL = 120.0;
    public static double MAX_OFFSET = TICKS_PER_DEGREE * 180;
}
