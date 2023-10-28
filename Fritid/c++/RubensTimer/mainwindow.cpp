#include "mainwindow.h"
#include "ui_mainwindow.h"
#include <iostream>
#include <cmath>
#include <ctime>




MainWindow::MainWindow(QWidget *parent) :
    QMainWindow(parent),
    ui(new Ui::MainWindow)
{
    ui->setupUi(this);
    timerStarted = false;

    timer = new QTimer(this);
    connect(timer, SIGNAL(timeout()),this,SLOT(onTimerTick()));

    getNewScramble();
    resetTheTimer();

    ui->timesList->setFont(QFont("Arial", 15));

    win = new addNewTimeWindow;
    connect(win, SIGNAL(okButtonClicked(int,int,QString)), this, SLOT(addNewTime(int,int,QString)));
    PBwin = new addNewTimeWindow;
    connect(PBwin, SIGNAL(okButtonClicked(int,int,QString)), this, SLOT(setPB(int,int,QString)));

    initTimesMap();

    load(saveFile);
    if(latestCube == 0){on_cubeChooser_currentIndexChanged(0);}else{ui->cubeChooser->setCurrentIndex(latestCube);}

    ui->inspectionBox->setChecked(inspection);
    ui->soundBox->setChecked(sound);
    ui->surpriseBox->setChecked(surprise);
}

MainWindow::~MainWindow()
{
    save(saveFile);
    delete ui;
    delete win;
    delete PBwin;
}

void MainWindow::initTimesMap(){ //reset
    times.clear();
    for(int i = 0; i < ui->cubeChooser->count();i++){
        times[i] = new QList<timeItem*>;
    }
}

void MainWindow::evaluateTime(){
    if(timeHund >= 100){
        int toAdd = 0;
        toAdd += floor(timeHund/100);
        timeHund -= toAdd*100;
        Time += toAdd;
    }

    if(timeHund < 0){
        int toTakeaway = abs(timeHund);
        timeHund = 100-toTakeaway;
        Time--;

    }
}

void MainWindow::UpdateTimerSurprise(){
    if(Time%2 == 0){
        setLCDColor(Qt::red);
    }else{
        setLCDColor(Qt::black);
    }
}

void MainWindow::UpdateTimer(){//kör som vanligt
    int sekundEn = 0;
    int sekundTio = 0;
    int tiond = 0;
    int hundrad = 0;
    int minTio = 0;
    int minSec = 0;
    int copyOfTime = 0;

    copyOfTime = Time;

    if(copyOfTime >= 60){ //fixa minuter! (om det finns några)
        minSec = floor(copyOfTime/60);
        copyOfTime -= minSec*60;
        if(minSec > 9){
            minTio = floor(minSec/10);
            minSec = minSec - minTio*10;
        }
    }

    sekundEn = copyOfTime;
    if(sekundEn >= 10){
        sekundTio = floor(sekundEn/10);
        sekundEn -= sekundTio*10;
    }

    hundrad = timeHund;
    if(hundrad >= 10){
        tiond = floor(hundrad/10);
        hundrad -= tiond*10;
    }

    ui->sekundSec->display(sekundEn);
    ui->sekundTen->display(sekundTio);
    ui->minutSec->display(minSec);
    ui->minutTen->display(minTio);
    ui->tiondelar->display(tiond);
    ui->hundradelar->display(hundrad);

}

void MainWindow::add(int hundradelar){
    timeHund += hundradelar;
}

void MainWindow::startTheTimer(){
    if(!timer->isActive()){
        timer->start(10);
    }
}

void MainWindow::stopTheTimer(){
    timer->stop();
}

void MainWindow::resetTheTimer(){
    Time = 0;
    timeHund = 0;

    ui->sekundSec->display(0);
    ui->sekundTen->display(0);
    ui->minutSec->display(0);
    ui->minutTen->display(0);
    ui->tiondelar->display(0);
    ui->hundradelar->display(0);
}

