package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

/**
 * Created by LH on 11/8/2017.
 */
@Autonomous(name = "Auto Blue Left Middle Glyph", group = "Tau: EXTRA")
//@Disable
public class Auto_BLUE_LEFT_MiddleGlyph extends AUTO_METHODS {

    @Override
    public void runOpMode() throws InterruptedException {
        String vuValue = "";
        int jewelValue = 0;
        IMUandVu();

        //CALL WHATEVER METHODS HERE:
        closeClaw();
        lowerJewelServo();
        sleepTau(500);
        raiseLiftSlightly();
        jewelValue = getJewel();
        if (jewelValue == 10) {
            turnToDegree(0.2,30);
            sleepTau(1200);
            raiseJewelServo();
            sleepTau(150);
            turnToDegree(0.2,-10);
            sleepTau(1000);
        } else if(jewelValue == 3) {
            turnDegree(0.2,-30);
            sleepTau(1200);
            raiseJewelServo();
            sleepTau(150);
            turnToDegree(0.2,-10);
            sleepTau(1000);
        } else {
            telemetry.addData("Jewel", "Unknown");
            updateTelemetry(telemetry);
            raiseJewelServo();
            sleepTau(450);
            turnToDegree(0.2,-10);
            sleepTau(1000);
        }

        vuValue = rightGetVuSMALL();
        telemetry.addData("VuMark", vuValue);
        updateTelemetry(telemetry);
        realign(0.1);
        sleepTau(1000);
        driveForwardStraightDISTANCE(0.3,1.08);
        sleepTauCheck(1400);
        realign(0.15);
        sleepTau(700);
        glideFindRightWall();
        sleepTau(250);
        realign(0.15);
        sleepTau(800);
        glideFindSpotFL(vuValue);
        lowerLiftSlightly();
        sleepTau(2000);




    }

}
