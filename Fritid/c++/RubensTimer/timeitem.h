#ifndef TIMEITEM_H
#define TIMEITEM_H

#include <QString>
#include <cmath>

class timeItem{
public:
    timeItem();
    timeItem(int time, int hundra, QString scram);
    timeItem(double time, QString scram);
    double getTime();
    QString getScramble();
    QString getScrambleSingleLine();
    QString getTimeAsString();
    bool operator ==(const timeItem &otherItem);

private:
    int Time;
    int timeHund;
    QString scramble;
};

#endif // TIMEITEM_H
