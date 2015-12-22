setwd("/data/evaluation/journal_distribution")

par(las=2)

for(i in 3:7){
  path=paste("cluster",paste(i,".csv",sep=""),sep="")
  x=read.csv(file=path,header=TRUE,sep=";")  
  barplot(log2(x$frequency), main=paste("2005 Journal distribution of cluster ",i,sep=""),names=1:length(x$journal),xlab="index of corresponding journals in csv",ylab="number of authors (log2 scale)") 
}

###### cluster 7 behavior
##boxplots of coauthïrs to identify general behavior
par(las=1)

coauths=data.frame(matrix(nrow=1132,ncol=0))
setwd("C:/Users/Giwrgos/hua/ptuxiakh/data/evaluation/coherence/")
for(i in 2006:2013){
  path=paste(paste(i,"/",sep=""),"cluster7.csv",sep="")
  x=read.csv(file=path,header=TRUE,sep=",")  
  coauths=cbind(coauths,x[4])
}
boxplot(coauths,names=2006:2013)


##find cluster 7 outliers and store them in csv
coauths=data.frame(matrix(nrow=394,ncol=0))
setwd("C:/Users/Giwrgos/hua/ptuxiakh/data/evaluation/coherence/")
i=2006
path=paste(paste(i,"/",sep=""),"cluster7.csv",sep="")
x=read.csv(file=path,header=TRUE,sep=",")  
coauths=x[4]

for(i in 2007:2013){
  path=paste(paste(i,"/",sep=""),"cluster7.csv",sep="")
  x=read.csv(file=path,header=TRUE,sep=",")  
  coauths=coauths+x[4]
}

write.csv(x[which(coauths>quantile(as.numeric(unlist(coauths)),.95)),1],"outliersCL7.csv",row.names=FALSE)

