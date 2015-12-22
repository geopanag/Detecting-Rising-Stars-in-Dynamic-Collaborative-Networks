setwd("/data/finals")


ts=read.csv("timeSeries.csv",header=FALSE)
labTs=ts[,1]
ts=ts[,-1]

library(fpc)  
library(dtw)   

p=pam(ts,7,diss=FALSE,metric='DTW')
write.csv(cbind(labTs,p$clustering),"tsPam.csv")
