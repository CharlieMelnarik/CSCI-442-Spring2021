import numpy as np
import cv2
import pickle

face_cascade = cv2.CascadeClassifier('face.xml')

recognizer = cv2.face.LBPHFaceRecognizer_create()
recognizer.read("trainer.yml")
labels = {}
with open ("labels.pickle", 'rb') as f:
    of_labels = pickle.load(f)
    labels = {v:k for k, v in of_labels.items()}

cv2.namedWindow("Image")

img = cv2.imread("TheThree.jpeg", 1)

gray = cv2.cvtColor(img, cv2.COLOR_BGR2GRAY)

faces = face_cascade.detectMultiScale(gray, 1.0556, 5)
#print("faces")

for (x,y,w,h) in faces:
    cv2.rectangle(img, (x,y), (x+w, y+h), (255,0,0),2)
    roi_gray = gray[y:y+h, x:x+w]
    roi_color = img[y:y+h, x:x+w]
    id, conf = recognizer.predict(roi_gray)
    print(conf)
    if conf >= 45 and conf <= 95:
        font                   = cv2.FONT_HERSHEY_SIMPLEX
        bottomLeftCornerOfText = (x,y)
        fontScale              = 1
        fontColor              = (255,255,255)
        lineType               = 2

        cv2.putText(img,labels[id], 
            bottomLeftCornerOfText, 
            font, 
            fontScale,
            fontColor,
            lineType)

        
##        print("id",id)
        print(labels[id])
        

cv2.imshow('Image', img)
