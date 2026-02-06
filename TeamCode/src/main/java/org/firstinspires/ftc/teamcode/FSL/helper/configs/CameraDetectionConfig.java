package org.firstinspires.ftc.teamcode.FSL.helper.configs;

import com.acmerobotics.dashboard.config.Config;
import com.bylazar.configurables.annotations.Configurable;

@Config
@Configurable
public class CameraDetectionConfig {
    public static double KP = 0.035;
    public static double KI = 0.0;
    public static double KD = 0.004;
    public static double TICKSPERDEGREE = (double) 28 /360;
    // Safety
    public static double MAXVEL = 120.0;
    public static double MAXOFFSET = 14;
    public static int CENTRALTOLERANCE = 3;
}
