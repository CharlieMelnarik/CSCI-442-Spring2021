from controller import *
import cv2

robot = Robot()
print("Hello")

timestep = int(robot.getBasicTimeStep())

leftMotor = robot.getDevice('left wheel motor')
rightMotor = robot.getDevice('right wheel motor')
leftMotor.setPosition(float('inf'))
rightMotor.setPosition(float('inf'))

while robot.step(timestep) != 9:
    pass