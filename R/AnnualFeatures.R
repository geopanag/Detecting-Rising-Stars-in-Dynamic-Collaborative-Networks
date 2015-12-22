library(igraph)

for(year in 1998:2005){
##read graph
  setwd(paste("/data/yearly graphs and datasets/",year,sep=""))
  g=read.graph(paste(paste("weightAggregated",year,sep=""),".txt",sep=""),format="ncol",directed=FALSE)
  
  ##network features
  f=data.frame(matrix(nrow=length(V(g)),ncol=3))
  names(f)=c("author","eigenvalue","degree")
  f$author=V(g)$name
  f$eigenvalue =evcent(g,directed = FALSE, scale = TRUE)$vector 
  f$degree=degree(g,v=V(g))##h normalizes = false
  
  ##the Weighted powergraph features
  pgw=read.csv(paste("authPGW",paste(year,".csv", sep=""),header=FALSE))
  ##the Simple powergraph features
  pgs=read.csv(paste("authPGS",paste(year,".csv", sep=""), sep=""),header=FALSE)
  
  library(hash)
  ##keep the index of every author, to avoid greping the datasets
  h=hash(keys=pgw[,1],values=seq(1:nrow(pgw)))
  
  ##the independant(from database) features
  db=read.csv(paste("authDB",paste(year,".csv", sep=""), sep=""),header=FALSE)
  db[,8]=0.0
  db[,9]=0.0
  db[,10]=0.0
  db[,11]=0.0
  db[,12]=0.0
  db[,13]=0.0
  db[,14]=0.0
  
  for(i in seq(nrow(f))){
    print(f$author[i])
    counter=h[[f$author[i]]]
    
    ##find the author i in the db dataset and in the pgw-pgs dataset
    ##keep the row with the right author, its eigen value and its normalized degree
    db[counter,8:9]=f[i,2:3]
    ##its sum of edge weights
    db[counter,10]=sum(E(g98)$weight[E(g98)[from(i)]])
    ##its weighted pg weight and clique weight
    db[counter,11:12]=pgw[counter,2:3]
    ##its simple pg weight and clique weight
    db[counter,13:14]=pgs[counter,2:3]
  }
  names(db)=c("id","paper_sum","paper_norm","paper_now","cit_sum","cit_norm","cit_now","eigenvector","degree_norm","edges_weight","wpn_weight","wpn_clique","spn_weight","spn_clique")
  write.csv(db,paste("features",year, sep=""),row.names=FALSE)
}
