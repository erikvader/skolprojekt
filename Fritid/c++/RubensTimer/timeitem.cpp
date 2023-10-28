#include "timeitem.h"

timeItem::timeItem(){

}

timeItem::timeItem(int time, int hundra, QString scram){
    Time = time;
    timeHund = hundra;
    scramble = scram.replace("@", "\n");

}

timeItem::timeItem(double time, QString scram){
    Time = floor(time);
    timeHund = round((time - Time)*100);
    scramble = scram.replace("@", "\n");
}

double timeItem::getTime(){
    double tempTime = 0;
    tempTime = Time;
    tempTime += (double) timeHund/100;
    return tempTime;
}

QString timeItem::getScramble(){
    return scramble;
}

QString timeItem::getScrambleSingleLine(){
    return scramble.replace("\n", "@");
}

QString timeItem::getTimeAsString(){
    QString final = "";
    int sekundEn = 0;
    int sekundTio = 0;
    int tiond = 0;
    int hundrad = 0;
    int minTio = 0;
    int minSec = 0;
    int copyOfTime = 0;

    if(timeHund >= 100){
        int toAdd = 0;
        toAdd += floor(timeHund/100);
        timeHund -= toAdd*100;
        Time += toAdd;
    }

    copyOfTime = Time;

    if(copyOfTime >= 60){ //fixa minuter! (om det finns några)
        minSec = floor(copyOfTime/60);
        copyOfTime -= minSec*60;
        if(minSec > 9){
            minTio = floor(minSec/10);
            minSec = minSec - minTio*10;
        }
    }

    sekundEn = copyOfTime;
    if(sekundEn >= 10){
        sekundTio = floor(sekundEn/10);
        sekundEn -= sekundTio*10;
    }

    hundrad = timeHund;
    if(hundrad >= 10){
        tiond = floor(hundrad/10);
        hundrad -= tiond*10;
    }

    final = QString::number(minTio)+QString::number(minSec)+":"+QString::number(sekundTio)+
            QString::number(sekundEn)+"."+QString::number(tiond)+QString::number(hundrad);

    return final;

}

bool timeItem::operator==(const timeItem &otherItem){
    bool success = false;

    if(Time == otherItem.Time && timeHund == otherItem.timeHund && scramble == otherItem.scramble)
        success = true;


    return success;
}
