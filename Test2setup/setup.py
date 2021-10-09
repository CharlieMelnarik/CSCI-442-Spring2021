import cv2 
import numpy as np


cap = cv2.VideoCapture(0)
cv2.namedWindow("Video")
cap1 = cv2.VideoCapture(0)
cv2.namedWindow("Standard")
cv2.namedWindow("Slider")
cv2.namedWindow("Video1")

def number(value):
    print(value)

cv2.createTrackbar('hh','Slider',0,255, number)

kernel = np.ones((5,5), np.uint8)

def getHSV(event, x, y, flags, param):
    if event == cv2.EVENT_LBUTTONDOWN:
        print(img[y,x])

print("start")
while True:
    status, img = cap1.read()
    status, img1 = cap.read()
    #img = cv2.Canny(img, 100, 200)
    img = cv2.cvtColor(img, cv2.COLOR_BGR2HSV)


    hh1 = cv2.getTrackbarPos('hh', 'Slider')


    array1 = np.array([hh1, hh1, hh1])
    array2 = np.array([0,1,2])
    
    frame_threshold = cv2.inRange(img1,array2, array1)

    img_erode = cv2.erode(frame_threshold, kernel, iterations = 3)
    img_dilate = cv2.dilate(img_erode, kernel, iterations = 3)
    

    cv2.imshow("Video", img)
    cv2.imshow("Standard", img1)
    cv2.imshow("Video1", img_dilate)
    cv2.setMouseCallback("Video", getHSV)

    k = cv2.waitKey(1)
    if k == 27:
        break

    cv2.imshow("Video", img)

cv2.destroyAllWindows()
