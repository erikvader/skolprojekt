#ifndef ECHART_H
#define ECHART_H

#include <QGraphicsItem>
#include <QPainter>
#include "nightcharts.h"

class EChart : public QGraphicsItem
{
public:

    EChart(int x, int y, int wid, int hei, Nightcharts::type t);
    QRectF boundingRect() const;
    void paint(QPainter *painter, const QStyleOptionGraphicsItem *option, QWidget *widget);
//    void addPiece(QString name, Qt::GlobalColor colour, float percentage);
//    void setLegendType(Nightcharts::legend_type t);
    Nightcharts * getChart();
    void reset();
    void setLegendType(Nightcharts::legend_type lty);
    void setLegendType(bool enable);
private:
    Nightcharts::type typ;
    Nightcharts::legend_type ltyp;
    Nightcharts *mainChart;
    int xpos = 0;
    int ypos = 0;
    int widt = 0;
    int heig = 0;
    bool showLegend = true;


};

#endif // ECHART_H
