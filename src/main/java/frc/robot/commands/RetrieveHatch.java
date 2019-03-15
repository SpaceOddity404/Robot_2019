/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import edu.wpi.first.wpilibj.command.CommandGroup;
import frc.robot.CarriageLevel;
import frc.robot.commands.carriage.CloseBeak;
import frc.robot.commands.carriage.OpenBeak;
import frc.robot.commands.driveTrain.DriveToDistanceTimed;
import frc.robot.commands.elevator.MoveToSetpoint;
import frc.robot.subsystems.Carriage;
import frc.robot.subsystems.DriveTrain;
import frc.robot.subsystems.Elevator;
import frc.robot.subsystems.Intake;

public class RetrieveHatch extends CommandGroup {

  public RetrieveHatch(Intake intake, Elevator elevator, Carriage carriage, DriveTrain driveTrain) {
    
    addSequential(new OpenBeak(carriage));
    addSequential(new CloseBeak(carriage));
    addSequential(new MoveToSetpoint(elevator, CarriageLevel.HatchPickup, carriage, intake));
    addSequential(new DriveToDistanceTimed(driveTrain, carriage, 0.5, -0.3, true));
    addSequential(new MoveToSetpoint(elevator, CarriageLevel.Zero, carriage, intake));
    
  }
}
