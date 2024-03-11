from keras_facenet import FaceNet
import numpy as np
from PIL import Image
import os
import json
import argparse
import requests

# Instantiate the FaceNet model
face_encoder = FaceNet()


def preprocess_image(image_path):
    # Load image and resize to expected shape
    img = Image.open(image_path)
    img = img.resize((160, 160))
    img = np.array(img)
    return img

def compare_faces(img1, img2):
    # Preprocess the images
    img1_processed = preprocess_image(img1)
    img2_processed = preprocess_image(img2)

    # Get the embeddings for the images
    img1_embedding = face_encoder.embeddings(np.expand_dims(img1_processed, axis=0))
    img2_embedding = face_encoder.embeddings(np.expand_dims(img2_processed, axis=0))

    # Compute distance between the two embeddings
    dist = np.linalg.norm(img1_embedding - img2_embedding)
    return dist

def main(image_path,gender, user_id):
    # img = cv2.imread(image_path)
    # cv2.imshow('image', img)
    # cv2.waitKey(0)
    # cv2.destroyAllWindows()
    distanceMap = dict()
    distanceMapAvg = dict()
    root_dir = 'StarsM'
    if(gender == 'F'):
        root_dir = 'StarsF'

    iter1 = iter(os.walk(root_dir))
    root_, dirs, files_ = next(iter1)
    testImg = os.path.join(image_path, 'face.jpg')

    for star in dirs:
        print(별)
        iterFile = iter(os.walk(os.path.join(root_dir,star)))
        star_, dirs_, files = next(iterFile)
        distArr = []
        for file in files:
            img_path = os.path.join(root_dir,star,file)
            print(img_path)
            dist = float(compare_faces(testImg,img_path))
            distArr.append(dist)
        print(distArr)
        distanceMap[star] = sum(distArr) / len(distArr)
        distanceMapAvg[star] = distanceMap[star]
        distanceMap[star + '_arr'] = distArr
        # print(distanceMap)

    json_data = json.dumps(distanceMapAvg)
    url = "http://localhost:9999/looks/" + user_id
    headers = {'Content-Type':'application/json'}
    response = requests.post(url, data=json_data, headers=headers)

    if response.status_code == 200:
        with open(os.path.join(image_path,'distanceMap.json'), 'w', encoding='utf-8') as f:
            json.dump({'distanceMap':distanceMap}, f)
        with open(os.path.join(image_path,'distanceMapAvg.json'), 'w', encoding='utf-8') as f:
            json.dump(distanceMapAvg, f)



if __name__ == "__main__":
    parser = argparse.ArgumentParser(description='Process an image.')
    parser.add_argument('image_path', help='The path to the image file')
    parser.add_argument('gender', help='Gender of the user')
    parser.add_argument('user_id', help='UserId of the user')

    args = parser.parse_args()
    main(args.image_path, args.gender, args.user_id)