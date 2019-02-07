setwd("/data/evaluation/validity")

library(fmsb)

data=data.frame(matrix(ncol=20,nrow=0))
x=read.csv("validity1.csv",header=TRUE,sep=",")
names(data)=names(x)
data[1,]=x[1,]
for(i in 2:7){
  x=read.csv(paste("validity",paste(i,".csv",sep=""),sep=""),header=TRUE,sep=",")
  data[i,]=x[1,]
}
write.csv(data,"validity.csv",row.names=FALSE)

val=read.csv("validity.csv")

#penalized productivity
toPlot=val[,1:5]
par(mar=c(0,0,0,0))

max(toPlot)
#set min 
toPlot=rbind(rep(0,ncol(toPlot)),toPlot)
#set max 
toPlot=rbind(rep(0.25,ncol(toPlot)),toPlot)

radarchart(toPlot,cglty=3,axistype=1,vlcex=0.8,caxislabels=c(0,"","","",0.25),plty=1,plwd=2,pty=c(15,16,17,18,4,8,11),pcol=c(1:6,8))

legend("topright", cex=.9,inset=c(0.05,0),c(paste("Cluster ",1:7,sep="")),lty=1,pch=c(15,16,17,18,4,8,11),lwd=2,col=c(1:6,8),xpd=TRUE)


#the rest
toPlot=log10(val[c(17,19,20,7,9,10,12,14,15)])
toPlot[7,1]=0

par(mar=c(0,0,0,0))


#set min 
toPlot=rbind(rep(0,ncol(toPlot)),toPlot)
#set max 
toPlot=rbind(rep(4,ncol(toPlot)),toPlot)

toPlotClean=do.call(data.frame,lapply(toPlot, function(x) replace(x, is.infinite(x),0)))
names(toPlotClean)=names(toPlot)

radarchart(toPlotClean,axistype=1,vlcex=1,caxislabels=c(0,"","","",4),plty=1,plwd=2,pty=c(15,16,17,18,4,8,11),pcol=c(1:6,8))

legend("topright", cex=.9,inset=c(0.05,0),c(paste("Cluster ",1:7,sep="")),lty=1,pch=c(15,16,17,18,4,8,11),lwd=2,col=c(1:6,8),xpd=TRUE)

