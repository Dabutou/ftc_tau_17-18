package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

/**
 * Created by LH on 11/8/2017.
 */
@Autonomous(name = "Auto Blue Left Middle Glyph", group = "Tau")
//@Disable
public class Auto_BLUE_LEFT_MiddleGlyph extends AUTO_METHODS {
    @Override
    public void runOpMode() throws InterruptedException {
        String vuValue = "";
        int jewelValue = 0;
        IMUandVu();

        //CALL WHATEVER METHODS HERE:
        closeClaw();
        getLiftPosition();
        lowerJewelServo();
        sleepTau(1500);
        raiseLiftSlightly();
        jewelValue = getJewel();
        if (jewelValue == 10){
            turnDegree(0.5,30);
            sleepTau(1200);
            raiseJewelServo();
            sleepTau(150);
            realign(0.5);
            sleepTau(500);
        }
        else if(jewelValue == 3){
            turnDegree(0.5,-30);
            sleepTau(1200);
            raiseJewelServo();
            sleepTau(150);
            realign(0.5);
            sleepTau(500);
        }
        else{
            telemetry.addData("Jewel", "Unknown");
            updateTelemetry(telemetry);
            raiseJewelServo();
            sleepTau(450);
        }
        driveForwardStraightDISTANCE(0.7,0.8);
        sleepTau(1500);
        realign(0.5);
        sleepTau(200);
        vuValue = leftGetVu();
        sleepTau(100);
        realign(0.6);
        sleepTau(700);
        if(vuValue.equals("CENTER")) {
            telemetry.addData("VuMark", "CENTER");
            updateTelemetry(telemetry);
            driveForwardStraightDISTANCE(0.7, 0.60);
        }
        else if(vuValue.equals("LEFT")){
            telemetry.addData("VuMark", "LEFT");
            updateTelemetry(telemetry);
            driveForwardStraightDISTANCE(0.7,0.89);
        }
        else if(vuValue.equals("RIGHT")){
            telemetry.addData("VuMark", "RIGHT");
            updateTelemetry(telemetry);
            driveForwardStraightDISTANCE(0.7,0.31);
        }
        else {
            telemetry.addData("VuMark", "UNKNOWN AND PROGRAMMERS BAD");
            updateTelemetry(telemetry);
            driveForwardStraightDISTANCE(0.7,0.60);
        }
        sleepTau(1500);
        turnToDegree(1,90.0);
        sleepTau(750);
        openClaw();
        sleepTau(150);
        driveBackwardStraightDISTANCE(0.5,0.3);
        sleepTau(700);
        lowerLiftSlightly();
        sleepTau(150);
        driveForwardStraightDISTANCE(0.5,0.63);
        sleepTau(1500);
        driveBackwardStraightDISTANCE(0.5,0.30);
        sleepTau(850);
        turnToDegree(1,-90);
        sleepTau(750);
        driveBackwardStraightDISTANCE(0.5,0.1);
        sleepTau(700);
        autoReposition(vuValue);
        sleepTau(1500);


    }
}
