package org.firstinspires.ftc.teamcode.FSL.helper.subsystems;

import com.qualcomm.robotcore.hardware.ColorRangeSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.teamcode.FSL.helper.control.PIDController;
import org.firstinspires.ftc.teamcode.FSL.helper.colors.ColorMethods;
import org.firstinspires.ftc.teamcode.FSL.helper.colors.Color;
import org.firstinspires.ftc.teamcode.FSL.helper.configs.StorageConfig;
import org.firstinspires.ftc.teamcode.FSL.helper.scoring.Scoring;

import java.util.ArrayDeque;
import java.util.LinkedList;

public class Storage {
    private final ArrayDeque<Color> queue = new ArrayDeque<>();
    public final Color[] slots;
    private final ColorRangeSensor colorSensor;
    public final DcMotorEx motor;
    public final Servo servo;
    private final Telemetry telemetry;
    public final PIDController pidController;
    private final ElapsedTime flickTimer;
    private Color currentColor;
    private boolean intakeMode;
    private boolean isFlicking;
    private boolean wasIntakeMode;
    private int focusedIndex;
    private final double basePosition;
    private final double ticksPerThird;
    private final double ticksPerHalf;
    private final ElapsedTime elapsedTime;

    // NEW: flag to handle immediate transition after last flick
    private boolean justEmptiedOnLastFlick = false;

    public Storage(HardwareMap hm, Telemetry telemetry, boolean emptyStorage) {
        motor = hm.get(DcMotorEx.class, "STM");
        motor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        motor.setDirection(DcMotorSimple.Direction.REVERSE);

        servo = hm.get(Servo.class, "FLS");
        servo.setPosition(StorageConfig.FLICK_SERVO_MIN);

        colorSensor = hm.get(ColorRangeSensor.class, "CS");
        colorSensor.enableLed(true);

        this.telemetry = telemetry;

        pidController = new PIDController(StorageConfig.KP, StorageConfig.KI, StorageConfig.KD, StorageConfig.KF);

        if (emptyStorage) {
            slots = new Color[]{null, null, null};
        } else {
            slots = new Color[]{Color.PURPLE, Color.PURPLE, Color.GREEN};
        }

        wasIntakeMode = !emptyStorage;
        intakeMode = emptyStorage;
        flickTimer = new ElapsedTime();
        isFlicking = false;
        currentColor = Color.NONE;

        focusedIndex = 0;

        basePosition = motor.getCurrentPosition();

        ticksPerThird = StorageConfig.ENCODER_RES / 3.0;
        ticksPerHalf  = StorageConfig.ENCODER_RES / 2.0;

        pidController.setOutputLimits(-1, 1);
        pidController.setTolerance(StorageConfig.TICK_TOLERANCE);
        elapsedTime = new ElapsedTime();
    }

    public void updateFlick() {
        if (!isFlicking) return;

        if (flickTimer.milliseconds() < StorageConfig.FLICK_FORWARD_TIME) {
            servo.setPosition(StorageConfig.FLICK_SERVO_MAX);
        } else if (flickTimer.milliseconds() < StorageConfig.FLICK_RETURN_TIME) {
            servo.setPosition(StorageConfig.FLICK_SERVO_MIN);
        } else {
            // Flick complete
            isFlicking = false;
            queue.removeFirst();
            slots[focusedIndex] = null;

            // Critical fix: if this was the last pixel, immediately transition to intake mode
            if (queue.isEmpty()) {
                justEmptiedOnLastFlick = true;
                intakeMode = true;                  // force now
                goToSlot0AlignedWithIntake();       // force target to intake side now
            }
        }
    }

    public void startFlick() {
        if (isFlicking || queue.isEmpty()) return;  // prevent flick after queue empty
        isFlicking = true;
        flickTimer.reset();
    }

    public boolean isEmpty() {
        return intakeMode;
    }

    public void setQueue(LinkedList<Color> colors) {
        queue.clear();
        queue.addAll(colors);
    }

    public boolean queueIsEmpty() {
        return queue.isEmpty();
    }

