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
        sleepTau(1000);
        raiseLiftSlightly();
        jewelValue = getJewel();
        if (jewelValue == 10){
            turnDegree(0.5,30);
            sleepTau(1200);
            raiseJewelServo();
            sleepTau(150);
            realign(0.5);
            sleepTau(1000);
        }
        else if(jewelValue == 3){
            turnDegree(0.5,-30);
            sleepTau(1200);
            raiseJewelServo();
            sleepTau(150);
            realign(0.5);
            sleepTau(1000);
        }
        else{
            telemetry.addData("Jewel", "Unknown");
            updateTelemetry(telemetry);
            raiseJewelServo();
            sleepTau(450);
        }
        driveForwardStraightDISTANCE(0.8);
        sleepTau(1500);
        realign(0.5);
        sleepTau(200);
        vuValue = leftGetVu();
        telemetry.addData("VuMark", vuValue);
        updateTelemetry(telemetry);
        sleepTau(100);
        realign(0.5);
        sleepTau(1200);
        if(vuValue.equals("CENTER")) {
            driveForwardStraightDISTANCE(0.66);
        }
        else if(vuValue.equals("LEFT")){
            driveForwardStraightDISTANCE(0.95);
        }
        else if(vuValue.equals("RIGHT")){
            driveForwardStraightDISTANCE(0.37);
        }
        else {
            driveForwardStraightDISTANCE(0.66);
        }
        sleepTau(1500);
        turnToDegree(1, 90.0);
        sleepTau(750);
        driveForwardStraightDISTANCE(0.4,0.48);
        sleepTau(1200);
        openClaw();
        sleepTau(250);
        driveBackwardStraightDISTANCE(0.5,0.5);
        sleepTau(1000);
        lowerLiftSlightly();
        sleepTau(150);
        turnToDegree(1,-90);
        sleepTau(1000);
        autoReposition(vuValue);
        sleepTau(150);
        turnToDegree(0.5,-90);
        sleepTau(2500);




    }

}
