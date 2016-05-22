setwd("/data/finals")
data=read.csv("aggregated.csv")
lab=data[,1]
data=data[,-1]
name=names(data)
data = data.frame(sapply(data,as.numeric))
names(data) = name

sing=svd(scale(data))
plot(sing$d^2/sum(sing$d^2),pch=19,col='blue',ylab="Singular Value",xlab="SVD column")

features_contribution = apply(abs(sing$v[,1:4]),1,sum)

features_contribution = sort(features_contribution,decreasing=T,index.return=TRUE)

plot(1:13,features_contribution$x,xlab="Feature",ylab="Contribution in variance",xaxt="n",col="blue",pch=19)
axis(side=1,at=1:13,labels=features_contribution$ix,cex.axis=1,las=1)
