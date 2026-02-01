package org.firstinspires.ftc.teamcode.FSL.helper.configs;

import com.acmerobotics.dashboard.config.Config;
import com.bylazar.configurables.annotations.Configurable;

@Config
@Configurable
public class CameraDetectionConfig {
    public static double KP = 0.035;
    public static double KI = 0.0;
    public static double KD = 0.004;
    public static double TICKS_PER_DEGREE = 28.0;

    // Safety
    public static double MAX_DEG_PER_SEC = 120.0;
    public static int CENTRALTOLERANCE = 3;
}
