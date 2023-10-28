#include "cardcontroller.h"

using namespace std;

CardController::CardController(bool careForAmount){
    CareForAmount = careForAmount;
}

void CardController::setFont(QFont f){
    font = f;
}

void CardController::save(char *URL){
    ofstream saveFile;
    saveFile.open(URL);

    if(saveFile.is_open()){
        for(int i = 0; i < allItems.size(); i++){
            MagicCard *curCard = getCard(allItems[i]);

            saveFile << curCard->Name.toStdString() + "[";  //0
            saveFile << curCard->Color.toStdString() + "["; //1
            saveFile << curCard->BlackMana.toStdString() + "["; //2
            saveFile << curCard->BlueMana.toStdString() + "["; //3
            saveFile << curCard->RedMana.toStdString() + "["; //4
            saveFile << curCard->GreenMana.toStdString() + "["; //5
            saveFile << curCard->WhiteMana.toStdString() + "["; //6
            saveFile << curCard->ColorlessMana.toStdString() + "["; //7
            saveFile << curCard->Power.toStdString() + "["; //8
            saveFile << curCard->Toughness.toStdString() + "["; //9
            saveFile << curCard->getCardTextInOneLine().toStdString() + "["; //10
            saveFile << curCard->Rarity.toStdString() + "["; //11
            saveFile << curCard->Set.toStdString() + "["; //12
            saveFile << curCard->Type.toStdString() + "["; //13
            if(CareForAmount){
                saveFile << QString::number(cardAmount[allItems[i]]).toStdString() + "["; //14
            }
            saveFile << endl;
        }
    }
    saveFile.close();
}

void CardController::save(QString saveFile){
    char tempThing[saveFile.size()];
    strcpy(tempThing,saveFile.toStdString().c_str());
    save(tempThing);
}

void CardController::load(char *URL){
    ifstream saveFile;
    saveFile.open(URL);

    if(saveFile.is_open()){
        string readString = "";
        QString stringTbl[15];
        MagicCard *tempCard;
        while(getline(saveFile, readString)){
            for(int i = 0; i < 15; i ++){
                int firstPos = readString.find_first_of("[");
                string workString = readString.substr(0, firstPos);
                stringTbl[i] = QString::fromStdString(workString);
                readString.erase(0, firstPos+1);
            }
            tempCard = new MagicCard;
            tempCard->setValues(stringTbl[0],stringTbl[7],stringTbl[2],
                                stringTbl[3],stringTbl[6],stringTbl[5],
                                stringTbl[4],stringTbl[8],stringTbl[9],
                                stringTbl[1],stringTbl[13],
                                tempCard->TransformCardTextFromOneLine(stringTbl[10]),
                                stringTbl[11],stringTbl[12]);

            add(tempCard);
            if(CareForAmount){
                cardAmount[allItems.at(allItems.size()-1)] = stringTbl[14].toInt();
                fixText(allItems.at(allItems.size()-1),tempCard);
            }
        }
    }
    saveFile.close();
}

void CardController::load(QString loadFile){
    char tempThing[loadFile.size()];
    strcpy(tempThing,loadFile.toStdString().c_str());
    load(tempThing);
}

void CardController::replace(QListWidgetItem *item, MagicCard *card){
    theMap.remove(item);
    theMap[item] = card;
    fixText(item, card);
}

void CardController::setList(QListWidget *list){
    myList = list;
}

void CardController::add(MagicCard *card){
    if(CareForAmount){
        QListWidgetItem *testItem = contains(card->Name);
        if(testItem != NULL){
            cardAmount[testItem]++;
            fixText(testItem, theMap[testItem]);
        }else{
            //samma sak som under
            int curInt = -1;
            allItems.push_back(new QListWidgetItem);
            curInt = allItems.length()-1;
            cardAmount[allItems.at(curInt)] = 1;
            fixText(allItems.at(curInt), card);
            theMap[allItems.at(curInt)] = card;
            myList->addItem(allItems.at(curInt));
        }
    }else{
        //samma sak som ovan
        int curInt = -1;
        allItems.push_back(new QListWidgetItem);
        curInt = allItems.length()-1;
        fixText(allItems.at(curInt), card);
        theMap[allItems.at(curInt)] = card;
        myList->addItem(allItems.at(curInt));
    }
}

QListWidgetItem *CardController::contains(QString name){

    for(int i = 0; i < allItems.size(); i++){
        if(theMap[allItems[i]]->Name == name){
            return allItems[i];
        }
    }

    return NULL;
}

void CardController::remove(QListWidgetItem *item){
    theMap.remove(item);
    allItems.removeOne(item);
    cardAmount.remove(item);
    delete item;
}

void CardController::removeSpecial(QListWidgetItem *item){
    if(cardAmount[item] > 1){
        cardAmount[item]--;
        fixText(item, theMap[item]);
    }else if(cardAmount[item] == 1){
        remove(item);
    }
}

void CardController::removeAll(){
    int size = allItems.size()-1;
    for(int i = size; i >= 0; i--){
        remove(allItems[i]);
    }
}

void CardController::fixText(QListWidgetItem *item, MagicCard *card){
    QString buildString = "";
    if(!CareForAmount){
        buildString += fixAddText("0",6);
    }else{
        buildString += fixAddText(QString::number(cardAmount[item]), 6);
    }
    buildString += fixAddText(card->Name, 31);
    buildString += fixAddText(card->getManaCosts(), 10);
    buildString += fixAddText(card->Power+"/"+card->Toughness, 6);
    buildString += fixAddText(card->Color, 12);
    buildString += fixAddText(card->Type, 20);
    buildString += fixAddText(card->getCardTextInOneLine(), 69);


    item->setText(buildString);
    item->setFont(font);
}

QString CardController::fixAddText(QString string, int spaces){
    QString finalString = string;

    if(finalString.length() > spaces){
        while(finalString.length()+2 > spaces){
            finalString.remove(finalString.length()-1, finalString.length()-1);
        }
        finalString += "...";
    }else{
        while(finalString.length()-1 < spaces){
            finalString += " ";
        }
    }
    finalString += "|";

    return finalString;
}

MagicCard * CardController::getCard(QListWidgetItem *item){
    return theMap[item];
}

void CardController::sortItems(CardController::sortOptions so){
    for(int i = 0; i<allItems.size();i++){
        MagicCard *tempCard = getCard(allItems[i]);
        if(so == CardController::Color){
            allItems[i]->setText(tempCard->Color);
        }else if(so == CardController::Name){
            allItems[i]->setText(tempCard->Name);
        }else if(so == CardController::Mana){
            allItems[i]->setText(tempCard->getConvertedManaCost());
        }
    }
    if(sorted){
        sorted = false;
        myList->sortItems(Qt::AscendingOrder);
    }else{
        sorted = true;
        myList->sortItems(Qt::DescendingOrder);
    }

    for(int i = 0; i<allItems.size();i++){
        MagicCard *tempCard = getCard(allItems[i]);
        fixText(allItems[i], tempCard);
    }
}

QList<QList<QString> > CardController::getAllCardsAsTables(){
    int size = allItems.size();
    QList<QList<QString> > array;

    for(int i = 0; i < size; i++){
        array.push_back(getCard(allItems[i])->getAsTable());
        array[i].insert(14,QString::number(cardAmount[allItems[i]]));
    }

    return array;
}

int CardController::getCardAmount(){
    int amount = 0;

    for(int i = 0; i < allItems.size(); i++){
        amount += cardAmount[allItems[i]];
    }

    return amount;
}
