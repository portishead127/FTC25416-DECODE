package org.firstinspires.ftc.teamcode.FSL.helper.configs;

import com.acmerobotics.dashboard.config.Config;
import com.bylazar.configurables.annotations.Configurable;

@Config
@Configurable
public class ShooterConfig {
    public static int LIMITFOR3POINTERRANGE = 100;
    public static double MOTORVELSCALARFOR3POINTER = 0.9;
    public static double MOTORVELSCALARFORLAYUP = 0.75;
    public static double SERVOPOSFOR3POINTER = 0.3;
    public static double SERVOPOSFORLAYUP = 0.7;
    public static final double WARM_UP_THRESHOLD = 0.90;
    public static final double VELOCITY_EPSILON = 20.0;


}
