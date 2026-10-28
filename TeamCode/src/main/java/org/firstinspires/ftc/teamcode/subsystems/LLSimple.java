package org.firstinspires.ftc.teamcode.subsystems;

import com.qualcomm.hardware.limelightvision.LLResultTypes;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.util.Alliance;

import java.util.List;

public class LLSimple {

    Limelight3A l;
    private static final int shoot = 0, track = 1;
    private int pipeline = shoot;

    private Alliance a;

    public LLSimple(HardwareMap hardwareMap, Alliance alliance) {
        l = hardwareMap.get(Limelight3A.class, "limelight");
        l.setPollRateHz(20);
        l.start();
        this.a = alliance;
    }

    public void switchToShoot() {
        if (pipeline != shoot)
            l.pipelineSwitch(shoot);
        l.setPollRateHz(20);
        l.start();
    }

    public double detectedID() {
        List<LLResultTypes.FiducialResult> r = l.getLatestResult().getFiducialResults();

        if (r.isEmpty()) return 0;

        LLResultTypes.FiducialResult target = null;
        for (LLResultTypes.FiducialResult i: r) {
            if (i != null) return i.getFiducialId();
        }

        return 0;
    }
    public boolean hiveUp() {
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
    public boolean redTag(int id) {
        return id >= 30 && id <= 37;
    }
    public boolean blueTag(int id) {
        return id >= 38 && id <= 45;
    }
}
