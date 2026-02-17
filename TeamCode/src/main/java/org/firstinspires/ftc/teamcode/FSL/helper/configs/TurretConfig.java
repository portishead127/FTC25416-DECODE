package org.firstinspires.ftc.teamcode.FSL.helper.configs;

import com.acmerobotics.dashboard.config.Config;
import com.bylazar.configurables.annotations.Configurable;

import org.firstinspires.ftc.teamcode.FSL.helper.constants.UltraplanetaryMotorConstants;

@Config
@Configurable
public class TurretConfig {
    public static double KP = 0.035;
    public static double KI = 0.0;
    public static double KD = 0.004;
    public static double TICKS_PER_DEGREE = ((double) 128/24) * (UltraplanetaryMotorConstants.ENCODER_RES / 360);
    public static double TICKS_PER_RADIAN = ((double) 128/24) * (UltraplanetaryMotorConstants.ENCODER_RES / 2 * Math.PI);
    public static double MAX_VEL = 120.0;
    public static double MAX_OFFSET = TICKS_PER_DEGREE * 90;
    public static int CENTRALTOLERANCE = 0;
}
