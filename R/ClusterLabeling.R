setwd("/data/finals")

#data=read.csv("aggregated.csv")
data=read.csv("final.csv")
lab=data[,1]
data=data[,-1]
f=65
k=kmeans(data,centers=7)

cent=data.frame(k$centers)

high=c()
low=c()
for(i in 1:ncol(k$centers)){
  high=c(high,quantile(cent[,i],c(.85)))
  low=c(low,quantile(cent[,i],c(.15)))
}

q=matrix(0,7,f)

for(x in 1:7){
  for(i in 1:ncol(cent)){
    if(cent[x,i]>high[i]){
      print(paste("megalo",names(cent)[i]))
      q[x,i]=cent[x,i]
    }
    if(cent[x,i]<low[i]){
      print(paste("mikro",names(cent)[i]))
      q[x,i]=cent[x,i]
    }
  }
}
par(mfrow=c(1,1))
plot(q[1,],xlab="",ylab="centers",xaxt="n",col="chocolate",pch=19)
points(q[2,],xaxt="n",col="green",pch=19)
points(q[3,],xaxt="n",col="red",pch=19)
points(q[4,],xaxt="n",col="blue",pch=19)
points(q[5,],xaxt="n",col="purple",pch=19)
points(q[6,],xaxt="n",col="magenta",pch=19)
points(q[7,],xaxt="n",col="black",pch=19)
axis(side=1,at=1:f,labels=names(data),cex.axis=0.8,las=2)



par(mfrow=c(2,2))
plot(k$centers[,43],xlab="column",ylab="lastChangeEdgWeight",col="blue",pch=19)  #1,3,4,5
plot(k$centers[,27],xlab="column",ylab="maxChangeYearlyCitations",col="blue",pch=19) #3,5
plot(k$centers[,10],xlab="column",ylab="avgPaperNormalized",col="blue",pch=19)
plot(k$centers[,54],xlab="column",ylab="sumChangesQualityPNClique",col="blue",pch=19)



plot(log(k$centers[1,]),xlab="columns",ylab="centers",type="l",xaxt="n",col="yellow",main="log norm")
lines(log(k$centers[2,]),type="l",xaxt="n",col="green")
lines(log(k$centers[3,]),type="l",xaxt="n",col="red")
lines(log(k$centers[4,]),type="l",xaxt="n",col="blue")
lines(log(k$centers[5,]),type="l",xaxt="n",col="purple")
lines(log(k$centers[6,]),type="l",xaxt="n",col="magenta")
lines(log(k$centers[7,]),type="l",xaxt="n",col="black")
axis(side=1,at=1:65,labels=names(data),cex.axis=0.8,las=2)


plot(k$centers[1,],xlab="columns",ylab="centers",type="l",xaxt="n",col="yellow",main="not norm")
lines(k$centers[2,],type="l",xaxt="n",col="green")
lines(k$centers[3,],type="l",xaxt="n",col="red")
lines(k$centers[4,],type="l",xaxt="n",col="blue")
lines(k$centers[5,],type="l",xaxt="n",col="purple")
lines(k$centers[6,],type="l",xaxt="n",col="magenta")
lines(k$centers[7,],type="l",xaxt="n",col="black")
axis(side=1,at=1:65,labels=names(data),cex.axis=0.8,las=2)


cl=matrix(nrow=7,ncol=65)
for(i in 1:7){
  for(j in 1:65){
    cl[i,j]=k$centers[i,j]/max(k$centers[,j])
  }
}

plot(cl[1,],xlab="columns",ylab="centers",type="l",xaxt="n",col="yellow",main="max norm")
lines(cl[2,],type="l",xaxt="n",col="green")
lines(cl[3,],type="l",xaxt="n",col="red")
lines(cl[4,],type="l",xaxt="n",col="blue")
lines(cl[5,],type="l",xaxt="n",col="purple")
lines(cl[6,],type="l",xaxt="n",col="magenta")
lines(cl[7,],type="l",xaxt="n",col="black")
axis(side=1,at=1:65,labels=names(data),cex.axis=0.8,las=2)
