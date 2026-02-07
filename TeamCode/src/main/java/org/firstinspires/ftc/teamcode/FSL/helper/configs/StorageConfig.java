package org.firstinspires.ftc.teamcode.FSL.helper.configs;

import com.acmerobotics.dashboard.config.Config;
import com.bylazar.configurables.annotations.Configurable;

@Configurable
@Config
public class StorageConfig {
   public static double KP = 0;
   public static double KI = 0;
   public static double KD = 0;
   public static double TICK_TOLERANCE = 5;
   public static final long FLICK_FORWARD_TIME = 200;
   public static final long FLICK_RETURN_TIME = 350; //total
}
