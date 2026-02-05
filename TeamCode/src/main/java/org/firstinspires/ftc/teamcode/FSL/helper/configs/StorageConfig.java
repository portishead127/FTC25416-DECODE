package org.firstinspires.ftc.teamcode.FSL.helper.configs;

import com.acmerobotics.dashboard.config.Config;
import com.bylazar.configurables.annotations.Configurable;

@Configurable
@Config
public class StorageConfig {
   public static double KP = 0;
   public static double KI = 0;
   public static double KD = 0;
   public static double TICKTOLERANCE = 5;
   public static double ENCODERRESOLUTION = 28;
   public static final long FLICKFORWARDTIME = 200;
   public static final long FLICKRETURNTIME = 350; //total
}
