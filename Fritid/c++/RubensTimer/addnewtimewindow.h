#ifndef ADDNEWTIMEWINDOW_H
#define ADDNEWTIMEWINDOW_H

#include <QDialog>
#include <QListWidget>

namespace Ui {
class addNewTimeWindow;
}

class addNewTimeWindow : public QDialog
{
    Q_OBJECT

public:
    explicit addNewTimeWindow(QWidget *parent = 0);
    ~addNewTimeWindow();


private slots:
    void on_okButton_clicked();

    void on_cancelButton_clicked();

signals:
    void okButtonClicked(int sek, int hundre, QString scram);

private:
    Ui::addNewTimeWindow *ui;

    void resetStuff();

};

#endif // ADDNEWTIMEWINDOW_H
