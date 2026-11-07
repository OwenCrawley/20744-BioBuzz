package org.firstinspires.ftc.teamcode.opmode.tests;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.pedropathing.follower.Follower;
import com.pedropathing.math.Pose;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.eventloop.opmode.Utility;

import org.firstinspires.ftc.teamcode.config.FieldPoses;
import org.firstinspires.ftc.teamcode.pedro.Constants;
import org.firstinspires.ftc.teamcode.subsystems.Turret;

@Utility
public class TurretTest extends LinearOpMode {
    Turret turret;
    Follower drive;

    boolean tOnPressed = false, tOffPressed = false;
    boolean targetPressed = false;
    double looptime = 0;

    @Override
    public void runOpMode() {
        drive = Constants.create(hardwareMap);
        turret = new Turret(hardwareMap);
        turret.off();


//        Drawing.init();

        telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());
        while (opModeInInit()) {
            if (gamepad1.x) turret.resetTurret();
            telemetry.addData("RPT", turret.getRPT());
            telemetry.addData("Turret Angle",turret.getYaw());
            telemetry.addData("Get Turret", turret.getTurret());
            telemetry.update();
        }
        Pose robotPose;
        waitForStart();
        while (opModeIsActive()) {
            robotPose = drive.pose();
            /*{
                Drawing.drawDebug(drive);
            }*/

            if (gamepad1.a && !tOnPressed)
                turret.on();
            tOnPressed = gamepad1.a;
            if (gamepad1.b && !tOffPressed)
                turret.off();
            tOffPressed = gamepad1.b;

            if (gamepad1.x && !targetPressed)
                turret.setYaw(-Math.PI/2);
            targetPressed = gamepad1.x;

            if (gamepad1.y) turret.face(FieldPoses.hiveRed, robotPose);

            turret.periodic();
            drive.update();
            telemetry.addLine("Controls:");
            telemetry.addLine("On/Off : A/B");
            telemetry.addLine("90 degrees : X");
            telemetry.addLine("Face Hive : Y (Hold)");
            telemetry.addLine();
            telemetry.addData("Robot Pose", drive.pose());
            telemetry.addData("Turret Angle",turret.getYaw());
            telemetry.addData("Get Turret", turret.getTurret());
            telemetry.addData("Turret Target", turret.getTurretTarget());
            double loop = System.nanoTime();
            telemetry.addData("hz ", 1000000000 / (loop - looptime));
            looptime = loop;
            telemetry.update();
        }
    }
}