void MainWindow::setLCDColor(Qt::GlobalColor color){
    QPalette pal;
    pal.setBrush(QPalette::Window, color);
    pal.setBrush(QPalette::WindowText, color);
    //pal.setBrush(QPalette::, Qt::white);

    ui->sekundSec->setPalette(pal);
    ui->sekundTen->setPalette(pal);
    ui->minutSec->setPalette(pal);
    ui->minutTen->setPalette(pal);
    ui->tiondelar->setPalette(pal);
    ui->hundradelar->setPalette(pal);
    ui->dotLabel->setPalette(pal);
    ui->kolonLabel->setPalette(pal);

    /*QColor col(color);

    QString styleSheet = "color: rgb("+QString::number(col.red())+","+QString::number(col.green())
                                     +","+QString::number(col.blue())+") ; border: 1px solid #ffffff;";
    ui->dotLabel->setStyleSheet(styleSheet);
    */
}

void MainWindow::onTimerTick(){
    if(curInspecting){
        add(-1);
    }else{
        add(1);
    }
    evaluateTime();
    if(surprise && !curInspecting){
        UpdateTimerSurprise();
    }else{
        UpdateTimer();
    }
    if(curInspecting){
        if(Time < 0){
            if(sound){PlaySound(TEXT("sounds\\inspection_ran_out.wav"),NULL, SND_ASYNC | SND_FILENAME);}
            curInspecting = false;
            stopTheTimer();
            addNewTime(18000, 0, "DNF");
            getNewScramble();
            resetTheTimer();
            //setLCDColor(Qt::black);
        }else if(Time == 7 && timeHund == 0){ //8 sec
            if(sound){PlaySound(TEXT("sounds\\at_8_sec.wav"),NULL, SND_ASYNC | SND_FILENAME);}
        }else if(Time == 3 && timeHund == 0){ // 12 sec
            if(sound){PlaySound(TEXT("sounds\\at_12_sec.wav"),NULL, SND_ASYNC | SND_FILENAME);}
        }
    }
}


void MainWindow::on_startStopButton_clicked(){
    if(timerStarted){//stannar timern
        stopTheTimer();
        //addTimeToList();
        addNewTime(Time, timeHund, ui->scramble->toPlainText());
        checkIfPb();
        getNewScramble();
        if(surprise){UpdateTimer();setLCDColor(Qt::black);}
        //fixAverages();
        timerStarted = false;
    }else if(timerStarted == false && inspection == true && curInspecting == false){//startar med inspektion
        resetTheTimer();
        curInspecting = true;
        Time = 15;
        setLCDColor(Qt::red);
        startTheTimer();
        if(sound){PlaySound(TEXT("sounds\\inspectionStarted.wav"), NULL, SND_FILENAME | SND_ASYNC);}

    }else{//startar
        curInspecting = false;
        setLCDColor(Qt::black);
        resetTheTimer();
        startTheTimer();
        if(sound){PlaySound(TEXT("sounds\\timerStarted.wav"), NULL, SND_FILENAME | SND_ASYNC);}
        timerStarted = true;
    }
}

void MainWindow::keyPressEvent(QKeyEvent *event){
    if(event->key() == Qt::Key_Space){
        on_startStopButton_clicked();
    }
}

/*void MainWindow::addTimeToList(){
    QListWidgetItem *item = new QListWidgetItem;
    QString text;

    text = QString::number(++itemsInList) + "; ";
    text.append(QString::number(ui->minutTen->value()));
    text.append(QString::number(ui->minutSec->value()) + ":");
    text.append(QString::number(ui->sekundTen->value()));
    text.append(QString::number(ui->sekundSec->value()) + ".");
    text.append(QString::number(ui->tiondelar->value()));
    text.append(QString::number(ui->hundradelar->value()));

    item->setText(text);
    ui->timesList->insertItem(0, item);

    timeItem *tempTimeItem = new timeItem(Time, timeHund, ui->scramble->toPlainText());
    timeMap[item] = tempTimeItem;

}*/

void MainWindow::fixList(){

    int totalItems = ui->timesList->count()-1;
    itemsInList = 0;

    for(int i = totalItems; i > -1; i--){
        QString oldText;
        oldText = ui->timesList->item(i)->text();
        int ind = oldText.indexOf(";");
        oldText.replace(0,ind,QString::number(++itemsInList));
        ui->timesList->item(i)->setText(oldText);
    }
}

