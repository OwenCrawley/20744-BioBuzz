package org.firstinspires.ftc.teamcode.opmode;

import com.acmerobotics.dashboard.config.Config;
import com.pedropathing.follower.ManualDrive;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.util.Range;

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
    double lastX = 0, lastPivot = 0;
    public static double slew = 0.08;

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
            ll.switchToAlign();
            double targetPivot = ll.AlignHive();   // turn power the Limelight wants
            double targetX = -ll.alignDrive();     // drive power the Limelight wants

            pivot = lastPivot + Range.clip(targetPivot - lastPivot, -slew, slew);
            x = lastX + Range.clip(targetX - lastX, -slew, slew);
        }
        lastPivot = pivot;
        lastX = x;


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
        if (ll.l.getLatestResult() != null)
        {
            telemetry.addData("tx", ll.l.getLatestResult().getTx());
            telemetry.addData("ty", ll.l.getLatestResult().getTy());
            telemetry.addData("ta", ll.l.getLatestResult().getTa());
            }


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
