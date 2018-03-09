package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
/**
 * Created by LH on 3/3/2018.
 */
@Autonomous(name = "Glide Test", group = "Tau")
@Disabled
public class GLIDETEST extends AUTO_METHODS{


    @Override
    public void runOpMode() throws InterruptedException {
        IMUandVu();
        glideFindRightWallBlue();
    }
}
