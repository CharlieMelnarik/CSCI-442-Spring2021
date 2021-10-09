from controller import *
import cv2
import numpy as np

robot = Robot()
print("I will follow that orange if its the last thing I do")

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

def getHSV(event, x, y, flags, param):
    if event == cv2.EVENT_LBUTTONDOWN:
        print(out[y,x])

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
    
    
    arrayH =np.array([20, 208, 240])
    arrayL =np.array([5, 140, 60])
    frame_threshold = cv2.inRange(img1, arrayL, arrayH)
    
    cv2.setMouseCallback("External", getHSV)
    
    kernel = np.ones((5,5), np.uint8)
    frame_threshold = cv2.dilate(frame_threshold, kernel, iterations = 3)

    cv2.imshow("External", frame_threshold)
    
    #contours, hierarchy = cv2.findContours(frame_threshold, cv2.RETR_TREE,
        #cv2.CHAIN_APPROX_NONE)
        
    #print(contours)
    
    M = cv2.moments(frame_threshold)
    
    x = int(M["m10"] / (M["m00"]+1))
    y = int(M["m01"] / (M["m00"]+1))
        
    jeff = cv2.circle(out, (x,y), 5, (255,0,0), -1)
    
    #cv2.drawContours(out, contours,-1, (0,255,0), 3)
    cv2.imshow("Image", out)

    
    number_white = cv2.countNonZero(frame_threshold)
    total_pixels = width * height
    number_black = total_pixels - number_white
    percentage = ((number_black - number_white) / number_black) * 100
    #print(percentage)
    #print(number_white)
    variable = 0
    
    if  x > 220:
        leftMotor.setVelocity(2.0)
        rightMotor.setVelocity(0.0)
        
    elif x <40:
        leftMotor.setVelocity(0.0)
        rightMotor.setVelocity(2.0)
    
    elif number_white < 5500 and number_white > 3000:
        leftMotor.setVelocity(2.0)
        rightMotor.setVelocity(2.0)
        
    elif number_white < 3000 and number_white > 1000:
        leftMotor.setVelocity(4.0)
        rightMotor.setVelocity(4.0)
        
    elif number_white < 1000 and number_white > 100:
        leftMotor.setVelocity(5.0)
        rightMotor.setVelocity(5.0)
        
    elif number_white > 6000 and number_white < 7500:
        leftMotor.setVelocity(-3.0)
        rightMotor.setVelocity(-3.0)
        
    elif number_white >7500:
        leftMotor.setVelocity(-6.8)
        rightMotor.setVelocity(-6.8)
        
    elif number_white < 100:
        leftMotor.setVelocity(0.0)
        rightMotor.setVelocity(5.0)
    
    k = cv2.waitKey(1)
    if k == 27:
        break
    
    
cv.destroyAllWindows()
