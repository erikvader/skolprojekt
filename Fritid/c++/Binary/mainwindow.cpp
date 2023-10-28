#include "mainwindow.h"
#include "ui_mainwindow.h"
#include "Base.h"
#include "binarystring.h"
#include <iostream>

using namespace std;

MainWindow::MainWindow(QWidget *parent) :
    QMainWindow(parent),
    ui(new Ui::MainWindow)
{
    ui->setupUi(this);
    initStuff();
}

MainWindow::~MainWindow()
{
    delete ui;
}

void MainWindow::initStuff(){
    ui->ModeSelector->addItem("Base converter");
    ui->ModeSelector->addItem("Text to binary");

    curModeIndex = 0;
    on_ModeSelector_currentIndexChanged(0);

}

void MainWindow::initBaseChoosers(){
    if(curModeIndex == 0){
        ui->BaseChooser_1->clear();
        ui->BaseChooser_2->clear();
        QStringList numbers;
        for(int i = 2; i <= 36; i++){
            QString inString = QString::number(i);
            numbers.push_back(inString);
        }

        ui->BaseChooser_1->addItems(numbers);
        ui->BaseChooser_2->addItems(numbers);

        ui->BaseChooser_1->setCurrentIndex(8);
        ui->BaseChooser_2->setCurrentIndex(8);

        ui->BaseChooser_1->setEnabled(true);
        ui->BaseChooser_2->setEnabled(true);
    }else if(curModeIndex == 1){

        ui->BaseChooser_1->clear();
        ui->BaseChooser_2->clear();
        ui->BaseChooser_1->addItem("Text");
        ui->BaseChooser_1->addItem("Binary");

        ui->BaseChooser_1->setCurrentIndex(0);

        ui->BaseChooser_1->setEnabled(true);
        ui->BaseChooser_2->setEnabled(false);
    }
}

void MainWindow::on_ModeSelector_currentIndexChanged(int index){
    curModeIndex = index;
    on_ClearButton_clicked();
    initBaseChoosers();
}

void MainWindow::on_GoButton_clicked()
{   string output = "";
    if(curModeIndex == 0){
        Base<long long> b;
        output = b.baseToBase(ui->InputTextField->toPlainText().toStdString(),
                     ui->BaseChooser_1->currentIndex()+2,
                     ui->BaseChooser_2->currentIndex()+2);

    }else if(curModeIndex == 1){
        BinaryString bs;
        if(ui->BaseChooser_1->currentIndex() == 0){
            output = bs.toBinary(ui->InputTextField->toPlainText().toStdString());
        }else if(ui->BaseChooser_1->currentIndex() == 1){
            output = bs.toText(ui->InputTextField->toPlainText().toStdString());
        }
    }
    ui->OutputTextField->setText(QString::fromStdString(output));
}

void MainWindow::on_SwapButton_clicked(){
    QString tempString = "";
    tempString = ui->InputTextField->toPlainText();
    ui->InputTextField->setText(ui->OutputTextField->toPlainText());
    ui->OutputTextField->setText(tempString);
}

void MainWindow::on_ClearButton_clicked(){
    ui->InputTextField->setText("");
    ui->OutputTextField->setText("");
}
