import cv2
import numpy as np

cap = cv2.VideoCapture(0)
cap1 = cv2.VideoCapture(0)
#cv2.namedWindow("BWimg")
cv2.namedWindow("Image")
cv2.namedWindow("32img")
#cv2.namedWindow("CaptureClone")
#cv2.namedWindow("Video")

print("start")

status, img = cap1.read()
image1 = img.copy()


width, height, channels = img.shape
min_x, min_y = width, height
max_x = max_y = 0

bw = np.zeros((width, height, 1), np.uint8)
imageForAve = np.zeros((width, height, 3), np.float32)
##bright = 35 * np.ones((width, height,3), np.uint8)
##runningColorDepth = img.copy()
##difference = img.copy()


#diff = cv2.absdiff(bw, imageForAve)

status, vid = cap.read()
difference = vid.copy()

while True:

    status, img = cap1.read()

    status, vid = cap.read()

    vid = cv2.add(vid,(35* np.ones((width, height, 3), np.uint8)))

    blur = cv2.blur(vid,(5,5))

    weighted = cv2.accumulateWeighted(blur, imageForAve, 0.5)

    weighted = cv2.convertScaleAbs(weighted)

    weighted = cv2.absdiff(weighted, difference)
    
    difference = weighted.copy()

    gray = cv2.cvtColor(weighted, cv2.COLOR_BGR2GRAY)

    ret,thresh = cv2.threshold(gray,10,255,cv2.THRESH_BINARY)

    blur1 = cv2.blur(thresh,(5,5))

    ret, thresh1 = cv2.threshold(blur1, 12, 255, cv2.THRESH_BINARY)

    contours, hierarchy = cv2.findContours(thresh1, cv2.RETR_CCOMP, cv2.CHAIN_APPROX_SIMPLE)

    cv2.drawContours(imageForAve, contours, -1, (0,0,0), 3)

    #ret,contthresh = cv2.threshold(contours, 1000,1000, cv2.THRESH_BINARY)

    for contour in contours:
        x,y,w,h = cv2.boundingRect(contour)
        cv2.rectangle(img,(x,y),(x+w,y+h),(255,0,0),1)
  
            
    cv2.imshow("Image", img)
    
    k = cv2.waitKey(1)
    if k == 27:
        break

    cv2.imshow("32img", imageForAve)


cv2.destroyAllWindows()
