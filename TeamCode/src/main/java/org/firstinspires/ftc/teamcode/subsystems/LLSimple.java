package org.firstinspires.ftc.teamcode.subsystems;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.LLResultTypes;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.teamcode.util.Alliance;

import java.util.List;
@Config
public class LLSimple {

    public Limelight3A l;
    private static final int generalTags = 0, align = 1;
    private int pipeline = generalTags;

    private Alliance a;

    public static double turnKP = 0.8;
    public static double txOffset = 1;
    public static double deadBand = 1.0;
    public static double maxTurn = 0.4;

    public static double driveSign = 1;     // flip to -1 if it moves the wrong way
    public static double turnMinPower = 0.50, driveMinPower = 0.5;

    public static double tyKP = 0.03, tyTarget = -15, tyDeadband = 1.0, maxDrive = 0.8;

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
        if (Math.abs(turn) < turnMinPower) turn = Math.signum(turn) * turnMinPower;
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

    public double alignDrive() {
        LLResultTypes.FiducialResult t = findHiveTag();
        if (t == null) return 0;

        double error = t.getTargetYDegrees() - tyTarget;   // positive = too close, negative = too far
        if (Math.abs(error) <= tyDeadband) return 0;

        double out = -tyKP * error * driveSign;
        if (Math.abs(out) < driveMinPower) out = Math.signum(out) * driveMinPower;
        return Range.clip(out, -maxDrive, maxDrive);
    }

}
