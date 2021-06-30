import pandas as pd
import sklearn
from sklearn.metrics.pairwise import cosine_similarity

df_30 = pd.read_csv('AI/features_30_sec.csv', index_col='filename')

labels = df_30[['label']]
df_30 = df_30.drop(columns=['length', 'label'])

df_30_scaled = sklearn.preprocessing.scale(df_30)

df_30 = pd.DataFrame(df_30_scaled, columns=df_30.columns)

similarity = cosine_similarity(df_30)

sim_df = pd.DataFrame(similarity, index=labels.index, columns=labels.index)

def find_similar_songs(name, n=10):
    series = sim_df[name].sort_values(ascending=False)
    series = series.drop(name)
    print (series.head(n).to_frame())
    print("\n")
    return series.head(n).to_frame()

def inputing (song):
    insong = find_similar_songs(song)
    
    testv = str(insong).split("\n")
    testv = testv[2:]

    hiphop = [0, 0]
    reggae = [0, 0]
    disco = [0, 0]
    pop = [0, 0]
    metal = [0, 0]
    jazz = [0, 0]
    blues = [0, 0]
    classical = [0, 0]
    country = [0, 0]
    rock = [0, 0]

    for i in testv:
        listin = list(filter(None,i.split(" ")))
        
        if listin[0].split(".")[0] == "hiphop":
            hiphop[0] += 1
            hiphop[1] += float(listin[1])
        elif listin[0].split(".")[0] == "reggae":
            reggae[0] += 1
            reggae[1] += float(listin[1])
        elif listin[0].split(".")[0] == "disco":
            disco[0] += 1
            disco[1] += float(listin[1])
        elif listin[0].split(".")[0] == "pop":
            pop[0] += 1
            pop[1] += float(listin[1])
        elif listin[0].split(".")[0] == "metal":
            metal[0] += 1
            metal[1] += float(listin[1])
        elif listin[0].split(".")[0] == "jazz":
            jazz[0] += 1
            jazz[1] += float(listin[1])
        elif listin[0].split(".")[0] == "blues":
            blues[0] += 1
            blues[1] += float(listin[1])
        elif listin[0].split(".")[0] == "classical":
            classical[0] += 1
            classical[1] += float(listin[1])
        elif listin[0].split(".")[0] == "country":
            country[0] += 1
            country[1] += float(listin[1])
        elif listin[0].split(".")[0] == "rock":
            rock[0] += 1
            rock[1] += float(listin[1])
            
    plus = hiphop[1] + reggae[1] + disco[1] + pop[1] + jazz[1] + metal[1] + blues[1] + classical[1] + country[1] + rock[1]
            
    print(song + " 분석 결과")
    
    if hiphop[0] != 0:
        print("hiphop 장르일 확률 : " + str(hiphop[0] * 10) + " % - 정 백분율 : " +str(round(hiphop[1] / plus * 100, 5)) + " %")
    if reggae[0] != 0:
        print("reggae 장르일 확률 : " + str(reggae[0] * 10) + " % - 정 백분율 : " +str(round(reggae[1] / plus * 100, 5)) + " %")
    if pop[0] != 0:
        print("pop 장르일 확률 : " + str(pop[0] * 10) + " % - 정 백분율 : " +str(round(pop[1] / plus * 100, 5)) + " %")
    if jazz[0] != 0:
        print("jazz 장르일 확률 : " + str(jazz[0] * 10) + " % - 정 백분율 : " +str(round(jazz[1] / plus * 100, 5)) + " %")
    if metal[0] != 0:
        print("metal 장르일 확률 : " + str(metal[0] * 10) + " % - 정 백분율 : " +str(round(metal[1] / plus * 100, 5)) + " %")
    if classical[0] != 0:
        print("classical 장르일 확률 : " + str(classical[0] * 10) + " % - 정 백분율 : " +str(round(classical[1] / plus * 100, 5)) + " %")
    if country[0] != 0:
        print("country 장르일 확률 : " + str(country[0] * 10) + " % - 정 백분율 : " +str(round(country[1] / plus * 100, 5)) + " %")
    if rock[0] != 0:
        print("rock 장르일 확률 : " + str(rock[0] * 10) + " % - 정 백분율 : " +str(round(rock[1] / plus * 100, 5)) + " %")
    if disco[0] != 0:
        print("disco 장르일 확률 : " + str(disco[0] * 10) + " % - 정 백분율 : " +str(round(disco[1] / plus * 100, 5)) + " %")
    if blues[0] != 0:
        print("blues 장르일 확률 : " + str(blues[0] * 10) + " % - 정 백분율 : " +str(round(blues[1] / plus * 100, 5)) + " %")
        
    print("\n")
    
inputing('rock.00058.wav')
inputing('pop.00018.wav')
inputing('blues.00098.wav')
inputing('classical.00012.wav')