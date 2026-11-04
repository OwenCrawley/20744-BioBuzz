package org.firstinspires.ftc.teamcode.opmode.tests;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.Utility;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

@Utility
public class basicDrive extends LinearOpMode {
    DcMotor bl,br,fl,fr;

    @Override
    public void runOpMode() {
        bl = hardwareMap.get(DcMotor.class,"backLeft");
        br = hardwareMap.get(DcMotor.class,"backRight");
        fl = hardwareMap.get(DcMotor.class,"frontLeft");
        fr = hardwareMap.get(DcMotor.class,"frontRight");

        br.setDirection(DcMotorSimple.Direction.REVERSE);
        fr.setDirection(DcMotorSimple.Direction.REVERSE);

        waitForStart();

        double x,y,pivot;

        while(opModeIsActive())
        {
            x = gamepad1.left_stick_y;
            y = gamepad1.left_stick_x;

            pivot = gamepad1.right_stick_x;

            bl.setPower(x + pivot - y);
            br.setPower(x - pivot + y);
            fl.setPower(x + pivot + y);
            fr.setPower(x - pivot - y);
        }
    }


}
