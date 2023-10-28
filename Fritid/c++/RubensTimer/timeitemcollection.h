#ifndef TIMEITEMCOLLECTION_H
#define TIMEITEMCOLLECTION_H

#include "timeitem.h"
#include <QList>
#include <QString>

class timeItemCollection{//average

public:
    timeItemCollection();
    timeItemCollection(QList<timeItem> list);

    double getAverage();
    double get5Average();
    double get12Average();
    double getBest();
    double getWorst();

    void setTimesList(QList<timeItem> list);
    QList<timeItem> getTimesList();
    QList<timeItem> getTimesListSorted();
    QList<timeItem> sortTimesList(QList<timeItem> &list);

private:

    QList<timeItem> times;

};

#endif // TIMEITEMCOLLECTION_H
