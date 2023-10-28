#include "timeitemcollection.h"

timeItemCollection::timeItemCollection(){

}

timeItemCollection::timeItemCollection(QList<timeItem> list){
    setTimesList(list);
}

double timeItemCollection::getAverage(){
    int totalItems = times.length();
    double avg = 0;
    double best = 0;
    double worst = 0;

    if(totalItems > 0){
        for(int i = 0; i < totalItems; i++){
            double theTime = times[i].getTime();
            avg += theTime;
            if(theTime < best || best == 0){
                best = theTime;
            }
            if(theTime > worst)
                worst = theTime;
        }
        avg /= totalItems;
    }

    return avg;
}

double timeItemCollection::get5Average(){
    int totalItems = times.length();
    double avg = 0;
    double best = 0;
    double worst = 0;


    if(totalItems >= 5){
        for(int i = 0; i < 5; i++){
            double theTime = times[i].getTime();
            avg += theTime;
            if(theTime < best || best == 0){
                best = theTime;
            }
            if(theTime > worst)
                worst = theTime;
        }
        avg -= (best+worst);
        avg /= 3;
    }

    return avg;
}

double timeItemCollection::get12Average(){
    int totalItems = times.length();
    double avg = 0;
    double best = 0;
    double worst = 0;

    if(totalItems >= 12){
        for(int i = 0; i < 12; i++){
            double theTime = times[i].getTime();
            avg += theTime;
            if(theTime < best || best == 0){
                best = theTime;
            }
            if(theTime > worst)
                worst = theTime;
        }
        avg -= (best+worst);
        avg /= 10;
    }

    return avg;
}

double timeItemCollection::getBest(){
    double best = 0;
    int totalItems = times.length();

    for(int i = 0; i < totalItems; i++){
        double theTime = times[i].getTime();
        if(theTime < best || best == 0){
            best = theTime;
        }
    }

    return best;
}

double timeItemCollection::getWorst(){
    double worst = 0;
    int totalItems = times.length();

    for(int i = 0; i < totalItems; i++){
        double theTime = times[i].getTime();
        if(theTime > worst)
            worst = theTime;
    }

    return worst;
}

void timeItemCollection::setTimesList(QList<timeItem> list){
    /*for(int i = 0; i < list.length(); i++){
        times.push_back(list.at(i));
    }*/
    times = list;
}

QList<timeItem> timeItemCollection::getTimesList(){
    return times;
}

QList<timeItem> timeItemCollection::getTimesListSorted(){
    return sortTimesList(times);
}

QList<timeItem> timeItemCollection::sortTimesList(QList<timeItem> &list){
    QList<timeItem> tempList = list;
    bool swapped = false;
    //int ran = 0;

    while(!swapped){
        swapped = true;
        for(int i = 1; i < tempList.count(); i++){
            if(tempList[i-1].getTime() > tempList[i].getTime()){
                tempList.swap(i-1, i);
                swapped = false;
            }
            //ran++;
        }
    }
    //printf("%d", ran);
    return tempList;
}
