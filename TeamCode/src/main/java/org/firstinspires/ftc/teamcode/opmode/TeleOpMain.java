package org.firstinspires.ftc.teamcode.opmode;

import com.pedropathing.follower.ManualDrive;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

import org.firstinspires.ftc.teamcode.config.FieldPoses;
import org.firstinspires.ftc.teamcode.config.Robot;
import org.firstinspires.ftc.teamcode.util.Alliance;


public class TeleOpMain extends OpMode {
    DcMotor bl,br,fl,fr;
    double x,y,pivot;
    Robot r;
    final Alliance a;
    public TeleOpMain(Alliance alliance) {
        this.a = alliance;
    }

    @Override
    public void init() {
        r = new Robot(hardwareMap, a);
        bl = hardwareMap.get(DcMotor.class,"bl");
        br = hardwareMap.get(DcMotor.class,"br");
        fl = hardwareMap.get(DcMotor.class,"fl");
        fr = hardwareMap.get(DcMotor.class,"fr");
        br.setDirection(DcMotorSimple.Direction.REVERSE);
        fr.setDirection(DcMotorSimple.Direction.REVERSE);
    }

    @Override
    public void start() {
        r.s.close();
        r.s.closeHood();
    }

    @Override
    public void loop() {
        x = gamepad1.left_stick_y;
        y = gamepad1.left_stick_x;

        pivot = gamepad1.right_stick_x;

        bl.setPower(x + pivot - y);
        br.setPower(x - pivot + y);
        fl.setPower(x + pivot + y);
        fr.setPower(x - pivot - y);
//        r.f.manual(ManualDrive.fieldCentric(
//                gamepad1.left_stick_y,
//                gamepad1.left_stick_x,
//                gamepad1.right_stick_x,r.f.
//                pose().heading()));

        if (gamepad1.a) r.s.pollen();
        else if (gamepad1.b) r.s.nectar();
        else r.s.closeHood();

        if (gamepad1.right_bumper)
            r.i.spinIn();
        else if (gamepad1.left_bumper)
            r.i.spinOut();
        else
            r.i.idle();

//        r.t.face(r.getShootTarget(), r.f.pose());

        r.periodic();
    }

}