    public void rotate1Slot(boolean anticlockwise) {
        if (!pidController.atTarget() || isFlicking) return;

        if (anticlockwise) {
            focusedIndex = (focusedIndex + 1) % 3;
        } else {
            focusedIndex = (focusedIndex - 1 + 3) % 3;
        }

        double target;
        if (intakeMode) {
            target = basePosition + focusedIndex * ticksPerThird;
        } else {
            target = basePosition + ticksPerHalf + focusedIndex * ticksPerThird;
        }
        pidController.setTarget(target);
    }

    public void goToSlot0AlignedWithShooter() {
        focusedIndex = 0;
        pidController.setTarget(basePosition + ticksPerHalf);
    }

    public void goToSlot0AlignedWithIntake() {
        focusedIndex = 0;
        pidController.setTarget(basePosition);
    }

    public void update(boolean shootable) {
        intakeMode = ((slots[0] == null || slots[1] == null || slots[2] == null) && queueIsEmpty());

        if (justEmptiedOnLastFlick) {
            justEmptiedOnLastFlick = false;
            intakeMode = true;
        }

        boolean justBecameFull  = wasIntakeMode && !intakeMode;

        updateFlick();

        if (intakeMode) {
            intakeUpdate();
        } else {
            if (justBecameFull) {
                goToSlot0AlignedWithShooter();
            }
            transferUpdate(shootable);
        }

        wasIntakeMode = intakeMode;

        if (!isFlicking) {
            double power = pidController.calculate(motor.getCurrentPosition());
            motor.setPower(power);
        } else {
            motor.setPower(0);
        }
    }

    public void intakeUpdate() {
        if (!pidController.atTarget() || isFlicking) {
            return;
        }

        currentColor = ColorMethods.fromSensor(colorSensor);

        if (currentColor != Color.NONE) {
            slots[focusedIndex] = currentColor;
            rotate1Slot(true);
        }
    }

    public void transferUpdate(boolean shootable) {
        if (!pidController.atTarget() || isFlicking || queueIsEmpty()) {
            return;
        }

        Color desired = queue.peekFirst();

        if (desired == slots[focusedIndex]) {
            if (shootable) {
                startFlick();
            }
        } else if (desired == slots[(focusedIndex + 1) % 3]) {
            rotate1Slot(true);
        } else if (desired == slots[(focusedIndex - 1 + 3) % 3]) {
            rotate1Slot(false);
        } else {
            if (slots[focusedIndex] != null) {
                if (shootable) {
                    startFlick();
                }
            } else if (slots[(focusedIndex + 1) % 3] != null) {
                rotate1Slot(false);
            } else if (slots[(focusedIndex - 1 + 3) % 3] != null) {
                rotate1Slot(true);
            }
        }
    }

    public void sendTelemetry() {
        telemetry.addLine("STORAGE - HARDWARE\n");
        telemetry.addData("SERVO POS", servo.getPosition());
        telemetry.addData("MOTOR POS", motor.getCurrentPosition());
        telemetry.addData("MOTOR TARGET POS", pidController.getTarget());
        telemetry.addData("AT TARGET", pidController.atTarget());
        telemetry.addData("LAST MOTOR OUTPUT", pidController.getLastOutput());
        telemetry.addData("MOTOR VEL", motor.getVelocity());
        telemetry.addData("COLOR SENSOR", currentColor.name());
        telemetry.addData("DETECTION DISTANCE (mm)", colorSensor.getDistance(DistanceUnit.MM));

        telemetry.addLine("STORAGE - SLOTS\n");
        telemetry.addData("INTAKE MODE", intakeMode);
        telemetry.addData("FOCUSED INDEX", focusedIndex);
        telemetry.addData("SLOT 0", slots[0]);
        telemetry.addData("SLOT 1", slots[1]);
        telemetry.addData("SLOT 2", slots[2]);

        telemetry.addLine("STORAGE - QUEUE\n");
        telemetry.addData("EMPTY", queueIsEmpty());
        telemetry.addData("QUEUE", queue);

        telemetry.addLine("STORAGE - FLICKER\n");
        telemetry.addData("FLICKING", isFlicking);
        telemetry.addData("FLICK TIMER (ms)", flickTimer.milliseconds());
    }
}