void MainWindow::fixAverages(){
    int totalItems = ui->timesList->count();
    int curInd = ui->cubeChooser->currentIndex();

    QList<timeItem> tempList;
    //if(totalItems > 12)
        //totalItems = 12;
    for(int i = 0; i < totalItems ; i++)
        tempList.push_back(*timeMap[ui->timesList->item(i)]);

    timeItemCollection temp(tempList);

    ui->labTotal->setText(timeItem(temp.getAverage(),"").getTimeAsString());
    ui->labWorst->setText(timeItem(temp.getWorst(),"").getTimeAsString());
    ui->labBest->setText(timeItem(temp.getBest(),"").getTimeAsString());
    ui->lab35->setText(timeItem(temp.get5Average(),"").getTimeAsString());
    ui->lab1012->setText(timeItem(temp.get12Average(),"").getTimeAsString());

    double PBsTime = 0;
    double avg35 = 0;
    double avg1012 = 0;

    if(PBs.contains(curInd)){
        PBsTime = PBs[curInd]->getTime();
    }

    if(bestAvg5.contains(curInd)){
        avg35 = bestAvg5[curInd]->get5Average();
    }

    if(bestAvg12.contains(curInd)){
        avg1012 = bestAvg12[curInd]->get12Average();
    }

    ui->labTotalBest->setText(timeItem(PBsTime, "").getTimeAsString());
    ui->labTotalAvg5->setText(timeItem(avg35, "").getTimeAsString());
    ui->labTotalAvg12->setText(timeItem(avg1012, "").getTimeAsString());


}

void MainWindow::getNewScramble(){
    int curInd = ui->cubeChooser->currentIndex();
    QString finalScramble = "";

    switch(curInd){
    case 0: case 18:
        finalScramble = scram.get2x2Scramble();
        break;
    case 1: case 10: case 11: case 12:
        finalScramble = scram.get3x3Scramble();
        break;
    case 2: case 19:
        finalScramble = scram.get4x4Scramble();
        break;
    case 3: case 20:
        finalScramble = scram.get5x5Scramble();
        break;
    case 4:
        finalScramble = scram.get6x6Scramble();
        break;
    case 5:
        finalScramble = scram.get7x7Scramble();
        break;
    case 6:
        finalScramble = scram.get8x8Scramble();
        break;
    case 7:
        finalScramble = scram.get9x9Scramble();
        break;
    case 8:
        finalScramble = scram.get10x10Scramble();
        break;
    case 9:
        finalScramble = scram.get11x11Scramble();
        break;
    case 14:
        finalScramble = scram.getPyraminxScramble();
        break;
    case 15:
        finalScramble = scram.getSkewbScramble();
        break;
    case 13:
        finalScramble = scram.getMegaminxScramble();
        break;


    }

    ui->scramble->setText(finalScramble);
}

void MainWindow::purgeTimer(){
    resetTheTimer();
    ui->timesList->clear();
    itemsInList = 0;
    timeMap.clear();
    getNewScramble();
    fixAverages();
}

void MainWindow::load(string URL){
    char of[URL.size()];
    strcpy(of, URL.c_str());
    ifstream stream;
    stream.open(of);

    string input;
    getline(stream, input);
    latestCube = stoi(input);
    getline(stream, input);
    inspection = input == "1" ? true:false;
    getline(stream, input);
    sound = input == "1" ? true:false;
    getline(stream, input);
    surprise = input == "1" ? true:false;

    int onIndex = -1;
    int scramTem = 0;
    string latestScram;
    double latestTime;

    while(getline(stream, input) && input != "PBs:"){
        if(input == "index:"+to_string(onIndex+1)){
            onIndex++;
        }else{
            if(scramTem == 0){
                latestTime = stod(input);
                scramTem = 1;
            }else{
                latestScram = input;
                times[onIndex]->push_back(new timeItem(latestTime, QString::fromStdString(latestScram)));
                scramTem = 0;
            }
        }
    }

    //onIndex = -1;
    while(getline(stream, input) && input != "avg5:"){
        onIndex = stoi(input.substr(4, input.length()-4));
        //cout << onIndex << endl;
        getline(stream, input);
        latestTime = stod(input);
        getline(stream, latestScram);
        PBs[onIndex] = new timeItem(latestTime, QString::fromStdString(latestScram));
    }

    stream.close();
}

