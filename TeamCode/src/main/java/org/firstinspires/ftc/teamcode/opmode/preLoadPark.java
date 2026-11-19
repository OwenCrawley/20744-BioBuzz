package org.firstinspires.ftc.teamcode.opmode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

@Autonomous(name = "Park (Blocks to Java)")
public class preLoadPark extends LinearOpMode {

    private DcMotor fr;
    private DcMotor br;
    private DcMotor bl;
    private DcMotor fl;
    private DcMotor shooter;
    private DcMotor intakeL;
    private DcMotor intakeR;

    @Override
    public void runOpMode() {
        fr = hardwareMap.get(DcMotor.class, "fr");
        br = hardwareMap.get(DcMotor.class, "br");
        bl = hardwareMap.get(DcMotor.class, "bl");
        fl = hardwareMap.get(DcMotor.class, "fl");
        shooter = hardwareMap.get(DcMotor.class, "shooter");
        intakeL = hardwareMap.get(DcMotor.class, "intakeL");
        intakeR = hardwareMap.get(DcMotor.class, "intakeR");

        fr.setDirection(DcMotor.Direction.REVERSE);
        br.setDirection(DcMotor.Direction.REVERSE);
        waitForStart();
        if (opModeIsActive()) {
            while (opModeIsActive()) {
                telemetry.update();
                bl.setPower(0.5);
                br.setPower(0.5);
                fl.setPower(0.5);
                fr.setPower(0.5);
                shooter.setPower(1);
                sleep(1000);
                bl.setPower(0);
                br.setPower(0);
                fl.setPower(0);
                fr.setPower(0);
                intakeL.setPower(1);
                intakeR.setPower(1);
                sleep(5000);
                bl.setPower(0.5);
                br.setPower(-0.5);
                fl.setPower(0.5);
                fr.setPower(-0.5);
                sleep(2000);
                bl.setPower(1);
                br.setPower(1);
                fl.setPower(1);
                fr.setPower(1);
                sleep(2000);
                bl.setPower(0);
                br.setPower(0);
                fl.setPower(0);
                fr.setPower(0);
                sleep(30000);
            }
        }
    }
}