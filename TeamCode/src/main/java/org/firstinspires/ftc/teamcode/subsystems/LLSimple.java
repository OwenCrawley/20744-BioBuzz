package org.firstinspires.ftc.teamcode.subsystems;

import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.LLResultTypes;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.robotcore.external.navigation.Position;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.teamcode.util.Alliance;

import java.util.List;

public class LLSimple {

    public Limelight3A l;
    private static final int generalTags = 0, align = 1;
    private int pipeline = generalTags;

    private Alliance a;

    public static double turnKP = 0.25;
    public static double txOffset = 1;
    public static double deadBand = 1.0;
    public static double minPower = 0.5;
    public static double maxTurn = 0.4;

    public static double driveKP = 0.1, strafeKP = 0.1;   // power per inch of error
    public static double posDeadband = 0.5, maxHold = 0.3;  // inches, max power
    public static double driveSign = 1, strafeSign = 1;     // flip to -1 if it moves the wrong way
    public static double yawKP = 0.02, yawDeadband = 1.0, yawSign = 1;
    private double holdX, holdZ, holdYaw;
    private boolean holdSet = false;

    public static double tyKP = 0.03, tyTarget = -15, tyDeadband = 1.0, maxDrive = 0.8;

    public boolean isHoldSet()
    {
        return holdSet;
    }

    public LLSimple(HardwareMap hardwareMap, Alliance alliance) {
        l = hardwareMap.get(Limelight3A.class, "limelight");
        l.setPollRateHz(50);
        l.start();
        this.a = alliance;
    }

    public void switchToGeneralTags() {
        if (pipeline != generalTags)
            l.pipelineSwitch(generalTags);
        pipeline = generalTags;
        l.start();
    }
    public void switchToAlign() {
        if (pipeline != align)
            l.pipelineSwitch(align);
        pipeline = align;
        l.start();
    }

    public double detectedID() {
        if (l.getLatestResult() == null) return 0;
        List<LLResultTypes.FiducialResult> r = l.getLatestResult().getFiducialResults();

        if (r.isEmpty()) return 0;

        LLResultTypes.FiducialResult target = null;
        for (LLResultTypes.FiducialResult i: r) {
            if (i != null) return i.getFiducialId();
        }

        return 0;
    }
    public boolean hiveUp() {
        if (l.getLatestResult() == null) return false;
//        switchToGeneralTags();
        List<LLResultTypes.FiducialResult> r = l.getLatestResult().getFiducialResults();

        if (r.isEmpty()) return false;
        boolean seesTarget = false;
        LLResultTypes.FiducialResult target = null;
        for (LLResultTypes.FiducialResult i: r) {

            if (i != null) {
                if (a == Alliance.BLUE && blueTag(i.getFiducialId())) seesTarget = true;
                if (a == Alliance.RED && redTag(i.getFiducialId())) seesTarget = true;
            }

        }

        return seesTarget;
    }

    public double AlignHive()
    {
        LLResult res = l.getLatestResult();
        if (res == null) return 0;

        LLResultTypes.FiducialResult tag = null;
        for (LLResultTypes.FiducialResult i : res.getFiducialResults()) {
            if (i == null) continue;
            int id = i.getFiducialId();
            if ((a == Alliance.BLUE && blueTag(id)) || (a == Alliance.RED && redTag(id))) {
                tag = i;
                break;
            }
        }
        if (tag == null)
        {
            return 0;
        }

        double error = tag.getTargetXDegrees() - txOffset;
        if (Math.abs(error) <= deadBand)
        {
            return 0;
        }
        double turn = turnKP * error;
        if (Math.abs(turn) < minPower) turn = Math.signum(turn) * minPower;
        return Range.clip(turn, -maxTurn, maxTurn);
    }



    public boolean redTag(int id) {
        return id >= 30 && id <= 37;
    }
    public boolean blueTag(int id) {
        return id >= 38 && id <= 45;
    }


    private LLResultTypes.FiducialResult findHiveTag() {
        LLResult res = l.getLatestResult();
        if (res == null) return null;
        for (LLResultTypes.FiducialResult i : res.getFiducialResults()) {
            if (i == null) continue;
            int id = i.getFiducialId();
            if ((a == Alliance.BLUE && blueTag(id)) || (a == Alliance.RED && redTag(id))) return i;
        }
        return null;
    }

    private double inches(Position p, double v)
    {
        return p.unit.toInches(v);
    }

    public void startHold()
    {
        switchToAlign();
        LLResultTypes.FiducialResult t = findHiveTag();
        if (t == null)
        {
            holdSet = false;
            return;
        }

        Position cam = t.getTargetPoseCameraSpace().getPosition();
        Position rob = t.getRobotPoseTargetSpace().getPosition();
        holdZ = inches(cam, cam.z);
        holdX = inches(rob, rob.x);
        holdYaw = t.getRobotPoseTargetSpace().getOrientation().getYaw(AngleUnit.DEGREES);
        holdSet = true;
    }

    public void stopHold()
    {
        holdSet = false;
    }

    private double shapeHold(double err, double kp) {
        if (Math.abs(err) <= posDeadband)
            return 0;
        double out = kp * err;
        if (Math.abs(out) < minPower) out = Math.signum(out) * minPower;
        return Range.clip(out, -maxHold, maxHold);
    }

    public double[] holdPosition() {
        LLResultTypes.FiducialResult t = findHiveTag();
        if (t == null || !holdSet) return new double[]{0, 0};

        Position cam = t.getTargetPoseCameraSpace().getPosition();
        Position rob = t.getRobotPoseTargetSpace().getPosition();
        double zErr = inches(cam, cam.z) - holdZ;   // positive = farther than the held spot
        double xErr = inches(rob, rob.x) - holdX;   // sideways error in tag space

        return new double[]{
                shapeHold(zErr, driveKP * driveSign),
                shapeHold(xErr, strafeKP * strafeSign)
        };

    }

    public double holdYawTurn() {
        LLResultTypes.FiducialResult t = findHiveTag();
        if (t == null || !holdSet) return 0;

        double yaw = t.getRobotPoseTargetSpace().getOrientation().getYaw(AngleUnit.DEGREES);
        double err = yaw - holdYaw;
        if (err > 180) err -= 360;      // wrap to -180..180
        if (err < -180) err += 360;
        if (Math.abs(err) <= yawDeadband) return 0;

        double turn = yawKP * yawSign * err;
        if (Math.abs(turn) < minPower) turn = Math.signum(turn) * minPower;
        return Range.clip(turn, -maxTurn, maxTurn);
    }

    public double alignDrive() {
        LLResultTypes.FiducialResult t = findHiveTag();
        if (t == null) return 0;

        double error = t.getTargetYDegrees() - tyTarget;   // positive = too close, negative = too far
        if (Math.abs(error) <= tyDeadband) return 0;

        double out = -tyKP * error * driveSign;
        if (Math.abs(out) < minPower) out = Math.signum(out) * minPower;
        return Range.clip(out, -maxDrive, maxDrive);
    }

}
