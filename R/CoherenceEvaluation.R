
setwd("/data/evaluation/coherence")

for(year in 2009){
  data=data.frame(matrix(ncol = 16, nrow = 0))
  names(data)=c("id","publications25","publications50","publications75","publications90","publicationsAVG","citations25","citations50","citations75","citations90","citationsAVG","coauthors25","coauthors50","coauthors75","coauthors90","coauthorsAVG")
  
  for(i in 1:7){
    path=paste(paste(year,"/cluster",sep=""),paste(i,".csv",sep=""),sep="")
    x=read.csv(file=path,header=TRUE,sep=",")
    
    cluster=c(i,as.numeric(quantile(x[[2]],c(0.25,0.50,0.75,0.90))))#publications
    cluster=c(cluster,mean(x[[2]]))
    cluster=c(cluster,as.numeric(quantile(x[[3]],c(0.25,0.50,0.75,0.90))))#citations
    cluster=c(cluster,mean(x[[3]]))
    cluster=c(cluster,as.numeric(quantile(x[[4]],c(0.25,0.50,0.75,0.90))))#coauthors
    cluster=c(cluster,mean(x[[4]]))
    data[i,]=cluster
  }
  
  write.csv(data,paste(year,"/coherence.csv",sep=""),row.names=FALSE)
}


pub90=data.frame(matrix(nrow=7,ncol=8))
names(pub90)=2006:2013
cit90=data.frame(matrix(nrow=7,ncol=8))
names(cit90)=2006:2013
coauths90=data.frame(matrix(nrow=7,ncol=8))
names(coauths90)=2006:2013


pubAVG=data.frame(matrix(nrow=7,ncol=8))
names(pubAVG)=2006:2013
citAVG=data.frame(matrix(nrow=7,ncol=8))
names(citAVG)=2006:2013
coauthsAVG=data.frame(matrix(nrow=7,ncol=8))
names(coauthsAVG)=2006:2013

for(year in 2006:2013){
  x=read.csv(file=paste(year,"/coherence.csv",sep=""),header=TRUE,sep=",")
  
  pub90[,year-2005]=as.numeric(x$publications90)
  cit90[,year-2005]=log2(as.numeric(x$citations90))
  coauths90[,year-2005]=log2(as.numeric(x$coauthors90))
  
  pubAVG[,year-2005]=as.numeric(x$publicationsAVG)
  citAVG[,year-2005]=log2(as.numeric(x$citationsAVG))
  coauthsAVG[,year-2005]=log2(as.numeric(x$coauthorsAVG))
}



plotClusterMetrics <- function(metric,name) {
  
  pdf(paste(paste("figures/",name,sep=""),".pdf",sep=""))
  
  par(mar=c(5.1, 4.1, 4.1, 8.1))
  
  plot(names(metric),as.numeric(metric[1,]),pch=15,col=1,ylab=name,xlab="",ylim=c(0,max(metric)),bty='L')
  lines(names(metric),as.numeric(metric[1,]),lwd=2,col=1)
  
  points(names(metric),as.numeric(metric[2,]),pch=16,col=2)
  lines(names(metric),as.numeric(metric[2,]),lwd=2,col=2)
  
  points(names(metric),as.numeric(metric[3,]),pch=17,col=3)
  lines(names(metric),as.numeric(metric[3,]),lwd=2,col=3)
  
  points(names(metric),as.numeric(metric[4,]),pch=18,col=4)
  lines(names(metric),as.numeric(metric[4,]),lwd=2,col=4)
  
  points(names(metric),as.numeric(metric[5,]),pch=4,col=5)
  lines(names(metric),as.numeric(metric[5,]),lwd=2,col=5)
  
  points(names(metric),as.numeric(metric[6,]),pch=8,col=6)
  lines(names(metric),as.numeric(metric[6,]),lwd=2,col=6)
  
  points(names(metric),as.numeric(metric[7,]),pch=11,col=8)
  lines(names(metric),as.numeric(metric[7,]),lwd=2,col=8)
  
  legend("topright", inset=c(-0.3,0),c(paste("Cluster ",1:7,sep="")),lty=1,pch=c(15,16,17,18,4,8,11),lwd=2,col=c(1:6,8),xpd=TRUE)
  
  dev.off()
}

plotClusterMetrics(pubAVG,"Average Publications")
plotClusterMetrics(pub90,"90th Percentile Publications")

plotClusterMetrics(citAVG,"Average Citations (in log2)")
plotClusterMetrics(cit90,"90th Percentile Citations (in log2)")

plotClusterMetrics(coauthsAVG,"Average Coauthors (in log2)")
plotClusterMetrics(coauths90,"90th Percentile Coauthors (in log2)")
