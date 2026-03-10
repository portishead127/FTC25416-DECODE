package org.firstinspires.ftc.teamcode.FSL.helper.control;

import com.qualcomm.robotcore.util.ElapsedTime;

public class PIDController {

    // Gains
    private final double kp;
    private final double ki;
    private final double kd;
    private final double kf;

    // State
    private double target = 0;
    private double tolerance = 0;

    private double integralSum = 0;
    private double lastError = 0;
    private double lastOutput = 0;

    private double minOutput = -Double.MAX_VALUE;
    private double maxOutput = Double.MAX_VALUE;
    private final ElapsedTime timer = new ElapsedTime();

    // Constructors
    public PIDController(double kp, double ki, double kd) {
        this(kp, ki, kd, 0);
    }

    public PIDController(double kp, double ki, double kd, double kf) {
        this.kp = kp;
        this.ki = ki;
        this.kd = kd;
        this.kf = kf;
        reset();
    }

    // ============================
    // Configuration Methods
    // ============================

    public void setTarget(double target) {
        if (this.target != target) {
            this.target = target;
            reset();
        }
    }

    public void setTolerance(double tolerance) {
        this.tolerance = tolerance;
    }

    public void setOutputLimits(double min, double max) {
        this.minOutput = min;
        this.maxOutput = max;
    }

    public double calculate(double state) {

        double dt = timer.seconds();
        timer.reset();

        if (dt <= 0) dt = 1e-6;

        double error = getError(state);

        // Integral
        integralSum += error * dt;
        double maxIntegral = Double.MAX_VALUE;
        double minIntegral = -Double.MAX_VALUE;
        integralSum = clamp(integralSum, minIntegral, maxIntegral);

        // Derivative
        double derivative = (error - lastError) / dt;

        // PID Output
        double output =
                (kp * error)
                + (ki * integralSum)
                + (kd * derivative)
                + kf;

        output = clamp(output, minOutput, maxOutput);

        lastError = error;
        lastOutput = output;


        return output;
    }

    private double getError(double state) {
        return target - state;
    }
    public boolean atTarget() {
        return Math.abs(lastError) < tolerance;
    }
    public void reset() {
        integralSum = 0;
        lastError = 0;
        timer.reset();
    }

    private double clamp(double value, double min, double max) {
        return Math.max(min, Math.min(max, value));
    }

    public double getTarget() {
        return target;
    }
    public double getLastOutput() {
        return lastOutput;
    }
}
