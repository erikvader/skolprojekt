#ifndef MAINWINDOW_H
#define MAINWINDOW_H

#include <QMainWindow>
#include <QtCore>
#include <QtGui>
#include <QGraphicsScene>
#include <echart.h>
#include <QListWidgetItem>
#include <iostream>
#include "magiccard.h"

namespace Ui {
class MainWindow;
}

class MainWindow : public QMainWindow
{
    Q_OBJECT

public:
    explicit MainWindow(QWidget *parent = 0);
    ~MainWindow();
    void UpdateAll();

private slots:
    void on_DatabaseList_itemDoubleClicked(QListWidgetItem *item);

    void on_DataBaseAddButton_clicked();

    void on_DataBaseDeleteButton_clicked();

    void on_DatabaseSortByNameButton_clicked();

    void on_DatabaseSortByColorButton_clicked();

    void on_DatabaseSortByManaButton_clicked();

    void on_actionSave_2_triggered();

    void on_MoveButton_clicked();

    void on_DeleteButton_clicked();

    void on_DeckSortByNameButton_clicked();

    void on_DeckSortByColorButton_clicked();

    void on_DeckSortByManaButton_clicked();

    void on_actionNew_triggered();

    void on_actionOpen_triggered();

    void on_actionSave_triggered();

    void on_incLandsCheckBox_clicked();

    void on_rarityIncludeLands_clicked();

    void on_UpdateMenuButton_triggered();

    void on_typeCheckBox_clicked();

private:
    Ui::MainWindow *ui;
    QGraphicsScene *ManaCurveScene;
    EChart *ManaCurveChart;
    QGraphicsScene *ColorScene;
    EChart *ColorChart;
    QGraphicsScene *TypeScene;
    EChart *TypeChart;
};



#endif // MAINWINDOW_H
