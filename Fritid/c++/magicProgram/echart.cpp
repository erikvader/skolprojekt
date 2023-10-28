#include "echart.h"

//Types: Histogramm, Pie, Dpie
//legendTypes: Round, Vertical

EChart::EChart(int x, int y, int wid, int hei, Nightcharts::type t){
    xpos = x;
    ypos = y;
    widt = wid;
    heig = hei;
    typ = t;
    mainChart = new Nightcharts;
    mainChart->setType(typ);
}

QRectF EChart::boundingRect() const{
    return QRectF(QRect(xpos, ypos, widt, heig));
}

//void EChart::addPiece(QString name, Qt::GlobalColor colour, float percentage){
//    mainChart.addPiece(name, colour, percentage);
//}

//void EChart::setLegendType(Nightcharts::legend_type t){
//    mainChart.setLegendType(t);
//}

Nightcharts * EChart::getChart(){
    return mainChart;
}

void EChart::paint(QPainter *painter, const QStyleOptionGraphicsItem *option, QWidget *widget){
    mainChart->setCords((double) xpos, (double) ypos, (double) widt, (double) heig);
    painter->translate(xpos,ypos);
    mainChart->draw(painter);
    if(showLegend){
        mainChart->drawLegend(painter);
    }


}

void EChart::reset(){
    mainChart = new Nightcharts;
    mainChart->setType(typ);
    mainChart->setLegendType(ltyp);
}

void EChart::setLegendType(Nightcharts::legend_type lty){
    ltyp = lty;
    mainChart->setLegendType(ltyp);
}

void EChart::setLegendType(bool enable){
    showLegend = enable;
}
