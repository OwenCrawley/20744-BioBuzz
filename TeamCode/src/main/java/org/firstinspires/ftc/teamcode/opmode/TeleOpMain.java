package org.firstinspires.ftc.teamcode.opmode;

import com.acmerobotics.dashboard.config.Config;
import com.pedropathing.follower.ManualDrive;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

import org.firstinspires.ftc.teamcode.config.FieldPoses;
import org.firstinspires.ftc.teamcode.config.Robot;
import org.firstinspires.ftc.teamcode.subsystems.LLSimple;
import org.firstinspires.ftc.teamcode.util.Alliance;

@Config
public class TeleOpMain extends OpMode {
    DcMotor bl,br,fl,fr;
    double x,y,pivot;
    Robot r;
    LLSimple ll;
    boolean bWasPressed = false;

    final Alliance a;

    public static double pivotSpeed = 0.5;

    public TeleOpMain(Alliance alliance) {
        this.a = alliance;
    }

    @Override
    public void init() {
        r = new Robot(hardwareMap, a);
        ll = new LLSimple(hardwareMap, a);
        bl = hardwareMap.get(DcMotor.class,"bl");
        br = hardwareMap.get(DcMotor.class,"br");
        fl = hardwareMap.get(DcMotor.class,"fl");
        fr = hardwareMap.get(DcMotor.class,"fr");
        br.setDirection(DcMotorSimple.Direction.REVERSE);
        fr.setDirection(DcMotorSimple.Direction.REVERSE);

        br.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        fr.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        fl.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        bl.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

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

        pivot = gamepad1.right_stick_x * pivotSpeed;

        if (gamepad1.b && ll.hiveUp()) {
            pivot = ll.AlignHive();      // turns until tx is about 0
            x = -ll.alignDrive();        // drives until ty is about tyTarget (x is inverted like your stick)
        }

        if (gamepad1.b && ll.isHoldSet() && ll.hiveUp()) {
            pivot = ll.holdYawTurn();
            double[] h = ll.holdPosition();
            x = -h[0];
            y = h[1];
        }

        bl.setPower(x - pivot + y);
        br.setPower(x + pivot - y);
        fr.setPower(x + pivot + y);
        fl.setPower(x - pivot - y);
//        r.f.manual(ManualDrive.fieldCentric(
//                gamepad1.left_stick_y,
//                gamepad1.left_stick_x,
//                gamepad1.right_stick_x,r.f.
//                pose().heading()));

        if (gamepad1.a) r.s.pollen();
        else if (gamepad1.b) r.s.nectar();
        else r.s.closeHood();

        if (gamepad1.right_bumper || gamepad1.a )
            r.i.spinIn();
        else if (gamepad1.left_bumper)
            r.i.spinOut();
        else
            r.i.spinIdle();

//        if (gamepad1.b)
//        {
//            ll.startHold();
//        } else {
//            ll.stopHold();
//        }
//        bWasPressed = gamepad1.b;
//
//        if (gamepad1.b && ll.hiveUp()) {
//            pivot = ll.holdYawTurn();
//            double[] h = ll.holdPosition();
//            x = -h[0];
//            y = h[1];
//        }

        telemetry.addData("B Pressed", bWasPressed);
        telemetry.addData("Drive KP", ll.driveKP);
        telemetry.addData("Strafe KP", ll.strafeKP);
        if (ll.l.getLatestResult() != null)
        {
            telemetry.addData("tx", ll.l.getLatestResult().getTx());
            telemetry.addData("ty", ll.l.getLatestResult().getTy());
            telemetry.addData("ta", ll.l.getLatestResult().getTa());
            }


        double[] hh = ll.holdPosition();
        telemetry.addData("B pressed", gamepad1.b);
        telemetry.addData("Hive tag seen", ll.hiveUp());
        telemetry.addData("Detected ID", ll.detectedID());
        telemetry.addData("Turn out", ll.AlignHive());
        telemetry.addData("Drive out", ll.alignDrive());




        telemetry.update();


//        r.t.face(r.getShootTarget(), r.f.pose());

        r.periodic();
    }

}
