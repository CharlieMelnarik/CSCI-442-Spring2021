
import cv2
import numpy as np

img = cv2.imread('mugshot.jpg', 1)
lefteyeimg = cv2.imread('lefteye.png', 1)
righteyeimg = cv2.imread('righteye.png', 1)


newimg = img.copy()


font                   = cv2.FONT_HERSHEY_SIMPLEX
bottomLeftCornerOfText = (10,500)
fontScale              = 1
fontColor              = (255,0,0)
lineType               = 2

cv2.putText(newimg,'CSCI 442 - Charlie Melnarik', 
    bottomLeftCornerOfText, 
    font, 
    fontScale,
    fontColor,
    lineType)


gray = cv2.cvtColor(newimg, cv2.COLOR_BGR2GRAY)



face = cv2.CascadeClassifier('haarcascade_frontalface_default.xml')
faces = face.detectMultiScale(gray, 1.1, 4)
print(faces)
for (x,y,w,h) in faces:
    cv2.rectangle(newimg, (x,y), (x+w, y+h), (255,0,0),2)



eye = cv2.CascadeClassifier('haarcascade_eye.xml')
eyes = eye.detectMultiScale(gray, 1.1, 125)
for (x,y,w,h) in eyes:
    cv2.rectangle(newimg, (x,y), (x+w, y+h), (0,255,0), 2)
    print(x)
    print(y)


newimg[243:333, 157:247] = lefteyeimg
newimg[243:333, 273:363] = righteyeimg


cv2.imshow('Charlie Melnarik', newimg)

