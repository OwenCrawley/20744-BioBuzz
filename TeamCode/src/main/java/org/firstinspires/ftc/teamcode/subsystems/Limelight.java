package org.firstinspires.ftc.teamcode.subsystems;

import com.qualcomm.hardware.limelightvision.LLResultTypes;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.robotcore.hardware.HardwareMap;

//import org.firstinspires.ftc.teamcode.util.Pattern;

import java.util.List;

public class Limelight {

    Limelight3A l;
    private static final int shoot = 0, track = 1;
    private int pipeline = shoot;

    public Limelight(HardwareMap hardwareMap) {
        l = hardwareMap.get(Limelight3A.class, "limelight");
        l.setPollRateHz(20);
        l.start();
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
}
