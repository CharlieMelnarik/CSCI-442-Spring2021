from controller import *
import cv2
import numpy as np
import time

robot = Robot()
print("I will follow that line if its the last thing I do")

timestep = int(robot.getBasicTimeStep())

leftMotor = robot.getDevice('left wheel motor')
rightMotor = robot.getDevice('right wheel motor')
leftMotor.setPosition(float('inf'))
rightMotor.setPosition(float('inf'))

leftMotor.setVelocity(0.0)
rightMotor.setVelocity(0.0)

cam = robot.getDevice("camera")
cam.enable(timestep)

cv2.namedWindow("External")
cv2.namedWindow("Image")

count = 0

def getHSV(event, x, y, flags, param):
    if event == cv2.EVENT_LBUTTONDOWN:
        print(out[y,x])
        
timeStart = time.perf_counter()

while robot.step(timestep) != -1:

    leftMotor.setVelocity(0.0)
    rightMotor.setVelocity(0.0)
    img = cam.getImage()
    
    width = cam.getWidth()
    height = cam.getHeight()
    temppic = np.frombuffer(img, np.uint8)
    out = np.reshape(temppic, (height,width, 4))
    can = cv2.Canny(out, 40, 80)
   # cv2.resize(can, (260,260))
    img1 = cv2.cvtColor(out, cv2.COLOR_BGR2HSV)
    
    
    arrayH =np.array([50, 50, 50])
    arrayL =np.array([0, 0, 0])
    frame_threshold = cv2.inRange(img1, arrayL, arrayH)
    
    cv2.setMouseCallback("External", getHSV)
    
    kernel = np.ones((5,5), np.uint8)
    frame_threshold = cv2.dilate(frame_threshold, kernel, iterations = 3)

    cv2.imshow("External", frame_threshold)
    
    
    M = cv2.moments(frame_threshold)
    
    x = int(M["m10"] / (M["m00"]+1))
    y = int(M["m01"] / (M["m00"]+1))
        
    jeff = cv2.circle(out, (x,y), 5, (255,0,0), -1)
    
    cv2.imshow("Image", out)
    print (count)

    
    number_white = cv2.countNonZero(frame_threshold)

    if (x == 0 or y == 0):
        leftMotor.setVelocity(.5)
        rightMotor.setVelocity(2.0)
        count += 1
        if (count > 10 and count < 20):
            leftMotor.setVelocity(5.0)
            rightMotor.setVelocity(5.0)
        
        if (count > 20):
            leftMotor.setVelocity(0.0)
            rightMotor.setVelocity(0.0)
            timeStop = time.perf_counter()
            RobotTimer = timeStop - timeStart
            print("The Time the Robot took to complete the course in real time, not Webots time is ", RobotTimer, " seconds")
            break
            
    
    elif  x > 120:
        leftMotor.setVelocity(2.0)
        rightMotor.setVelocity(0.0)
        count = 0
        
    elif x <80:
        leftMotor.setVelocity(0.0)
        rightMotor.setVelocity(2.0)
        count = 0
        
    else:
        leftMotor.setVelocity(6.28)
        rightMotor.setVelocity(6.28)
        count = 0
    
    k = cv2.waitKey(1)
    if k == 27:
        break
    
    
cv2.destroyAllWindows()