void MainWindow::save(string URL){
    char sf[URL.size()];
    strcpy(sf, URL.c_str());
    ofstream stream;
    stream.open(sf);

    stream << latestCube << endl;
    stream << inspection << endl;
    stream << sound << endl;
    stream << surprise << endl;
    for(int i = 0; i < ui->cubeChooser->count(); i++){
        stream << "index:"+to_string(i) << endl;
        for(int a = 0; a < times[i]->size();a++){
            stream << times[i]->at(a)->getTime() << endl;
            stream << times[i]->at(a)->getScrambleSingleLine().toStdString() << endl;
        }
    }
    stream << "PBs:" << endl;
    QList<int> keys = PBs.keys();
    for(int i = 0; i < keys.count();i++){
        stream << "key:"+to_string(keys[i]) << endl;
        stream << PBs[keys[i]]->getTime() << endl;
        stream << PBs[keys[i]]->getScrambleSingleLine().toStdString() << endl;
    }

    stream << "avg5:" << endl;
    keys = bestAvg5.keys();
    QList<timeItem> timeList;
    for(int i = 0; i < bestAvg5.count();i++){
        stream << "key:"+to_string(keys[i]) << endl;
        timeList = bestAvg5[keys[i]]->getTimesList();
        for(int j = 0; j < timeList.count(); j++){
            stream << timeList[j].getTime() << endl;
            stream << timeList[j].getScrambleSingleLine().toStdString() << endl;
        }
    }

    stream << "avg12:" << endl;
    keys = bestAvg12.keys();
    for(int i = 0; i < bestAvg12.count();i++){
        stream << "key:"+to_string(keys[i]) << endl;
        timeList = bestAvg12[keys[i]]->getTimesList();
        for(int j = 0; j < timeList.count(); j++){
            stream << timeList[j].getTime() << endl;
            stream << timeList[j].getScrambleSingleLine().toStdString() << endl;
        }
    }

    stream.close();
}

void MainWindow::on_cubeChooser_currentIndexChanged(int index){
    //ui->scramble->setText(QString::number(index));
    latestCube = index;
    switchTimes();
    setBGPicture();
}

void MainWindow::on_delBut_clicked(){
    if(!ui->timesList->selectedItems().isEmpty()){
        QListWidgetItem *item = ui->timesList->currentItem();
        //delete timeMap[item];
        times[ui->cubeChooser->currentIndex()]->removeOne(timeMap[item]);
        timeMap.remove(item);
        delete item;
        fixList();
        fixAverages();
    }
}

void MainWindow::on_infoBut_clicked(){
    if(!ui->timesList->selectedItems().isEmpty()){
        QListWidgetItem *item = ui->timesList->currentItem();
        timeItem *t = timeMap[item];
        QMessageBox msgBox;
        msgBox.setText("Time: " + t->getTimeAsString());
        msgBox.setInformativeText("Scramble:\n" + t->getScramble());
        msgBox.exec();
    }
}

void MainWindow::on_actionAdd_time_triggered(){ 
   win->show();

}

/*void MainWindow::addNewTime(int sec, int hundreths, QString scramble){
    timeItem *tItem = new timeItem(sec, hundreths,scramble);
    QListWidgetItem *item = new QListWidgetItem;

    item->setText(QString::number(++itemsInList) + "; " + tItem->getTimeAsString());
    timeMap[item] = tItem;
    ui->timesList->insertItem(0,item);

    fixAverages();
}*/

void MainWindow::addNewTime(int sec, int hundreths, QString scramble){
    timeItem *it = new timeItem(sec, hundreths, scramble);
    addTime(it, ui->cubeChooser->currentIndex());
    displayTime(it);
}

