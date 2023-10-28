#ifndef CARDCONTROLLER_H
#define CARDCONTROLLER_H

#include <QListWidget>
#include <QListWidgetItem>
#include "magiccard.h"
#include <QHash>
#include <QList>
#include <fstream>
#include <string>

using namespace std;

class CardController
{
public:
    CardController(bool careForAmount = false);
    bool CareForAmount;
    void setList(QListWidget *list);
    void add(MagicCard *card);
    void remove(QListWidgetItem *item);
    void removeSpecial(QListWidgetItem *item);
    void removeAll();
    void fixText(QListWidgetItem *item, MagicCard *card);
    MagicCard * getCard(QListWidgetItem *item);
    void setFont(QFont f);
    void save(char *URL);
    void save(QString saveFile);
    void load(char *URL);
    void load(QString loadFile);
    void replace(QListWidgetItem *item, MagicCard *card);
    enum sortOptions {Color, Name, Mana};
    void sortItems(CardController::sortOptions so);
    QList<QList<QString> > getAllCardsAsTables();
    int getCardAmount();
private:
    QString fixAddText(QString string, int spaces);
    QListWidget *myList;
    QHash<QListWidgetItem*, MagicCard*> theMap;
    QList<QListWidgetItem*> allItems;
    QFont font;
    bool sorted = false;
    QListWidgetItem *contains(QString name);
    QHash<QListWidgetItem*, int> cardAmount;
};

#endif // CARDCONTROLLER_H
