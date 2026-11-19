package org.firstinspires.ftc.teamcode.config;

import com.pedropathing.math.Pose;

public class FieldPoses {
    private static double TS = 24.0; // Tile Size
    private static double BOT_LENGTH = 15, BOT_WIDTH = 14;
    private static double R = Math.PI/2;

    public static Pose rightStartRed = new Pose(TS*-1+BOT_WIDTH/2, TS*-3+BOT_LENGTH/2, R);
    public static Pose leftStartRed = new Pose(TS*-0.5, TS*3-BOT_LENGTH/2, -R);
    public static Pose hiveRed = new Pose(TS*-0.6, 0);
    public static Pose rightHiveRed = new Pose(hiveRed.x(), TS*-0.6);
    public static Pose leftHiveRed = new Pose(hiveRed.x(), TS*0.6);

    public static Pose redParkRight = new Pose(TS*-3+BOT_LENGTH/2,TS*1,0);
    public static Pose redParkControl = new Pose(TS*-1.5+BOT_LENGTH/2, TS, R/-2);
    public static Pose redParkLeft = new Pose(TS*-3+BOT_LENGTH/2,TS*2,0);

    public static Pose redShootRight = new Pose(TS*-.5, TS*-2,R);
    public static Pose redShootControl = new Pose(TS*-.5, TS, 2*R);
    public static Pose redShootLeft = new Pose(TS*-.5, TS*2,-R);



    public static Pose mirror(Pose pose) {
        return new Pose(pose.x()*-1,pose.y()*-1,pose.heading()+180);
    }

}
