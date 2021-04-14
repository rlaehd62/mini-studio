import math
import matplotlib.pyplot as plt
import numpy as np
from sklearn.datasets import load_iris

#아이리스 데이터
data = load_iris()

#정보 추출
features = data['data']    # shape = (150, 4)
feature_names = data['feature_names']
target = data['target']
target_names = data['target_names']

#산점도
for t in range(3):
    if t == 0:
        c='r'
        marker='>'
    elif t == 1:
        c='g'
        marker='o'
    elif t == 2:
        c='b'
        marker='x'
    plt.scatter(features[target == t, 0],   # sepal length
                features[target == t, 1],   # sepal width
                marker = marker,
                c = c)
    plt.xlabel("sepal length")
    plt.ylabel("sepal width")


#참고 시스템
'''
DATASET_PATH = "/content/drive/My Drive/data/genres_Classification_data"
JSON_PATH = "data_10.json"
SAMPLE_RATE = 22050
TRACK_DURATION = 30 # sec
SAMPLES_PER_TRACK = SAMPLE_RATE * TRACK_DURATION # 한 track의 sample 수

def save_mfcc(dataset_path,json_path, n_mfcc, n_fft, hop_length, n_segments ):
  data = { "mapping": [],"labels": [],"mfcc": [] }
    
  samples_per_segment = int(SAMPLES_PER_TRACK / n_segments)
  num_mfcc_vectors_per_segment = math.ceil(samples_per_segment / hop_length)

    # 모든 sub 폴더에 대해 loop 
  for i, (dirpath, dirnames, filenames) in enumerate(os.walk(dataset_path)):
    if dirpath is not dataset_path:
      
      genre_label = dirpath.split("/")[-1]
      data["mapping"].append(genre_label)
      print("\n Processing : {}".format(genre_label))

      for f in filenames:
          
        file_path = os.path.join(dirpath,f) 
        sig, sr = librosa.load(file_path, sr=SAMPLE_RATE)

        for d in range(n_segments):
          start = samples_per_segment * d 
          finish = start + samples_per_segment

          mfcc = librosa.feature.mfcc(sig[start:finish], sr, n_mfcc=n_mfcc, n_fft=n_fft, hop_length=hop_length)
          mfcc = mfcc.T

          if len(mfcc) == num_mfcc_vectors_per_segment:
            data["mfcc"].append(mfcc.tolist())
            data["labels"].append(i-1)
            print("{}, segment:{}".format(file_path, d+1))
                  
  with open(json_path, "w") as fp:
    json.dump(data, fp, indent =4)


if __name__ == "__main__":
  save_mfcc(DATASET_PATH,JSON_PATH,13,2048,512,5)
'''