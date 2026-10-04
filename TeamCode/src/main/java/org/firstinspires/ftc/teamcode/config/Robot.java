package org.firstinspires.ftc.teamcode.config;

//import static org.firstinspires.ftc.teamcode.config.ApolloConstants.KDOWN;
//import static org.firstinspires.ftc.teamcode.config.ApolloConstants.KUP;

import com.pedropathing.follower.Follower;
import com.pedropathing.math.Pose;
import com.pedropathing.utils.Timer;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.subsystems.Intake;
import org.firstinspires.ftc.teamcode.subsystems.Shooter;
import org.firstinspires.ftc.teamcode.subsystems.Turret;
import org.firstinspires.ftc.teamcode.pedro.Constants;
import org.firstinspires.ftc.teamcode.util.Alliance;

public class Robot {
    public final Intake i;
    public final Shooter s;
    public final Turret t;
    public final Follower f;
    public Alliance a;

//    private final LynxModule hub;
    private final Timer loop = new Timer();

    public static Pose endPose;
//    public static Pose defaultPose = new Pose(8+24,6.25+24,0);
    public static Pose defaultPose = new Pose(0,0,0);
    public static Pose shootTarget = new Pose(6, 144-6, 0);

    public Robot(HardwareMap h, Alliance a) {
        this.a = a;
        i = new Intake(h);
        s = new Shooter(h);
        t = new Turret(h);
        f = Constants.create(h);

//        hub = h.getAll(LynxModule.class).get(0);
//        hub.setBulkCachingMode(LynxModule.BulkCachingMode.MANUAL);

        loop.reset();
//        setShootTarget();
    }

    public void periodic() {
//        setShootTarget();

//        if (loop.getElapsedTime() % 5 == 0) {
//            hub.clearBulkCache();
//        }

        f.update();
        t.periodic();
        s.periodic();
    }

    public void stop() {
        endPose = f.pose();
    }

    public void saveEnd() {
        endPose = f.pose();
    }


//    public void setShootTarget() {
//        if (a == Alliance.BLUE)
//            shootTarget = FieldPoses.blueHoop;
//        else if (a == Alliance.RED)
//            shootTarget = FieldPoses.redHoop;
//    }

    public Pose getShootTarget() {
        return shootTarget;
    }

}