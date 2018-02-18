package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;

/**
 * Created by LH on 2/1/2018.
 */
@Autonomous(name = "Auto TEST", group = "TEST")
@Disabled
public class Auto_TEST extends AUTO_METHODS{

    @Override
    public void runOpMode() throws InterruptedException {
        IMUandVu();
        /*
        turnToDegree(1,0);
        sleepTau(2500);
        turnToDegree(1,90);
        sleepTau(2500);
        turnToDegree(1,-45);
        sleepTau(2500);
        turnToDegree(1, 180);
        sleepTau(2500);
        turnToDegree(1,0);
        sleepTau(2500);
        turnDegree(1,-90);
        sleepTau(2500);
        turnDegree(1,90);
        sleepTau(2500);*/
        ////getPositions();
        openClaw();
        sleepTau(150);
        closeClaw();
        sleepTau(200);
        pokeClaw();
        sleepTau(500);
        //getPositions();
        /*driveForwardStraightDISTANCE(1);
        sleepTauCheck(2500);
        getImuHeading();
        driveBackwardStraightDISTANCE(1);
        sleepTauCheck(2500);
        //getPositions();
        getImuHeading();
        getPositions();
        driveForwardStraightDISTANCE(0.5,1);
        sleepTauCheck(2500);
        getImuHeading();
        getPositions();
        driveBackwardStraightDISTANCE(0.5,1);
        sleepTauCheck(2500);
        getImuHeading();
        //getPositions();
        getPositions();
        driveForwardStraightDISTANCE(0.25,1);
        sleepTauCheck(2500);
        getImuHeading();
        getPositions();
        driveBackwardStraightDISTANCE(0.25,1);
        sleepTauCheck(2500);
        getImuHeading();
        //getPositions();
        getPositions();
        driveLeftStraightDISTANCE(1);
        sleepTauCheck(2500);
        getImuHeading();
        getPositions();
        driveRightStraightDISTANCE(1);
        sleepTauCheck(2500);
        getImuHeading();
        //getPositions();
        getPositions();
        driveLeftStraightDISTANCE(0.5,1);
        sleepTauCheck(2500);
        getImuHeading();
        getPositions();
        driveRightStraightDISTANCE(0.5,1);
        sleepTauCheck(2500);
        getImuHeading();
        getPositions();
        //getPositions();
        driveLeftStraightDISTANCE(0.25,1);
        sleepTauCheck(2500);
        getImuHeading();
        getPositions();
        driveRightStraightDISTANCE(0.25,1);
        sleepTauCheck(2500);
        getImuHeading();*/
        //getPositions();
        //getPositions();
        //glideFindSpot();
        glideFindRightWall();


        /*driveLeftStraightDISTANCE(0.5,10);
        sleepTauCheck(20000);
*/
        /*turnDegree(1,180);
        sleepTau(2000);
        getImuHeading();
        turnDegree(1,180);
        sleepTau(2000);
        getImuHeading();
        realign(1);
        sleepTau(1000);
        turnDegree(1,-180);
        sleepTau(2000);
        turnDegree(1,-180);
        sleepTau(2000);
        realign(1);
        sleepTau(1000);
*/
    }
}
