#
# This is the server logic of a Shiny web application. You can run the 
# application by clicking 'Run App' above.
#
# Find out more about building applications with Shiny here:
# 
#    http://shiny.rstudio.com/
#

library(shiny)
library(ggplot2)

cents=read.csv("cluster_centers.csv")
clusters=read.csv("author_clusters.csv",header=FALSE)
features=read.csv("features_meaning.csv")
featureTypes=factor(c(rep("Individual",30),rep("Social",35))) 

shinyServer(function(input, output) {
  
  
    output$distPlot <- renderPlot({
      highs=lapply(cents, function(x) quantile(x,c(input$hq)))
      lows=lapply(cents, function(x) quantile(x,c(input$lq)))
    
      qp<-qplot(1:65,as.numeric(cents[as.numeric(input$cluster),]),colour = featureTypes,geom = "point")
        
      if(input$quantLine=="Rigid"){
        qp<-qp+geom_line(aes(x=1:65,y=as.numeric(highs)),colour = "red",lwd=1)+geom_line(aes(x=1:65,y=as.numeric(lows)),colour = "green")
        }else{
          qp<-qp+geom_smooth(aes(x=1:65,y=as.numeric(highs)),colour = "red",lwd=1)+geom_smooth(aes(x=1:65,y=as.numeric(lows)),colour = "green")
        }
        
       qp+geom_point(size =3)+
       labs(
        title = paste(paste("Values of cluster's",as.numeric(input$cluster)),"centroid (min-max scaled)"),
        color = "Types of Features:"
        )+theme(axis.title.y=element_blank(),axis.title.x=element_blank(),axis.text.x = element_blank(),axis.title=element_text(size=12),legend.text=element_text(size=12),legend.title=element_text(size=12),legend.position="top")
  })

    output$hover_info <- renderText({
      if(!is.null(input$plot_hover)){
        hover=input$plot_hover
        dist=sqrt((hover$x-1:65)^2+(hover$y-cents[as.numeric(input$cluster),])^2)
        
        if(min(dist) < 3)
          as.character(features[which.min(dist),2])
      } 
    })
    
  
})
