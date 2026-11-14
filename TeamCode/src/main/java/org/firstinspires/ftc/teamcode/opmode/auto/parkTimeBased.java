package org.firstinspires.ftc.teamcode.opmode.auto;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

@Config
@Autonomous
public class parkTimeBased extends LinearOpMode {
    public static double driveTime = 1;
    DcMotor bl,br,fl,fr;

    @Override
    public void runOpMode() {
        bl = hardwareMap.get(DcMotor.class,"bl");
        br = hardwareMap.get(DcMotor.class,"br");
        fl = hardwareMap.get(DcMotor.class,"fl");
        fr = hardwareMap.get(DcMotor.class,"fr");

        br.setDirection(DcMotorSimple.Direction.REVERSE);
        fr.setDirection(DcMotorSimple.Direction.REVERSE);

        while (opModeInInit()) {
            if (gamepad1.dpadUpWasPressed()) driveTime += 0.5;
            if (gamepad1.dpadDownWasPressed()) driveTime -= 0.5;

            telemetry.addLine("Dpad Up/Down to change drive time");
            telemetry.addData("Drive Time", driveTime+" second(s)");
            telemetry.update();
        }

        waitForStart();

        drive(0.3,0,0);
        sleep( (int)(driveTime *1000) );
        drive(0,0,0);
    }

    private void drive(double x,double y,double pivot) {
        bl.setPower(x + pivot - y);
        br.setPower(x - pivot + y);
        fl.setPower(x + pivot + y);
        fr.setPower(x - pivot - y);
    }
}
