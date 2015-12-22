setwd("")

data=read.csv("/data/yearly graphs and datasets/2005/dynamic_indices2005.csv")
data[,1]=as.character(data[,1])

trends=read.csv("trends.csv",head=FALSE)
library(hash)
##keep the index of every author, to avoid greping the datasets
hp=hash(keys=trends[,1],values=trends[,2])
hc=hash(keys=trends[,1],values=trends[,3])


for(i in 1:nrow(data)){
  print(data[i,1])
  treP=hp[[data[i,1]]]
  treC=hc[[data[i,1]]]
  data[i,16]=data[i,16]+treP
  data[i,31]=data[i,31]+treC
}

write.csv(data,"/data/finals/final.csv",row.names=FALSE)