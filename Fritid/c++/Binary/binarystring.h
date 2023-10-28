#ifndef BINARYSTRING_H
#define BINARYSTRING_H

#include <string>

using namespace std;

class BinaryString
{
public:
    BinaryString();
    string toBinary(string text);
    string toText(string binary);
};

#endif // BINARYSTRING_H
