#include "binarystring.h"
#include <string>
#include <Base.h>
#include <iostream>

using namespace std;

BinaryString::BinaryString(){

}

string BinaryString::toBinary(string text){
    string result = "";
    int length = text.length();
    char workChar = ' ';
    int inten = 0;
    string workString = "";

    Base<int> b;
    for(int i = 0; i < length ; i++){
        workChar = text[i];
        inten = int(workChar);
        workString = b.toBase(2, inten);
        while(workString.length() < 8){
            workString = "0" + workString;
        }

        result += workString;
    }
    return result;
}

string BinaryString::toText(string binary){
    string result = "";
    int length = binary.length()/8;
    int inten = 0;

    Base<int> b;
    for(int i = 0; i<length; i++){
        string workString = "";
        for(int a = i*8; a<(i*8)+8;a++){
            workString += binary[a];
        }
        workString = workString.substr(1, 8);
        inten = b.toNumber(2, workString);
        result += char(inten);
    }

    return result;
}
