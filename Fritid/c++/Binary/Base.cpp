#include "Base.h"
#include <iostream>
#include <string>
#include <cmath>
#include <sstream>
#include <hash_map>

using namespace std;
using namespace __gnu_cxx;

string values[36] = {
"0", "1", "2", "3", "4", "5", "6" , "7", "8", "9", "A", "B",
"C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M",
 "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"
};

hash_map<char, int> values2;

template <class N>
Base<N>::Base(){
    values2['0'] = 0; //Super ineffective!
    values2['1'] = 1;
    values2['2'] = 2;
    values2['3'] = 3;
    values2['4'] = 4;
    values2['5'] = 5;
    values2['6'] = 6;
    values2['7'] = 7;
    values2['8'] = 8;
    values2['9'] = 9;
    values2['A'] = 10;
    values2['B'] = 11;
    values2['C'] = 12;
    values2['D'] = 13;
    values2['E'] = 14;
    values2['F'] = 15;
    values2['G'] = 16;
    values2['H'] = 17;
    values2['I'] = 18;
    values2['J'] = 19;
    values2['K'] = 20;
    values2['L'] = 21;
    values2['M'] = 22;
    values2['N'] = 23;
    values2['O'] = 24;
    values2['P'] = 25;
    values2['Q'] = 26;
    values2['R'] = 27;
    values2['S'] = 28;
    values2['T'] = 29;
    values2['U'] = 30;
    values2['V'] = 31;
    values2['W'] = 32;
    values2['X'] = 33;
    values2['Y'] = 34;
    values2['Z'] = 35;


}

template <class N>
string Base<N>::toBase(int base, N number){

    string result = "";
    int startValue = 0;
    N toAdd = 0;
    N takeAway = 0;
    int b = base;
    N n = number;

    while(power(b, startValue) <= n){
        startValue++;
    }
    if(startValue > 0){startValue--; }
    while(startValue >= 0){
        for(N i = 0; i < (b-1); i++){
            takeAway = power(b, startValue);
            n -= takeAway;

            if(n >= 0){
                toAdd++;
            }else{
                n += takeAway;
                break;
            }
        }

        result += values[toAdd];
        startValue--;
        toAdd = 0;
    }

    return result;
}

template <class N>
N Base<N>::toNumber(int base, string number){

    N result = 0;
    int startValue = number.length()-1;
    int readValue = 0;
    char curChar = ' ';
    N curInt = 0;

    while(startValue >= 0){

        curChar = number[readValue];
        curInt = values2[curChar];
        result += power(base, startValue)*curInt;

        readValue++;
        startValue--;

    }

    return result;
}

template <class N>
string Base<N>::baseToBase(string number, int fromBase, int toBase){
    N conversionInt = 0;
    string returnString = "";
    conversionInt = toNumber(fromBase, number);
    returnString = Base::toBase(toBase, conversionInt);
    return returnString;
}

template <class N>
N Base<N>::power(N base, N exponent){
    N result = 1;
    for(N i = 0; i < exponent; i++){
        result *= base;
    }
    return result;
}

template class Base<int>;
template class Base<long>;
template class Base<long long>;

