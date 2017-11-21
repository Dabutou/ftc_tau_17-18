package org.firstinspires.ftc.teamcode;


import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.hardware.bosch.JustLoggingAccelerationIntegrator;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.I2cAddr;
import com.qualcomm.robotcore.hardware.I2cDevice;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.matrices.OpenGLMatrix;
import org.firstinspires.ftc.robotcore.external.navigation.Acceleration;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackable;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackables;

/**
 * Created by Lance He on 10/16/2017.
 */

public class HardwareNL {

    // Motor variable names
    public DcMotor frontLeftMotor = null;
    public DcMotor frontRightMotor = null;
    public DcMotor backLeftMotor = null;
    public DcMotor backRightMotor = null;

    // Servo variable names
    public Servo jewelServo = null;

    // Sensor variable names
    public I2cDevice colori2C = null;
    public ModernRoboticsI2cColorSensor2 color = null;
    //VuForia**************************
    OpenGLMatrix lastLocation = null;
    private VuforiaLocalizer vuforia;
    public VuforiaTrackable relicTemplate;
    public VuforiaTrackables relicTrackables;

    //IMU******************************
    private BNO055IMU imu;

    // Other variable names
    HardwareMap hwMap;
    private ElapsedTime period = new ElapsedTime();
    private Orientation angles;
    private Acceleration gravity;

    // Constant variable names
    public static final double LEFT_LIFT_CLOSE = 1;
    public static final double RIGHT_LIFT_CLOSE = 0;

    public HardwareNL()
    {
        hwMap = null;
    }

    public void init(HardwareMap hwMap)
    {

        // Save reference to Hardware map
        this.hwMap = hwMap;
        period.reset();

        // Define Motors
        frontLeftMotor = hwMap.dcMotor.get("left_front");
        frontRightMotor = hwMap.dcMotor.get("right_front");
        backLeftMotor = hwMap.dcMotor.get("left_back");
        backRightMotor = hwMap.dcMotor.get("right_back");

        // Initialize Motors
        frontLeftMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        frontRightMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backLeftMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backRightMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        // ******MAY CHANGE *******  Fix Forward/Reverse under testing
        frontLeftMotor.setDirection(DcMotor.Direction.FORWARD);
        frontRightMotor.setDirection(DcMotor.Direction.FORWARD);
        backLeftMotor.setDirection(DcMotor.Direction.FORWARD);
        backRightMotor.setDirection(DcMotor.Direction.FORWARD);

        frontLeftMotor.setPower(0);
        frontRightMotor.setPower(0);
        backLeftMotor.setPower(0);
        backRightMotor.setPower(0);

        // May use RUN_USING_ENCODERS if encoders are installed
        frontLeftMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        frontRightMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        backLeftMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        backRightMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        frontLeftMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        frontRightMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        backLeftMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        backRightMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        // Define servos
        jewelServo = hwMap.servo.get("jewel_servo");

        // Initialize servos
        jewelServo.scaleRange(0,0.70);
        jewelServo.setDirection(Servo.Direction.FORWARD);
        jewelServo.setPosition(0);

        // Define sensors
        int cameraMonitorViewId = hwMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hwMap.appContext.getPackageName());
        VuforiaLocalizer.Parameters Vuparameters = new VuforiaLocalizer.Parameters(cameraMonitorViewId);
        Vuparameters.vuforiaLicenseKey = "AW8UaHn/////AAAAGSEYBhrOd0rKoinNfInqojQEhkiiEXypTuQY/KgFQY8k+6dx0JDcvHPtVpMjrNdnPY2boqh97cPelF2Si0HqBGdDErR3pyMYpV5evj1cppHIRqDHO0HjNkdbnvnoILWRJtn5+MLWocscbvi2Kbc9PBKxziwcIfl82Dl1t62Y5C8mL3iFF0fAtmTifuB0qp4r1wekhd9hScm6NTybtyBEk9QduDH8kyMOW56geGGhot9Oq+A/wk6spIv8NCQLJHgD30pj9TrtVBmWmA59x/pT9nBKBuI/ah1b+SZ5cSm5CTPv+Gra53m3y4k/usz66j8rCakKdj5DDg6+Ivpc3V6uQxDNpzh0HBE+x/zEGr7dMFRz";
        Vuparameters.cameraDirection = VuforiaLocalizer.CameraDirection.BACK;
        this.vuforia = ClassFactory.createVuforiaLocalizer(Vuparameters);
        relicTrackables = this.vuforia.loadTrackablesFromAsset("RelicVuMark");
        relicTemplate = relicTrackables.get(0);
        relicTemplate.setName("relicVuMarkTemplate"); // can help in debugging; otherwise not necessary

        BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();
        parameters.angleUnit           = BNO055IMU.AngleUnit.DEGREES;
        parameters.accelUnit           = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC;
        parameters.calibrationDataFile = "BNO055IMUCalibration.json"; // see the calibrafion sample opmode
        parameters.loggingEnabled      = true;
        parameters.loggingTag          = "IMU";
        parameters.accelerationIntegrationAlgorithm = new JustLoggingAccelerationIntegrator();

        imu = hwMap.get(BNO055IMU.class, "imu");
        colori2C = hwMap.i2cDevice.get("color");
        color = new ModernRoboticsI2cColorSensor2(colori2C.getI2cController(),colori2C.getPort());
        color.setI2cAddress(I2cAddr.create8bit(0x4C));

        // Initialize sensors
        imu.initialize(parameters);

    }

    public void initTeleOp(HardwareMap hwMap){
        // Save reference to Hardware map
        this.hwMap = hwMap;
        period.reset();

        // Define Motors
        frontLeftMotor = hwMap.dcMotor.get("left_front");
        frontRightMotor = hwMap.dcMotor.get("right_front");
        backLeftMotor = hwMap.dcMotor.get("left_back");
        backRightMotor = hwMap.dcMotor.get("right_back");

        // Initialize Motors
        frontLeftMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        frontRightMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backLeftMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backRightMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        // ******MAY CHANGE *******  Fix Forward/Reverse under testing
        frontLeftMotor.setDirection(DcMotor.Direction.FORWARD);
        frontRightMotor.setDirection(DcMotor.Direction.FORWARD);
        backLeftMotor.setDirection(DcMotor.Direction.FORWARD);
        backRightMotor.setDirection(DcMotor.Direction.FORWARD);

        frontLeftMotor.setPower(0);
        frontRightMotor.setPower(0);
        backLeftMotor.setPower(0);
        backRightMotor.setPower(0);

        // May use RUN_USING_ENCODERS if encoders are installed

        frontLeftMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        frontRightMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        backLeftMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        backRightMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        // Define servos
        jewelServo = hwMap.servo.get("jewel_servo");

        // Initialize servos
        jewelServo.scaleRange(0,0.70);
        jewelServo.setDirection(Servo.Direction.FORWARD);
        jewelServo.setPosition(0);

        // Define sensors
        BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();
        parameters.angleUnit           = BNO055IMU.AngleUnit.DEGREES;
        parameters.accelUnit           = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC;
        parameters.calibrationDataFile = "BNO055IMUCalibration.json"; // see the calibrafion sample opmode
        parameters.loggingEnabled      = true;
        parameters.loggingTag          = "IMU";
        parameters.accelerationIntegrationAlgorithm = new JustLoggingAccelerationIntegrator();

        imu = hwMap.get(BNO055IMU.class, "imu");
        colori2C = hwMap.i2cDevice.get("color");
        color = new ModernRoboticsI2cColorSensor2(colori2C.getI2cController(),colori2C.getPort());
        color.setI2cAddress(I2cAddr.create8bit(0x4C));

        // Initialize sensors
        imu.initialize(parameters);
    }

    public double getTime(){
        return period.time();
    }

    public BNO055IMU getImu(){
        return imu;
    }
}
