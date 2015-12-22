setwd("/data/finals")
ts=read.csv("ts_clusters.csv",header=FALSE)
km=read.csv("autho_clusters.csv",header=FALSE)

ComAuthors=matrix(nrow=7,ncol=7)
for(j in 1:7){
  TsIds=as.factor(ts[ts[,2]==j,1])##ids of authors in j cluster of timeSeries
  for(i in 1:7){
    KIds=as.factor(km[km[,2]==i,1])##ids of authors in i cluster of kmeans
    count=0
    for(s in KIds){
      if(s %in% TsIds){
        count=count+1##count the common authors
      }
    }
    ComAuthors[j,i]=count
  }
}

ComAuthors=rbind(c(1,2,3,4,5,6,7),ComAuthors)##corresponding TS clusters
ComAuthors=cbind(c(0,1,2,3,4,5,6,7),ComAuthors)## >> kmeans clusters
sum=0
while(ncol(ComAuthors)>2){ #&& nrow(kala)>1){
  k=which(ComAuthors==max(ComAuthors),arr.ind=TRUE)
  sprintf("%s TsCluster corresponds to %s KCluster with %s Common Authors", ComAuthors[k[1],1] ,ComAuthors[1,k[2]],max(ComAuthors))
  sum=sum+max(ComAuthors)
  ComAuthors=ComAuthors[-k[1],]
  ComAuthors=ComAuthors[,-k[2]]
}
sum=sum+ComAuthors[2,2] ##add the last value
print(sum*100/nrow(n))
