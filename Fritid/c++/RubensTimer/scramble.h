#ifndef SCRAMBLE_H
#define SCRAMBLE_H

#include <QString>
#include <time.h>
#include <QList>

class scramble{
public:
    scramble();
    QString get2x2Scramble();
    QString get3x3Scramble();
    QString get4x4Scramble();
    QString get5x5Scramble();
    QString get6x6Scramble();
    QString get7x7Scramble();
    QString get8x8Scramble();
    QString get9x9Scramble();
    QString get10x10Scramble();
    QString get11x11Scramble();
    QString getPyraminxScramble();
    QString getMegaminxScramble();
    QString getSkewbScramble();
private:
    void initScrambleIcons();
    QList<QString> helpInitIcons(int a);
    QString generateScrambleString(int index, int amount, bool doubles = true);

    //QString scrambleIcons[24][20];
    QList<QList<QString> > scrambleIcons;
};




#endif // SCRAMBLE_H
