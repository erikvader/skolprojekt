#include "addnewtimewindow.h"
#include "ui_addnewtimewindow.h"

addNewTimeWindow::addNewTimeWindow(QWidget *parent) :
    QDialog(parent),
    ui(new Ui::addNewTimeWindow)
{
    ui->setupUi(this);
    this->setWindowFlags(this->windowFlags() & ~Qt::WindowContextHelpButtonHint);
}

addNewTimeWindow::~addNewTimeWindow()
{
    delete ui;
}

void addNewTimeWindow::on_okButton_clicked(){
    int sekun = 0;
    int hundrade = 0;
    QString scramb = "";

    sekun = ui->secSpin->value();
    sekun += ui->minSpin->value()*60;
    hundrade = ui->hundSpin->value();
    scramb = ui->scramEdit->text();

    emit okButtonClicked(sekun,hundrade,scramb);

    resetStuff();
    this->close();
}

void addNewTimeWindow::on_cancelButton_clicked(){
    resetStuff();
    this->close();
    //delete this;
}

void addNewTimeWindow::resetStuff(){
    ui->hundSpin->setValue(0);
    ui->secSpin->setValue(0);
    ui->minSpin->setValue(0);
    ui->scramEdit->setText("");
}
