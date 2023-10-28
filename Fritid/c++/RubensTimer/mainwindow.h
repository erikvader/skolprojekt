#ifndef MAINWINDOW_H
#define MAINWINDOW_H

#include <QMainWindow>
#include <QTimer>
#include <QKeyEvent>
#include <QHash>
#include <QListWidgetItem>
#include "timeitem.h"
#include <QMessageBox>
#include "addnewtimewindow.h"
#include <qfiledialog.h>
#include <fstream>
//#include <vector>
#include <QList>
#include <string>
#include <windows.h>
#include <mmsystem.h>
#include <QPalette>
#include <QtCore>
#include <QtGui>
#include "scramble.h"
#include "timeitemcollection.h"

using namespace std;

namespace Ui {
class MainWindow;
}

class MainWindow : public QMainWindow
{
    Q_OBJECT

public:
    explicit MainWindow(QWidget *parent = 0);
    ~MainWindow();
    void UpdateTimer();//sätter nuvarande tiden på LCD grejerna
    void evaluateTime();
    void UpdateTimerSurprise();//LCDgrejerna blinkar
    void add(int hundradelar);
    void startTheTimer();
    void stopTheTimer();
    void resetTheTimer();
    void getNewScramble();
    void purgeTimer();
    void load(/*char *URL*/string URL);
    void save(/*char *URL*/string URL);
    void displayTime(timeItem *tItem);
    void addTime(timeItem *tItem, int index);
    void switchTimes();
    void checkIfPb();
    void setLCDColor(Qt::GlobalColor color);
    void setBGPicture(int index = -1);

    enum average {treAvFem, TioAvTolv};

private slots:
    void onTimerTick();
    void on_startStopButton_clicked();
    void on_cubeChooser_currentIndexChanged(int index);
    void on_delBut_clicked();
    void on_infoBut_clicked();
    void on_actionAdd_time_triggered();
    void addNewTime(int sec, int hundreths, QString scramble);
    void on_actionSave_triggered();
    void on_actionThis_triggered();//clear
    void on_actionAll_triggered();//clear
    void on_inspectionBox_toggled(bool checked);
    void on_soundBox_toggled(bool checked);
    void on_surpriseBox_toggled(bool checked);
    void setPB(int sec, int hund, QString scramble, int index = -1);
    void setBestAvg(int index, QList<timeItem> times, average x);
    void on_actionRemove_all_Personal_Bests_triggered();
    void stopSound(){PlaySound(NULL, NULL, SND_ASYNC);}
    void on_actionRemove_Best_10_12_triggered();
    void on_actionShow_Best_10_12_triggered();
    void on_actionRemove_Best_avg3_5_triggered();
    void on_actionShow_Best_avg3_5_triggered();
    void on_actionSet_Best_Single_triggered();
    void on_actionRemove_Best_Single_triggered();
    void on_actionShow_Best_Single_triggered();
    void on_addAvg35_clicked();
    void on_addAvg1012_clicked();

protected:
    void keyPressEvent(QKeyEvent *event);
    void paintEvent(QPaintEvent *);

private:
    Ui::MainWindow *ui;
    void initScrambleIcons();
    QString generateScrambleString(int index, int amount, int tblAmount, bool doubles = true);
    void addTimeToList();
    void fixList();
    void fixAverages();
    void initTimesMap(); //reset

    addNewTimeWindow *win;
    addNewTimeWindow *PBwin;

    int latestCube = 1;
    bool inspection = false;
    bool sound = false;
    bool surprise = false;
    bool curInspecting = false;
    string saveFile = "data\\data.txt";
    int itemsInList;
    int Time;
    int timeHund;
    QTimer *timer;
    bool timerStarted;
    QHash<QListWidgetItem*, timeItem*> timeMap;
    QHash<int, QList<timeItem*>* > times;
    QHash<int, timeItem*> PBs;
    QHash<int, timeItemCollection*> bestAvg5;
    QHash<int, timeItemCollection*> bestAvg12;

    QImage bgPicture;
    scramble scram;

};

#endif // MAINWINDOW_H
