package org.firstinspires.ftc.teamcode.opmode.auto;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.IMU;
import com.qualcomm.robotcore.hardware.ImuOrientationOnRobot;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.subsystems.Intake;
import org.firstinspires.ftc.teamcode.subsystems.Shooter;

@Config
@Autonomous
public class shootTimeBased extends LinearOpMode {
    public static double shootTime = 3;
    public static double turnToDegree = 25;
    DcMotor bl,br,fl,fr;
    IMU imu;

    Shooter s;
    Intake i;

    ElapsedTime waitTime = new ElapsedTime();

    @Override
    public void runOpMode() {
        bl = hardwareMap.get(DcMotor.class,"bl");
        br = hardwareMap.get(DcMotor.class,"br");
        fl = hardwareMap.get(DcMotor.class,"fl");
        fr = hardwareMap.get(DcMotor.class,"fr");
        imu = hardwareMap.get(IMU.class, "imu");
        imu.initialize((
                new IMU.Parameters(new RevHubOrientationOnRobot(
                        RevHubOrientationOnRobot.LogoFacingDirection.RIGHT,
                        RevHubOrientationOnRobot.UsbFacingDirection.DOWN
                        ))
        ));

        br.setDirection(DcMotorSimple.Direction.REVERSE);
        fr.setDirection(DcMotorSimple.Direction.REVERSE);

        s = new Shooter(hardwareMap);
        i = new Intake(hardwareMap);

        imu.resetYaw();

        while (opModeInInit()) {
            if (gamepad1.dpadUpWasPressed()) shootTime += 0.5;
            if (gamepad1.dpadDownWasPressed()) shootTime -= 0.5;

            telemetry.addLine("Dpad Up/Down to change drive time");
            telemetry.addData("Drive Time", shootTime +" second(s)");
            telemetry.update();
        }

        s.closeHood();

        waitForStart();
        s.close();

        driveForTime(0.3,0,0,0.5);
        while (!s.atTarget()) periodic();
        i.spinIn();
        s.pollen();
        waits(shootTime);
        s.closeHood();
        turnToDegree(0.4,turnToDegree,4);
        driveForTime(0.5,0,0,0.5);
    }
    private void periodic() {
        s.periodic();
        telemetry.addData("Shooter RPM", s.getVelocity());
        telemetry.addData("Shooter Target", s.getTarget());
        telemetry.update();
    }
    private void drive(double x,double y,double pivot) {
        bl.setPower(x + pivot - y);
        br.setPower(x - pivot + y);
        fl.setPower(x + pivot + y);
        fr.setPower(x - pivot - y);
    }
    private void turnToDegree(double speed, double degree, double error) {
        bl.setPower(+speed);
        br.setPower(-speed);
        fl.setPower(+speed);
        fr.setPower(-speed);
        while (Math.abs(imu.getRobotYawPitchRollAngles().getYaw() - degree) <= error ) periodic();
        drive(0,0,0);
    }
    private void driveForTime(double x, double y, double pivot, double seconds) {
        drive(x,y,pivot);
        waitTime.reset();
        waits(seconds);
        drive(0,0,0);
    }
    private void waits(double seconds) {
        while (waitTime.seconds() < seconds) periodic();
    }
}
