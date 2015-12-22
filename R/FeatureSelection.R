setwd("/data/finals")
data=read.csv("aggregated.csv")
lab=data[,1]
data=data[,-1]

for(i in 1:ncol(data)){
  data[,i]=as.numeric(data[,i])
}


library(svd)
sing=svd(scale(data))
plot(sing$d^2/sum(sing$d^2),pch=19,col='blue',ylab="Singular Value",xlab="SVD column",main="percentage of deviation depicted")

for (i in 1:4){
  plot(sing$v[,4],xlab="", ylab="Singular Value",pch=19,cex=0.5,col="blue")
  title(i)
}

l=c()
for(i in 1:nrow(sing$v)){###for every feature
  l=c(l,sum(abs(sing$v[i,1:4])))###count its right singular vector value
}

plot(1:13,l,xlab="",ylab="contribution in variance",xaxt="n",col="blue",pch=19,main="variance contribution per feature")
axis(side=1,at=1:13,labels=names(data),cex.axis=0.8,las=2)
print(names(data)[which(l==max(l))]) ##the most influential feature