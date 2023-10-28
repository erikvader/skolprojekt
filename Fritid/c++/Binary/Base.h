#ifndef BASE_H
#define BASE_H

#include <string>

using namespace std;

template <class N>
class Base
{
    public:
        Base();
        string toBase(int base, N number);
        N toNumber(int base, string number);
        string baseToBase(string number, int fromBase, int toBase);
    protected:
    private:
        N power(N, N);
};

#endif // BASE_H

