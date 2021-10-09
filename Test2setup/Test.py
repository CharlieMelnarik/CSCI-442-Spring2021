import cv2
import numpy as np

#0 = GRAYSCALE
img = cv2.imread('imageface.jpg', 1)
imgf = cv2.imread('pictwo.jpg', 1)


#(0,0) = starting coordinates
#(511,511) = ending coordinates
#(255,0,0) = color blue
#5 = line thickness, -1 to fill shape
##cv2.line(img,(0,0),(511,511),(255,0,0),5)
##
##cv2.rectangle(img,(0,0),(511,511),(0,255,0),5)
##
##cv2.circle(img,(255,255), 255, (0,0,255),5)


################# DRAW POLYGONS, NPARRAY IS THE DIFFERENT POINTS OF THE SHAPE
##pts = np.array([[100,50],[200,300],[700,200],[500,100]], np.int32)
##pts = pts.reshape((-1,1,2))
##cv2.polylines(img,[pts],True,(0,255,255))

######################### ADD TEXT TO THE PICTURE
font                   = cv2.FONT_HERSHEY_SIMPLEX
bottomLeftCornerOfText = (10,500)
fontScale              = 1
fontColor              = (255,255,255)
lineType               = 2

cv2.putText(img,'Hello World!', 
    bottomLeftCornerOfText, 
    font, 
    fontScale,
    fontColor,
    lineType)


##########################CONVERT TO HSV
#img = cv2.cvtColor(img, cv2.COLOR_BGR2HSV)

##################CONVERT TO GRAY     FACE AND EYE DETECT MUST BE GRAY FIRST
gray = cv2.cvtColor(img, cv2.COLOR_BGR2GRAY)


###########################DETECT FACES
face = cv2.CascadeClassifier('haarcascade_frontalface_default.xml')
faces = face.detectMultiScale(gray, 1.1, 4)
print(faces)
for (x,y,w,h) in faces:
    cv2.rectangle(img, (x,y), (x+w, y+h), (255,0,0),2)




########################DETECT EYES
eye = cv2.CascadeClassifier('haarcascade_eye.xml')
eyes = eye.detectMultiScale(gray, 1.1, 6)
for (x,y,w,h) in eyes:
    cv2.rectangle(img, (x,y), (x+w, y+h), (255,0,0), 2)

############################# ADD AND SUBTRACT IMAGES, IMAGES MUST BE SAME SIZE
#img = cv2.addWeighted(img, 0.5, imgf, 0.4, 0)
#img = cv2.subtract(img, imgf)
#img = cv2.absdiff(img, imgf)

################################### COPY PART OF AN IMAGE AND
################################### PUT IT IN ANOTHER PART OF THE IMAGE
ball = img[349:88, 161:161]
img[193:103, 200:200] = ball
#cv2.imshow('image', imgf)


cv2.imshow('Charlie Melnarik', img)
