#
# This is the user-interface definition of a Shiny web application. You can
# run the application by clicking 'Run App' above.
#
# Find out more about building applications with Shiny here:
# 
#    http://shiny.rstudio.com/
#

library(shiny)

# Define UI for application that draws a histogram
shinyUI(fluidPage(
  
  # Application title
  titlePanel(strong("EXAMINE CLUSTER CHARACTERISTICS")),
  
  sidebarLayout(
    sidebarPanel(
      selectInput("cluster", "Select Cluster to plot:", 
                  choices = c("1", "2","3","4","5","6","7")),
      numericInput('hq',span('Select High Quantile ',style = "color:red"),.85,min=.51,max=1,step=.01),
      numericInput('lq',span('Select Low Quantile ',style = "color:green"),.15,min=0,max=.50,step=.01),
      selectInput("quantLine", "Select the type of quantile line*:",choices=c("Smooth","Rigid")),
      em("* Rigid will plot the exact line while Smooth will plot a fited line with its confidence intervals."),
      #submitButton('Submit')
      div("Cluster labeling can be an arduous task, demanding for carefull investigation of several figures. 
          This interactive plot allows to examine the values of each cluster in all 65 characteristics 
          (points in the plot). It also facilitates the comparison with the behavior of all clusters
          in higher and lower percentiles (red and green lines), making apparent the characteristis where the cluster stands out.
          Hovering over a point will present which feature it corresponds to.",
          style = "align::center")
      
    ),
    # Show a plot of the generated distribution
    mainPanel(
      h3("Selected cluster's features compared to all clusters' higher and lower values"),
      plotOutput("distPlot",hover = hoverOpts(id ="plot_hover")),
      p("Respective Feature:",verbatimTextOutput("hover_info"))
    )
  )
))
