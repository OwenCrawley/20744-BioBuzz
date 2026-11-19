package org.firstinspires.ftc.teamcode.opmode.auto;

import static com.pedropathing.api.Paths.curve;
import static com.pedropathing.api.Paths.line;

import static org.firstinspires.ftc.teamcode.config.FieldPoses.redParkControl;
import static org.firstinspires.ftc.teamcode.config.FieldPoses.redParkLeft;
import static org.firstinspires.ftc.teamcode.config.FieldPoses.redParkRight;
import static org.firstinspires.ftc.teamcode.config.FieldPoses.redShootControl;
import static org.firstinspires.ftc.teamcode.config.FieldPoses.redShootLeft;
import static org.firstinspires.ftc.teamcode.config.FieldPoses.redShootRight;

import com.pedropathing.math.Pose;
import com.pedropathing.paths.Path;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.config.FieldPoses;
import org.firstinspires.ftc.teamcode.config.Robot;
import org.firstinspires.ftc.teamcode.util.Alliance;

import java.io.File;
import java.lang.reflect.Field;

@Autonomous
public class PreLoadRight extends OpMode{
    final Alliance a;
    Robot r;

    Pose start = FieldPoses.rightStartRed;
    Pose shootRight = redShootRight;
    Pose shootLeft = FieldPoses.redShootLeft;
    Pose shootControl = FieldPoses.redShootControl;
    Pose parkRight = FieldPoses.redParkRight;
    Pose parkControl = FieldPoses.redParkControl;
    Pose parkLeft = FieldPoses.redParkLeft;



    public PreLoadRight(Alliance alliance) {
        this.a = alliance;
    }

    private Path shootPreLoad() {
        return line(start, shootRight).linear(start, shootRight);
    }

    private Path preLeftShoot() {
        return curve(shootRight, shootControl, shootLeft).linear(shootRight, shootLeft);
    }

    private Path preParkLeft() {
        return curve(shootLeft, parkControl, parkLeft).linear(shootLeft, parkLeft);
    }

    private Path preParkRight() {
        return curve(shootLeft, parkControl, parkRight).linear(shootLeft, parkRight);
    }

    @Override
    public void init() {
        r = new Robot(hardwareMap, a);

        if( a == Alliance.BLUE)
        {
            start = FieldPoses.mirror(start);
            shootRight = FieldPoses.mirror(redShootRight);
            shootLeft = FieldPoses.mirror(redShootLeft);
            shootControl = FieldPoses.mirror(redShootControl);
            parkRight = FieldPoses.mirror(redParkRight);
            parkControl = FieldPoses.mirror(redParkControl);
            parkLeft= FieldPoses.mirror(redParkLeft);
        }

    }

    @Override
    public void loop() {

    }
}
