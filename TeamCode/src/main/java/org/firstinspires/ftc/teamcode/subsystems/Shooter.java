package org.firstinspires.ftc.teamcode.subsystems;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.seattlesolvers.solverslib.command.SubsystemBase;


@Config

public class Shooter extends SubsystemBase {
    enum HoodState {
        CLOSED, POLLEN, NECTAR
    }
    public Servo f;
    private DcMotorEx l, r;

    private double t = 0;
    public static double kS = 0.535, kV = 0.00018, kP = 0.0025;
    public static double far_kS = 0.37, far_kV = 0.00027, far_kP = 0.0065;
    private boolean activated = true;
    public static double close = 1300;
    public static double far = 1650;
    public static double velocityError = 40;
    public static double rpmOffset = 0;
    public static double hoodClosed = 0.6, hoodNectar = 0.78, hoodPollen = 0.87;
    private HoodState hoodState = HoodState.CLOSED;
    public boolean isFar = false;
    public double hoodPos = 0.4;
    public Shooter(HardwareMap hardwareMap) {
        l = hardwareMap.get(DcMotorEx.class, "lShooter");
        r = hardwareMap.get(DcMotorEx.class, "rShooter");
        f = hardwareMap.get(Servo.class, "hood");
        r.setDirection(DcMotorSimple.Direction.REVERSE);
    }

    public double getTarget() {
        return t;
    }

    public double getVelocity() {
        return -l.getVelocity();
    }

    public void setPower(double p) {
        l.setPower(p);
        r.setPower(p);
    }

    public void off() {
        activated = false;
        setPower(0);
    }

    public void on() {
        activated = true;
    }

    public boolean isActivated() {
        return activated;
    }

    public void far() {
        setTarget(far);
        on();
        isFar = true;
    }

    public void close() {
        setTarget(close);
        on();
        isFar = false;
    }

//    double[][] table = {
//
//            {50.7,800+rpmOffset, 0.37},
//            {65.9,875+rpmOffset,0.37},
//            {83, 940 +rpmOffset,0.5},
//            {91, 1000+rpmOffset,0.5},
//            {105,1100+rpmOffset,0.6},
//            {125,1300+rpmOffset,0.7}, //far shot
//            {136,1345+rpmOffset,0.75},
//            {145,1350+rpmOffset,0.8}
//    };
//    public double calcShooterPower(double dist) {
//        if (dist <= table[0][0]) return table[0][1];
//
//        for (int i = 0; i < table.length - 1; i++) {
//
//            double d1 = table[i][0];
//            double t1 = table[i][1];
//            double d2 = table[i+1][0];
//            double t2 = table[i+1][1];
//
//            if (dist <= d2) {
//                return t1 + (dist - d1) * (t2 - t1) / (d2 - d1);
//            }
//        }
//
//        return table[table.length - 1][1];
//    }
//
//    public double calcHoodPower(double dist) {
//        if (dist <= table[0][0]) return table[0][2];
//
//        for (int i = 0; i < table.length - 1; i++) {
//
//            double d1 = table[i][0];
//            double t1 = table[i][2];
//            double d2 = table[i+1][0];
//            double t2 = table[i+1][2];
//
//            if (dist <= d2) {
//                return t1 + (dist - d1) * (t2 - t1) / (d2 - d1);
//            }
//        }
//
//        return table[table.length - 1][2];
//    }

    public void setTarget(double velocity) {
        t = velocity;
    }

    @Override
    public void periodic() {

        if (activated) {
            double power;
            power = (kV * getTarget()) + (kP * (getTarget() - getVelocity())) + kS;
            if (isFar) power = (far_kV * getTarget()) + (far_kP * (getTarget() - getVelocity())) + far_kS;
            setPower(power);
        }
    }

    public void setHoodState(HoodState state) {
        switch (state) {
            case CLOSED: f.setPosition(hoodClosed);
            case NECTAR: f.setPosition(hoodNectar);
            case POLLEN: f.setPosition(hoodPollen);
        }
        hoodState = state;
    }
    public void nectar() {
        setHoodState(HoodState.NECTAR);
    }
    public void pollen() {
        setHoodState(HoodState.POLLEN);
    }
    public void closeHood() {
        setHoodState(HoodState.CLOSED);
    }

    public boolean atTarget() {
//        return Math.abs((getTarget()- getVelocity())) < ApolloConstants.shooterVelError;
        return getVelocity() >= getTarget()-velocityError;
    }

    public void forDistance(double distance) {
        //setTarget((6.13992 * distance) + 858.51272);

//        setTarget((0.00180088*Math.pow(distance, 2))+(4.14265*distance)+948.97358);
    }

    public double clamp(double val, double min, double max) {
        if (val < min) return min;
        if (val > max) return max;
        return val;
    }
}
