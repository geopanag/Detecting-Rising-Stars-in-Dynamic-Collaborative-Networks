setwd("/data/yearly graphs and datasets")

library(igraph)

g98<-read.graph("1998/weightAggregated1998.txt",format="ncol",directed=FALSE)
g99<-read.graph("1999/weightAggregated1999.txt",format="ncol",directed=FALSE)
g00<-read.graph("2000/weightAggregated2000.txt",format="ncol",directed=FALSE)
g01<-read.graph("2001/weightAggregated2001.txt",format="ncol",directed=FALSE)
g02<-read.graph("2002/weightAggregated2002.txt",format="ncol",directed=FALSE)
g03<-read.graph("2003/weightAggregated2003.txt",format="ncol",directed=FALSE)
g04<-read.graph("2004/weightAggregated2004.txt",format="ncol",directed=FALSE)
g05<-read.graph("2005/weightAggregated2005.txt",format="ncol",directed=FALSE)

deg=c(mean(degree(g98)),mean(degree(g99)),mean(degree(g00)),mean(degree(g01)),mean(degree(g02)),mean(degree(g03)),mean(degree(g04)),mean(degree(g05)))

nod=c(length(V(g98)),length(V(g99)),length(V(g00)),length(V(g01)),length(V(g02)),length(V(g03)),length(V(g04)),length(V(g05)))

edg=c(length(E(g98)),length(E(g99)),length(E(g00)),length(E(g01)),length(E(g02)),length(E(g03)),length(E(g04)),length(E(g05)))
year=1998:2005
papers=c(2993,2959,3193,3549,3914,4654,5764,6591)

par(mfrow=c(1,1))
barplot(rev(nod),xlab="Number of nodes",ylab="year",horiz=TRUE,ylim=c(0,10),col='blue',pch=19,las=1)
usr <- par("usr")
par(usr=c(usr[1:2], 0, 1.1*length(year)))
axis(side=2,at=seq(1,length(year)),labels=rev(year),cex.axis=0.8,las=2)

barplot(rev(edg),xlab="Number of collaborations",ylab="year",horiz=TRUE,col='green',pch=19,las=1)  
usr <- par("usr")
par(usr=c(usr[1:2], 0, 1.1*length(year)))
axis(side=2,at=seq(1,length(year)),labels=rev(year),cex.axis=0.8,las=2)

barplot(rev(papers),xlab="Number of papers",ylab="year",horiz=TRUE,col='red',pch=19,las=1)  
usr <- par("usr")
par(usr=c(usr[1:2], 0, 1.1*length(year)))
axis(side=2,at=seq(1,length(year)),labels=rev(year),cex.axis=0.8,las=2)

