#ifndef MAGICCARD_H
#define MAGICCARD_H

#include <QString>
#include <QList>

class MagicCard
{
public:
    MagicCard();
    void setValues(QString name, QString colorlessMana, QString blackMana, QString blueMana,
                   QString whiteMana, QString greenMana, QString redMana, QString power,
                   QString toughness, QString color, QString type,
                   QString cardText, QString rarity = " ", QString set = " ");
    QString Name, ColorlessMana, BlackMana, BlueMana, WhiteMana, GreenMana, RedMana,
            Color, Type, CardText, Power, Toughness, Rarity, Set;
    QString getCardTextInOneLine();
    QString TransformCardTextFromOneLine(QString str);
    QString getManaCosts();
    QList<QString> getAsTable();
    QString getConvertedManaCost();
};

#endif // MAGICCARD_H