void MainWindow::displayTime(timeItem *tItem){
    QListWidgetItem *item = new QListWidgetItem;

    item->setText(QString::number(++itemsInList) + "; " + tItem->getTimeAsString());
    timeMap[item] = tItem;
    ui->timesList->insertItem(0,item);

    fixAverages();
}

void MainWindow::addTime(timeItem *tItem, int index){
    times[index]->push_back(tItem);
}

void MainWindow::switchTimes(){
    purgeTimer();
    int curInd = ui->cubeChooser->currentIndex();

    for(int i = 0; i < times[curInd]->size();i++){
        displayTime(times[curInd]->at(i));
    }

}

void MainWindow::checkIfPb(){
    double oldPB = -1;

    if(PBs.contains(ui->cubeChooser->currentIndex())){
        oldPB = PBs[ui->cubeChooser->currentIndex()]->getTime();
    }

    double newPB = (double) Time + ( (double) timeHund/100);
    if(newPB < oldPB || oldPB == -1){
        setPB(Time, timeHund, ui->scramble->toPlainText());
        if(sound){PlaySound(TEXT("sounds\\PB.wav"), NULL, SND_FILENAME | SND_ASYNC);}
        QMessageBox box;
        box.setText("You got a new Personal best!");
        box.exec();
        connect(&box, SIGNAL(destroyed()),SLOT(stopSound()));
    }
}

void MainWindow::on_actionSave_triggered(){
    QString url = QFileDialog::getSaveFileName(this,"Save","","Times Files(*.txt);;All Files(*)");
    char temp[url.size()];
    strcpy(temp,url.toStdString().c_str());
    //save(temp);


}

/*void MainWindow::on_actionLoad_triggered(){
    QString url = QFileDialog::getOpenFileName(this,"Load","","Times Files(*.txt);;All Files(*)");
    char temp[url.size()];
    strcpy(temp,url.toStdString().c_str());
    load(temp);


}*/

void MainWindow::on_actionThis_triggered(){//clear
    purgeTimer();
    times[ui->cubeChooser->currentIndex()] = new QList<timeItem*>;
}

void MainWindow::on_actionAll_triggered(){//clear
    purgeTimer();
    initTimesMap(); //reset
}

void MainWindow::on_inspectionBox_toggled(bool checked){
    //cout << checked << endl;
    inspection = checked;
}

void MainWindow::on_soundBox_toggled(bool checked){
    sound = checked;
}

void MainWindow::on_surpriseBox_toggled(bool checked){
    surprise = checked;
}

void MainWindow::setPB(int sec, int hund, QString scramble, int index){ //single
    int curInd = index;
    if(index == -1){
        curInd = ui->cubeChooser->currentIndex();
    }
    PBs[curInd] = new timeItem(sec, hund, scramble);
    fixAverages();
}

void MainWindow::on_actionRemove_all_Personal_Bests_triggered(){
    on_actionRemove_Best_Single_triggered();
    on_actionRemove_Best_10_12_triggered();
    on_actionRemove_Best_avg3_5_triggered();
    //fixAverages();
}

void MainWindow::setBGPicture(int index){
    if(index == -1){
        index = ui->cubeChooser->currentIndex();
    }
    if(bgPicture.load(":/bgPictures/"+QString::number(index))){
        bgPicture = bgPicture.scaled(this->size(),Qt::IgnoreAspectRatio, Qt::SmoothTransformation);
    }else{
        bgPicture.load(":/bgPictures/other");
        bgPicture = bgPicture.scaled(this->size(),Qt::IgnoreAspectRatio, Qt::SmoothTransformation);
    }
    this->update();
}


void MainWindow::paintEvent(QPaintEvent *){

    QPainter painter(this);
    painter.fillRect(QRectF(QPoint(0, 0), this->size()), Qt::white);
    if(!bgPicture.isNull()){
        painter.drawImage(0, 0, bgPicture);
    }
    /*cout << to_string(picture.rect().x()) << to_string(picture.rect().y())
            << to_string(picture.rect().width()) << to_string(picture.rect().height()) << endl;*/

}

