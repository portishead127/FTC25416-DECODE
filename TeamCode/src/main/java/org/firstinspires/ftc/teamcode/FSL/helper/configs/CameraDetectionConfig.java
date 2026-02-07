package org.firstinspires.ftc.teamcode.FSL.helper.configs;

import com.acmerobotics.dashboard.config.Config;
import com.bylazar.configurables.annotations.Configurable;

import org.firstinspires.ftc.teamcode.FSL.helper.constants.UltraplanetaryMotorConstants;

@Config
@Configurable
public class CameraDetectionConfig {
    public static double KP = 0.035;
    public static double KI = 0.0;
    public static double KD = 0.004;
    public static double TICKS_PER_DEGREE = UltraplanetaryMotorConstants.ENCODER_RES /360;
    public static double MAX_VEL = 120.0;
    public static double MAX_OFFSET = 14;
    public static int CENTRALTOLERANCE = 3;
}
