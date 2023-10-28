#include "mainwindow.h"
#include "ui_mainwindow.h"

MainWindow::MainWindow(QWidget *parent) :
    QMainWindow(parent),
    ui(new Ui::MainWindow)
{
    ui->setupUi(this);

    diameter = 10;
    viewWidth = 379;
    viewHeight = 249;

    scene = new QGraphicsScene(QRectF(0, 0, viewWidth, viewHeight));
    ui->gView->setScene(scene);
    //ellipse = scene->addEllipse(0, 0, 50, 50);
    ellipse = new QGraphicsEllipseItem();
    ellipse->setBrush(QBrush(Qt::blue));
    ellipse->setPen(QPen(Qt::blue));
    scene->addItem(ellipse);
    ellipse->setRect(0,0,diameter, diameter);
    ui->Slider->setValue(diameter+1);

}

MainWindow::~MainWindow()
{
    delete ui;
}

void MainWindow::on_Slider_valueChanged(int value){
    diameter = value;
    ellipse->setPos(value,0);
    //ellipse->setRect(viewWidth/2-diameter/2, viewHeight/2-diameter/2, diameter, diameter);
    ui->gView->update();
}
