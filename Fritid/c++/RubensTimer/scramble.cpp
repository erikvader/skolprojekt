#include "scramble.h"

scramble::scramble(){
    initScrambleIcons();
    qsrand(time(0));
}

QString scramble::get3x3Scramble(){
    return generateScrambleString(1, 25);
}

QString scramble::get2x2Scramble(){
    return generateScrambleString(0, 15);
}

QString scramble::get4x4Scramble(){
    return generateScrambleString(2, 40);
}

QString scramble::get5x5Scramble(){
    return generateScrambleString(2, 60);
}

QString scramble::get6x6Scramble(){
    return generateScrambleString(3, 80);
}

QString scramble::get7x7Scramble(){
    return generateScrambleString(3, 100);
}

QString scramble::get8x8Scramble(){
    return generateScrambleString(4, 120);
}

QString scramble::get9x9Scramble(){
    return generateScrambleString(4, 140);
}

QString scramble::get10x10Scramble(){
    return generateScrambleString(5, 160);
}

QString scramble::get11x11Scramble(){
    return generateScrambleString(5, 180);
}

QString scramble::getPyraminxScramble(){
    QString final = "";
    int randNum;
    QString icons[4][2] = {{"u", "u'"},{"r", "r'"},{"l", "l'"},{"b", "b'"}};

    final = generateScrambleString(6, 25, false);
    //qsrand(time(0));
    for(int i = 0; i < 4; i++){
        randNum = qrand()%3;
        if(randNum == 0){
            final += icons[i][0]+"  ";
        }else if(randNum == 1){
            final += icons[i][1]+"  ";
        }
    }

    return final;
}

QString scramble::getMegaminxScramble(){
    QString final = "";
    int randNum = 0;

    //qsrand(time(0));
    for(int i = 0; i < 7; i++){
        final += generateScrambleString(7, 10, false);
        randNum = qrand()%2;
        if(randNum == 0){
            final.append("U");
        }else{
            final.append("U'");
        }
        if(i != 6){
            final.append("\n");
        }

    }

    return final;
}

QString scramble::getSkewbScramble(){
    return generateScrambleString(6, 15, false);
}

void scramble::initScrambleIcons(){
    QList<QString> tempList;
    //2x2, ind 0
    tempList.clear();
    tempList.push_back("U");
    tempList.push_back("U'");
    tempList.push_back("R");
    tempList.push_back("R'");
    tempList.push_back("F");
    tempList.push_back("F'");
    scrambleIcons.push_back(tempList);
    //3x3, ind 1
    tempList = helpInitIcons(1);
    scrambleIcons.push_back(tempList);
    //4x4,5x5 ind 2
    scrambleIcons.push_back(helpInitIcons(2));
    //6x6, 7x7, ind 3
    scrambleIcons.push_back(helpInitIcons(3));
    //8x8, 9x9, ind 4
    scrambleIcons.push_back(helpInitIcons(4));
    //10x10, 11x11, ind 5
    scrambleIcons.push_back(helpInitIcons(5));
    //pyraminx, skewb ind 6
    tempList.clear();
    tempList.push_back("U");
    tempList.push_back("U'");
    tempList.push_back("R");
    tempList.push_back("R'");
    tempList.push_back("L");
    tempList.push_back("L'");
    tempList.push_back("B");
    tempList.push_back("B'");
    scrambleIcons.push_back(tempList);
    //megaminx, ind 7
    tempList.clear();
    tempList.push_back("R++");
    tempList.push_back("R--");
    tempList.push_back("D++");
    tempList.push_back("D--");
    scrambleIcons.push_back(tempList);


}
//3x3              4x4, 5x5     6x6, 7x7            8x8, 9x9             10x10, 11x11
//1 = ingen wedge, 2 = wedge, 3 = upp till 2wedge, 4 = upp till 3wedge, 5 = upp till 4 wedge
QList<QString> scramble::helpInitIcons(int a){
    QList<QString> tempList;
    QString toAdd = "";
    QString symbols[] = {"U", "D", "R", "L", "B", "F"};

    for(int i = 0; i < a; i++){
        for(int t = 0; t < 6; t++){
            toAdd = symbols[t];
            if(i != 0){
                toAdd += i == 1 ? "":QString::number(i);
                toAdd += "w";
            }
            tempList.push_back(toAdd);
            tempList.push_back(toAdd+"'");
        }
    }
    return tempList;
}

QString scramble::generateScrambleString(int index, int amount, bool doubles){
    QString final = "";
    int lastAdded = -1;
    int randNum = 0;
    //QString toAdd = "";
    int tblAmount = scrambleIcons[index].length();

    //qsrand(time(0));

    for(int i = 0; i < amount; i++){
        while(true){
            randNum = qrand()%tblAmount;
            if(lastAdded < 0){break;}
            if(randNum != lastAdded){
                bool even = (randNum % 2 == 0) ? true : false;
                if(even && randNum != (lastAdded-1)){
                    break;
                }else if(!even && randNum != (lastAdded+1)){
                    break;
                }
            }
        }
        lastAdded = randNum;
        final += scrambleIcons[index][randNum];
        if(doubles){
            randNum = qrand()%2;
            if(randNum == 0){
                if(final[final.length()-1] == 39){ // 39 = '
                    final.remove(final.length()-1,1);
                }
                final += "2";
            }
        }
        final += "  ";
    }

    return final;
}
