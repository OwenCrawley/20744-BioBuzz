package org.firstinspires.ftc.teamcode.opmode.tests;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.eventloop.opmode.Utility;

import org.firstinspires.ftc.teamcode.subsystems.Shooter;

@Utility
public class JustShooterPID extends LinearOpMode {
    FtcDashboard dashboard = FtcDashboard.getInstance();
    Shooter shooter;

    boolean farPressed = false, closePressed = false, onPressed = false, offPressed = false;
    @Override
    public void runOpMode() {
        shooter = new Shooter(hardwareMap);

        telemetry = new MultipleTelemetry(telemetry, dashboard.getTelemetry());
        waitForStart();
        while (opModeIsActive()) {
            if (gamepad1.dpad_up && !closePressed) {
                shooter.close(); shooter.closeHood();
            }closePressed = gamepad1.dpad_up;
            if (gamepad1.dpad_down && !farPressed) {
                shooter.far(); shooter.pollen();
            }farPressed = gamepad1.dpad_down;
            if (gamepad1.x && !onPressed)
                shooter.on();
            onPressed = gamepad1.x;
            if (gamepad1.y && !offPressed)
                shooter.off();
            offPressed = gamepad1.y;

            shooter.periodic();

            telemetry.addLine("Controls:");
            telemetry.addLine("On/Off : X/Y");
            telemetry.addLine("closed? : dpad_up");
            telemetry.addLine("pollen? : dpad_down");
            telemetry.addLine();
            telemetry.addData("Shooter Vel", shooter.getVelocity());
            telemetry.addData("Shooter Target Vel", shooter.getTarget());
            telemetry.addData("Shooter Activated", shooter.isActivated());
            telemetry.update();
        }
    }
}
