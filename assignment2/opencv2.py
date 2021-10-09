import cv2 #as cv, time
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
cv2.createTrackbar('hv','Slider',0,255, number)
cv2.createTrackbar('hs','Slider',0,255, number)
cv2.createTrackbar('lh','Slider',0,255, number)
cv2.createTrackbar('lv','Slider',0,255, number)
cv2.createTrackbar('ls','Slider',0,255, number)

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
    hv1 = cv2.getTrackbarPos('hv', 'Slider')
    hs1 = cv2.getTrackbarPos('hs', 'Slider')
    lh1 = cv2.getTrackbarPos('lh', 'Slider')
    lv1 = cv2.getTrackbarPos('lv', 'Slider')
    ls1 = cv2.getTrackbarPos('ls', 'Slider')

    array1 = np.array([hh1, hv1, hs1])
    array2 = np.array([lh1, hv1, ls1])
    
    frame_threshold = cv2.inRange(img1, array2, array1)

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
