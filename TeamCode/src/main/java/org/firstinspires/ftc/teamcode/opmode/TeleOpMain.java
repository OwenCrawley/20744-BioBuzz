package org.firstinspires.ftc.teamcode.opmode;

import com.pedropathing.follower.ManualDrive;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.config.FieldPoses;
import org.firstinspires.ftc.teamcode.config.Robot;
import org.firstinspires.ftc.teamcode.util.Alliance;


public class TeleOpMain extends OpMode {
    Robot r;
    final Alliance a;
    public TeleOpMain(Alliance alliance) {
        this.a = alliance;
    }

    @Override
    public void init() {
        r = new Robot(hardwareMap, a);
    }

    @Override
    public void start() {
        r.i.spinIn();
        r.s.close();
        r.s.closeHood();
    }

    @Override
    public void loop() {
        r.f.manual(ManualDrive.fieldCentric(
                gamepad1.left_stick_y,
                gamepad1.left_stick_x,
                gamepad1.right_stick_x,r.f.
                pose().heading()));

        if (gamepad1.a) r.s.pollen();
        else if (gamepad1.b) r.s.nectar();
        else r.s.closeHood();

        r.t.face(r.getShootTarget(), r.f.pose());

        r.periodic();
    }

}
