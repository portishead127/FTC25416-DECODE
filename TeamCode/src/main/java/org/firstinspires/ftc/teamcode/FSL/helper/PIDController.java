package org.firstinspires.ftc.teamcode.FSL.helper;

import com.qualcomm.robotcore.util.ElapsedTime;

public class PIDController {
    final double kp;
    final double ki;
    final double kd;
    double target;
    double tolerance;
    double integralSum;
    double lastError;
    ElapsedTime timer;
    public PIDController(double kp, double ki, double kd){
        this.kp = kp;
        this.ki = ki;
        this.kd = kd;
        tolerance = 0;

        timer = new ElapsedTime();
        reset();
    }
    public PIDController(double kp, double ki, double kd, double tolerance){
        this.kp = kp;
        this.ki = ki;
        this.kd = kd;
        this.tolerance = tolerance;

        timer = new ElapsedTime();
        reset();
    }
    public void setTarget(double target, boolean append){
        if(target != this.target){
           reset();
        }
        if(append){
            this.target += target;
        }
        else{
            this.target = target;
        }
    }
    public void reset(){
        integralSum = 0;
        lastError = 0;
        timer.reset();
    }
    public double calculateScalar(double state){
        double error = target - state;
        double derivative = (error - lastError) / timer.seconds();

        integralSum = integralSum + (error * timer.seconds());

        double out = (kp * error) + (ki * integralSum) + (kd * derivative);

        lastError = error;
        if(Math.abs(out) <= tolerance){
            return 0;
        }
        return out;
   }
}
