#ifndef EDITCARD_H
#define EDITCARD_H

#include "magiccard.h"
#include "mainwindow.h"
#include <QHash>
#include "cardcontroller.h"

#include <QFrame>

namespace Ui {
class EditCard;
}

class EditCard : public QFrame
{
    Q_OBJECT

public:
    explicit EditCard(QWidget *parent = 0);
    ~EditCard();
    void setCardController(CardController *c);
    void edit(QListWidgetItem *item);
private slots:
    void on_AddButton_clicked();

private:
    Ui::EditCard *ui;
    CardController *controller;
    QHash<int, QString> intToColor;
    bool editing = false;
    QListWidgetItem *editingItem;
};

#endif // EDITCARD_H
