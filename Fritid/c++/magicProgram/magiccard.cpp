#include "magiccard.h"

MagicCard::MagicCard(){

}

void MagicCard::setValues(QString name, QString colorlessMana, QString blackMana, QString blueMana,
                          QString whiteMana, QString greenMana, QString redMana, QString power,
                          QString toughness, QString color, QString type,
                          QString cardText, QString rarity, QString set){
    this->Name = name;
    this->Power = power;
    this->Toughness = toughness;
    this->Color = color;
    this->Type = type;
    this->CardText = cardText;
    this->Set = set;
    this->Rarity = rarity;
    this->ColorlessMana = colorlessMana;
    this->BlackMana = blackMana;
    this->BlueMana = blueMana;
    this->WhiteMana = whiteMana;
    this->GreenMana = greenMana;
    this->RedMana = redMana;

}

QString MagicCard::getCardTextInOneLine(){
    QString returnString = CardText;

    returnString.replace("\n", ";", Qt::CaseInsensitive);

    return returnString;
}

QString MagicCard::TransformCardTextFromOneLine(QString str){
    QString returnString = str;

    returnString.replace(";", "\n", Qt::CaseInsensitive);

    return returnString;
}

QString MagicCard::getManaCosts(){
    QString manC = "";
    if(ColorlessMana != "0"){
        manC += ColorlessMana;
        manC += ",";
    }
    if(BlackMana != "0"){
        manC += BlackMana + "B,";
    }
    if(RedMana != "0"){
        manC += RedMana + "R,";
    }
    if(GreenMana != "0"){
        manC += GreenMana + "G,";
    }
    if(BlueMana!= "0"){
        manC += BlueMana + "Bl,";
    }
    if(WhiteMana!= "0"){
        manC += WhiteMana + "W,";
    }
    return manC;
}

QList<QString> MagicCard::getAsTable(){
    QList<QString> array;

    array.push_back(Name); //0
    array.push_back(Power); //1
    array.push_back(Toughness); //2
    array.push_back(Color); //3
    array.push_back(Type); //4
    array.push_back(CardText); //5
    array.push_back(Set); //6
    array.push_back(Rarity); //7
    array.push_back(ColorlessMana); //8
    array.push_back(BlackMana); //9
    array.push_back(BlueMana); //10
    array.push_back(WhiteMana); //11
    array.push_back(GreenMana); //12
    array.push_back(RedMana); //13
    //Amount //14
    array.push_back(this->getConvertedManaCost());//15

    return array;
}

QString MagicCard::getConvertedManaCost(){
    int b = BlackMana.toInt();
    int bl = BlueMana.toInt();
    int r = RedMana.toInt();
    int g = GreenMana.toInt();
    int w = WhiteMana.toInt();
    int c = ColorlessMana.toInt();

    int sum = b+bl+r+g+w+c;

    return QString::number(sum);
}