void MainWindow::setBestAvg(int index, QList<timeItem> times, MainWindow::average x){

    timeItemCollection *temp = new timeItemCollection(times);

    if(x == average::treAvFem){
        bestAvg5[index] = temp;
    }else if(x == average::TioAvTolv){
        bestAvg12[index] = temp;
    }

    fixAverages();
}

void MainWindow::on_actionRemove_Best_10_12_triggered(){
    bestAvg12.remove(ui->cubeChooser->currentIndex());
    fixAverages();
}

void MainWindow::on_actionRemove_Best_avg3_5_triggered(){
    bestAvg5.remove(ui->cubeChooser->currentIndex());
    fixAverages();
}

void MainWindow::on_actionShow_Best_10_12_triggered(){
    int curInd = ui->cubeChooser->currentIndex();
    QMessageBox box;
    timeItemCollection *tempCollection;
    QList<timeItem> tempList;
    //timeItem best;
    //timeItem worst;
    QString text = "";

    if(bestAvg12.contains(curInd)){
        tempCollection = bestAvg12[curInd];
        tempList = tempCollection->getTimesListSorted();


        text = "Your personal best 10/12 average on the "+ui->cubeChooser->currentText()+" is:\n"
                    +timeItem(tempCollection->get5Average(),"").getTimeAsString()+"\n\n";

        for(int i = 0; i < tempList.count(); i++){
            text.append(tempList[i].getTimeAsString()+"\t");
        }

        box.setText(text);

    }else{
        box.setText("You don't have a personal best 10/12 average on the "+ui->cubeChooser->currentText());
    }

    box.exec();
}

void MainWindow::on_actionShow_Best_avg3_5_triggered(){
    int curInd = ui->cubeChooser->currentIndex();
    QMessageBox box;
    timeItemCollection *tempCollection;
    QList<timeItem> tempList;
    //timeItem best;
    //timeItem worst;
    QString text = "";

    if(bestAvg5.contains(curInd)){
        tempCollection = bestAvg5[curInd];
        tempList = tempCollection->getTimesListSorted();


        text = "Your personal best 3/5 average on the "+ui->cubeChooser->currentText()+" is:\n"
                    +timeItem(tempCollection->get5Average(),"").getTimeAsString()+"\n\n";

        for(int i = 0; i < tempList.count(); i++){
            text.append(tempList[i].getTimeAsString()+"\t");
        }

        box.setText(text);

    }else{
        box.setText("You don't have a personal best 3/5 average on the "+ui->cubeChooser->currentText());
    }

    box.exec();
}

void MainWindow::on_addAvg35_clicked(){
    int totalItems = ui->timesList->count();
    int curInd = ui->cubeChooser->currentIndex();
    QList<timeItem> tempList;

    if(totalItems >= 5){
        for(int i = 0; i < 5; i++){
            tempList.push_back(*timeMap[ui->timesList->item(i)]);
        }
    }

    setBestAvg(curInd, tempList, average::treAvFem);
    //fixAverages();

}

void MainWindow::on_addAvg1012_clicked(){
    int totalItems = ui->timesList->count();
    int curInd = ui->cubeChooser->currentIndex();
    QList<timeItem> tempList;

    if(totalItems >= 12){
        for(int i = 0; i < 12; i++){
            tempList.push_back(*timeMap[ui->timesList->item(i)]);
        }
    }

    setBestAvg(curInd, tempList, average::TioAvTolv);
    //fixAverages();
}

void MainWindow::on_actionSet_Best_Single_triggered(){
    PBwin->show();
}

void MainWindow::on_actionShow_Best_Single_triggered(){
    int curInd = ui->cubeChooser->currentIndex();
    QMessageBox box;
    if(PBs.contains(curInd)){
        box.setText("Your Personal Best on the "+ui->cubeChooser->currentText()+" is:\n"+
                PBs[curInd]->getTimeAsString()+"\n"+
                PBs[curInd]->getScramble()
                );
    }else{
        box.setText("You don't have a Personal Best on the "+ui->cubeChooser->currentText());
    }
    box.exec();
}

void MainWindow::on_actionRemove_Best_Single_triggered(){
    int curInd = ui->cubeChooser->currentIndex();
    PBs.remove(curInd);
    fixAverages();
}
