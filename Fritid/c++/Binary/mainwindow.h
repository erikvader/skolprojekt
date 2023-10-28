#ifndef MAINWINDOW_H
#define MAINWINDOW_H

#include <QMainWindow>

namespace Ui {
class MainWindow;
}

class MainWindow : public QMainWindow
{
    Q_OBJECT

public:
    int curModeIndex;
    explicit MainWindow(QWidget *parent = 0);
    ~MainWindow();

private slots:
    void on_ModeSelector_currentIndexChanged(int index);

    void on_GoButton_clicked();

    void on_SwapButton_clicked();

    void on_ClearButton_clicked();

private:
    Ui::MainWindow *ui;
    void initStuff();
    void initBaseChoosers();
};

#endif // MAINWINDOW_H
