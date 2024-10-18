// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import java.util.Map;

import edu.wpi.first.networktables.GenericEntry;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInWidgets;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to
 * each mode, as described in the TimedRobot documentation. If you change the
 * name of this class or
 * the package after creating this project, you must also update the
 * build.gradle file in the
 * project.
 */
public class Robot extends TimedRobot {
  private    double leftMotorAdjustment = 0.7;
  private  double rightMotorAdjustment = 0.7;
  private final XboxController xboxController = new XboxController(0);
  private final Timer AutoTimer = new Timer();
  private int dimer = 0;
  private static final String kDefaultAuto = "Default";
  private static final String kCustomAuto = "My Auto";
  private static final String kBinkAuto = "Binkus Auto";
  private String m_autoSelected;
  private final SendableChooser<String> m_chooser = new SendableChooser<>();
  private final GenericEntry nubnerSlider = Shuffleboard.getTab("My Tab").add("My Number", 0).withWidget(BuiltInWidgets.kNumberSlider).withProperties(Map.of("min", 0, "max", 20)).getEntry();

  private final XRPDrivetrain m_drivetrain = new XRPDrivetrain();

  /**
   * This function is run when the robot is first started up and should be used
   * for any
   * initialization code.
   */
  @Override
  public void robotInit() {
    m_chooser.setDefaultOption("Default Auto", kDefaultAuto);
    m_chooser.addOption("My Auto", kCustomAuto);
    m_chooser.addOption("Binkus Auto", kBinkAuto);
    SmartDashboard.putData("Auto choices", m_chooser);
  }

  /**
   * This function is called every 20 ms, no matter the mode. Use this for items
   * like diagnostics
   * that you want ran during disabled, autonomous, teleoperated and test.
   *
   * <p>
   * This runs after the mode specific periodic functions, but before LiveWindow
   * and
   * SmartDashboard integrated updating.
   */
  @Override
  public void robotPeriodic() {
  }

  /**
   * This autonomous (along with the chooser code above) shows how to select
   * between different
   * autonomous modes using the dashboard. The sendable chooser code works with
   * the Java
   * SmartDashboard. If you prefer the LabVIEW Dashboard, remove all of the
   * chooser code and
   * uncomment the getString line to get the auto name from the text box below the
   * Gyro
   *
   * <p>
   * You can add additional auto modes by adding additional comparisons to the
   * switch structure
   * below with additional strings. If using the SendableChooser make sure to add
   * them to the
   * chooser code above as well.
   */
  @Override
  public void autonomousInit() {
    dimer = 0;
    AutoTimer.reset();
    AutoTimer.start();
    m_autoSelected = m_chooser.getSelected();
    // m_autoSelected = SmartDashboard.getString("Auto Selector", kDefaultAuto);
    System.out.println("Auto selected: " + m_autoSelected);
    m_drivetrain.resetEncoders();
  }

  /** This function is called periodically during autonomous. */
  @Override
  public void autonomousPeriodic() {
    dimer++;
    if (dimer == 0) {

    }
    switch (m_autoSelected) {
      case kCustomAuto:
        // Put custom auto code here
        // put stupid auto code here
        if (AutoTimer.get() < 1) {
          m_drivetrain.arcadeDrive(1, 0.0);
        } else if (AutoTimer.get() < 2) {
          m_drivetrain.arcadeDrive(0.0, 10.0);
        } else if (AutoTimer.get() < 3) {
          AutoTimer.reset();
        }
        break;
      case kBinkAuto:
        m_drivetrain.setUltrasonicChannels((nubnerSlider.get().getInteger()),nubnerSlider.get().getInteger()+1);
        System.out.println(m_drivetrain.getUltrasonicDist());
        if (m_drivetrain.getUltrasonicDist() >1)
        {
          driveAutoCalibration(kDefaultPeriod);
        }
        else
        {
          m_drivetrain.arcadeDrive(0, 0);
        }
      break;
      case kDefaultAuto:
      default:


        // Put default auto code here
        break;
    }
  }

  /** This function is called once when teleop is enabled. */
  @Override
  public void teleopInit() {
  }

  /** This function is called periodically during operator control. */
  @Override
  public void teleopPeriodic() {
    //m_drivetrain.arcadeDrive(1, 0);


  }

  /** This function is called once when the robot is disabled. */

  @Override
  public void disabledInit() {
  }

  /** This function is called periodically when disabled. */
  @Override
  public void disabledPeriodic() {
  }

  /** This function is called once when test mode is enabled. */
  @Override
  public void testInit() {
  }

  /** This function is called periodically during test mode. */
  @Override
  public void testPeriodic() {
  }
  public void driveAutoCalibration(double targetDiff){
          double FloorSpeed = 0.03;
      double leftEncoderRate = m_drivetrain.encoderRateTestLeft();
      double rightEncoderRate = m_drivetrain.encoderRateTestRight();
      if (leftEncoderRate!=0&&rightEncoderRate!=0)
      {
        //System.out.println(leftMotorAdjustment+":"+rightMotorAdjustment);

        //System.out.println(leftMotorAdjustment);
        //if (leftEncoderRate < leftTargetSpeed) {leftMotorAdjustment +=0.01;} if (leftEncoderRate > leftTargetSpeed) {leftMotorAdjustment -=0.01;}
        //if (rightEncoderRate < rightTargetSpeed) {rightMotorAdjustment +=0.01;} if (rightEncoderRate > rightTargetSpeed) {rightMotorAdjustment -=0.01;}
        if (rightEncoderRate > leftEncoderRate+targetDiff)
        {
          rightMotorAdjustment -=0.01;
        }
        if (leftEncoderRate > rightEncoderRate+targetDiff)
        {
          leftMotorAdjustment -=0.01;
        }
        if ((leftEncoderRate+rightEncoderRate)/2<FloorSpeed)
        {
          leftMotorAdjustment+=0.01;
          rightMotorAdjustment +=0.01;
        }
      }

      //leftMotorAdjustment -=(leftEncoderRate-leftTargetSpeed);
      //rightMotorAdjustment -=(rightEncoderRate-rightTargetSpeed);
      m_drivetrain.setLeftMotorSpeed(leftMotorAdjustment);
      m_drivetrain.setRightMotorSpeed(rightMotorAdjustment);
  }
}
