package org.firstinspires.ftc.teamcode.FSL.helper.control;

import com.pedropathing.geometry.Pose;

import org.jetbrains.annotations.TestOnly;

public class Localization {
    private static Pose desiredGoalPose;
    public static void setDesiredGoalPose(Pose goalPose){
        desiredGoalPose = goalPose;
    }
    public static double calculateTurretAngle(Pose robotPose){
        return calculateFieldCentricAngle(robotPose) - robotPose.getHeading();
    }
    @TestOnly
    public static double calculateFieldCentricAngle(Pose robotPose){
        double xToGoal = desiredGoalPose.getX() - robotPose.getX();
        double yToGoal = desiredGoalPose.getY() - robotPose.getY();
        return Math.atan2(yToGoal, xToGoal);
    }
    public static double calculateDistance(Pose robotPose){
        return robotPose.distanceFrom(desiredGoalPose);
    }
}
