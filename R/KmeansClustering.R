setwd("/data/finals")

library(clusterSim)

data=read.csv("final.csv")
lab=data[,1]
data=data[,-1]
cls=data.frame(matrix(nrow=98,ncol=5))
names(cls)=c("number","avg(within groups sum of squares)","avg(distance between cluster centroids)","avg(db)","avg(dunn)")
for(p in 3:100){
  print(p)
  kClust=kmeans(data,centers=p) ##kmeans clustering with different numbe rof clusters
  wgss=c()
  for(i in 1:length(kClust$withinss)){
    wgss=c(wgss,kClust$withinss[i]/kClust$size[i])##normalized within group sum of squares
  }
  dbcc=0
  dun=c()
  for(i in 1:ncol(kClust$centers)){
    inter=c()
    h=0
    for(j in 1:nrow(kClust$centers)){
      for(k in j:nrow(kClust$centers)){
        if(k!=j){
          inter=c(inter,abs(kClust$centers[j,i]-kClust$centers[k,i]))##pairwise distance between cluster centroids
          h=h+1##number of distances
        }
      }
    }
    dbcc=dbcc+sum(inter)/h##distance between cluster centroids for each feature
    dun=c(dun,inter)##intercluster distances
  }
  cls[p-2,1]=p
  cls[p-2,2]=sum(wgss)/p##average sum of squares through clusters
  cls[p-2,3]=dbcc/ncol(kClust$centers)##average distance between centroids through columns
  cls[p-2,4]=index.DB(data,kClust$cluster,centrotypes="centroids",p=2)$DB ##Davies-Bouldin
  cls[p-2,5]=min(dun)/max(wgss)##minimum(intercluster)/maximum(intracluster)
}
write(cls,"kmeans100.csv")






