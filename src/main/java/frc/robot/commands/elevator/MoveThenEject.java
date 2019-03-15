/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands.elevator;

import edu.wpi.first.wpilibj.command.CommandGroup;
import frc.robot.CarriageLevel;
import frc.robot.commands.DoNothing;
import frc.robot.commands.carriage.CarriageClose;
import frc.robot.commands.carriage.CarriageOpen;
import frc.robot.commands.driveTrain.DriveToDistanceTimed;
import frc.robot.subsystems.Carriage;
import frc.robot.subsystems.DriveTrain;
import frc.robot.subsystems.Elevator;
import frc.robot.subsystems.Intake;

public class MoveThenEject extends CommandGroup {

  public MoveThenEject(Intake intake, Elevator elevator, Carriage carriage, CarriageLevel level, double ejectTimeout, DriveTrain driveTrain) {

    addSequential(new MoveToSetpoint(elevator, level, carriage, intake));
    addSequential(new CarriageOpen(carriage, intake));
    addSequential(new DoNothing(ejectTimeout));
    addSequential(new DriveToDistanceTimed(driveTrain, carriage, 0.5, -0.2, false));
    addSequential(new CarriageClose(carriage));
    addSequential(new MoveToSetpoint(elevator, CarriageLevel.Zero, carriage, intake));

  }

}
