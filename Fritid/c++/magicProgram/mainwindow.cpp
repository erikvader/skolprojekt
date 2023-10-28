#include "mainwindow.h"
#include "ui_mainwindow.h"
#include "cardcontroller.h"
#include "magiccard.h"
#include <iostream>
#include "editcard.h"
#include <QFileDialog>

using namespace std;

CardController DatabaseCardController;
CardController DeckCardController(true);
EditCard *editcard;
char databaseSaveFile[_MAX_PATH+1];
string databaseSaveFileName = "DatabaseSave.txt";
//QList<MagicCard*> allCards;

MainWindow::MainWindow(QWidget *parent) :
    QMainWindow(parent),
    ui(new Ui::MainWindow)
{
    ui->setupUi(this);
    this->setFixedSize(this->size());

    QFont font("Monospace");
    font.setStyleHint(QFont::TypeWriter);
    ui->DatabaseLabel->setText("Amount |Name\t\t\t\t|Mana cost  |Power  |Color\t  |Type\t\t\t|Card text");
    ui->DatabaseLabel->setFont(font);
    ui->DeckLabel->setText("Amount |Name\t\t\t\t|Mana cost  |Power  |Color\t  |Type\t\t\t|Card text");
    ui->DeckLabel->setFont(font);
    DatabaseCardController.setList(ui->DatabaseList);
    DatabaseCardController.setFont(font);
    DeckCardController.setList(ui->DeckList);
    DeckCardController.setFont(font);
    //fixa URLEN!
    GetModuleFileNameA(NULL,databaseSaveFile,_MAX_PATH);
    string final(databaseSaveFile);
    int toStart = final.find("magicProgram.exe");
    final.replace(toStart, 16, databaseSaveFileName);
    strcpy(databaseSaveFile, final.c_str());
    //SLUT PÅ FIXA URLEN!
    DatabaseCardController.load(databaseSaveFile);

    ColorChart = new EChart(-50,-30,100,100,Nightcharts::Pie);
    ColorChart->setLegendType(Nightcharts::Vertical);
    ColorScene = new QGraphicsScene();
    ui->ColorGV->setScene(ColorScene);
    ColorScene->addItem(ColorChart);

    TypeChart = new EChart(-50,-30,100,100,Nightcharts::Pie);
    TypeChart->setLegendType(Nightcharts::Vertical);
    TypeScene = new QGraphicsScene();
    ui->TypeChartGV->setScene(TypeScene);
    TypeScene->addItem(TypeChart);

    ManaCurveChart = new EChart(-100,0,450,150,Nightcharts::Histogramm);
    ManaCurveChart->setLegendType(false);
    ManaCurveScene = new QGraphicsScene();
    ui->ManaCurveGV->setScene(ManaCurveScene);
    ManaCurveScene->addItem(ManaCurveChart);
    QGraphicsTextItem *numbas = new QGraphicsTextItem;
    numbas->setPos(-200,150);
    numbas->setPlainText("    0       1       2       3       4       5       6      7       8       9+");
    numbas->setFont(QFont("Arial",14));
    ManaCurveScene->addItem(numbas);

    UpdateAll();
}

MainWindow::~MainWindow()
{
    delete ui;
    delete editcard;
    DatabaseCardController.save(databaseSaveFile);
}

void MainWindow::on_DatabaseList_itemDoubleClicked(QListWidgetItem *item){
    editcard = new EditCard();
    editcard->setCardController(&DatabaseCardController);
    editcard->edit(item);
}

void MainWindow::on_DataBaseAddButton_clicked(){
    editcard = new EditCard();
    editcard->setCardController(&DatabaseCardController);
    editcard->show();

}

void MainWindow::on_DataBaseDeleteButton_clicked()
{
    //delete in allCards;
    DatabaseCardController.remove(ui->DatabaseList->currentItem());
}

void MainWindow::on_DatabaseSortByNameButton_clicked(){
    DatabaseCardController.sortItems(CardController::Name);
}

void MainWindow::on_DatabaseSortByColorButton_clicked(){
    DatabaseCardController.sortItems(CardController::Color);
}

void MainWindow::on_DatabaseSortByManaButton_clicked(){
    DatabaseCardController.sortItems(CardController::Mana);
}

void MainWindow::on_actionSave_2_triggered(){
    DatabaseCardController.save(databaseSaveFile);
}

void MainWindow::on_MoveButton_clicked(){
    DeckCardController.add(DatabaseCardController.getCard(ui->DatabaseList->currentItem()));
    UpdateAll();
}

void MainWindow::on_DeleteButton_clicked(){

    DeckCardController.removeSpecial(ui->DeckList->currentItem());
    UpdateAll();
}

void MainWindow::on_DeckSortByNameButton_clicked(){
    DeckCardController.sortItems(CardController::Name);
}

void MainWindow::on_DeckSortByColorButton_clicked(){
    DeckCardController.sortItems(CardController::Color);
}

void MainWindow::on_DeckSortByManaButton_clicked(){
    DeckCardController.sortItems(CardController::Mana);
}

void MainWindow::on_actionNew_triggered(){
    DeckCardController.removeAll();
    ui->DisplayDeckNameLabel->setText("");
    UpdateAll();
}

void MainWindow::on_actionOpen_triggered(){
    QString loadDir = QFileDialog::getOpenFileName(this,"Load","","Deck save files(*.txt);;All Files(*)");
    if(loadDir != ""){
        DeckCardController.removeAll();
        DeckCardController.load(loadDir);
        loadDir = loadDir.mid(loadDir.lastIndexOf("/")+1);
        loadDir.remove(".txt");
        ui->DisplayDeckNameLabel->setText(loadDir);
        UpdateAll();
    }
}

void MainWindow::on_incLandsCheckBox_clicked(){
    UpdateAll();
}

void MainWindow::on_rarityIncludeLands_clicked(){
    UpdateAll();
}

void MainWindow::on_typeCheckBox_clicked(){
    UpdateAll();
}

void MainWindow::on_actionSave_triggered(){
    QString saveDir = QFileDialog::getOpenFileName(this,"Save","","Deck save files(*.txt);;All Files(*)");
    DeckCardController.save(saveDir);
}

void MainWindow::UpdateAll(){
    QList<QList<QString> > allCards = DeckCardController.getAllCardsAsTables();
    int cardsSize = allCards.size();
    //cardamounts
    ui->CardAmountLabel2->setText(QString::number(DeckCardController.getCardAmount()));
    //end cardamounts
    int curBlackLand = 0;
    int curBlueLand = 0;
    int curRedLand = 0;
    int curGreenLand = 0;
    int curWhiteLand = 0;
    int curOvrigtLand = 0;
    int curColorlessLand = 0;
    int curMulticoloredLand = 0;
    int RedNonLand = 0;
    int BlueNonLand = 0;
    int BlackNonLand = 0;
    int WhiteNonLand = 0;
    int GreenNonLand = 0;
    int ColorlessNonLand = 0;
    int MulticoloredNonLand = 0;
    int commons = 0;
    int uncommons = 0;
    int rares = 0;
    int mythics = 0;
    int totalLands = 0;
    int creatures = 0;
    int artifacts = 0;
    int enchantments = 0;
    int instants = 0;
    int planeswalkers = 0;
    int sorceries = 0;
    int mana0 = 0;
    int mana1 = 0;
    int mana2 = 0;
    int mana3 = 0;
    int mana4 = 0;
    int mana5 = 0;
    int mana6 = 0;
    int mana7 = 0;
    int mana8 = 0;
    int mana9p = 0;
    for(int i = 0; i < cardsSize; i++){
        QList<QString> curCard = allCards[i];
        //kollar lands & färg
        if(curCard[4].contains("Land", Qt::CaseInsensitive)){
            totalLands+=curCard[14].toInt();
            if(curCard[3] == "Black"){
                curBlackLand+=curCard[14].toInt();
            }else if(curCard[3] == "Blue"){
                curBlueLand+=curCard[14].toInt();
            }else if(curCard[3] == "Red"){
                curRedLand+=curCard[14].toInt();
            }else if(curCard[3] == "Green"){
                curGreenLand+=curCard[14].toInt();
            }else if(curCard[3] == "White"){
                curWhiteLand+=curCard[14].toInt();
            }else if(curCard[3] == "Colorless"){
                curColorlessLand+=curCard[14].toInt();
            }else if(curCard[3] == "Multicolored"){
                curMulticoloredLand += curCard[14].toInt();
            }
        }else{
            if(curCard[3] == "Black"){
                BlackNonLand+=curCard[14].toInt();
            }else if(curCard[3] == "Blue"){
                BlueNonLand+=curCard[14].toInt();
            }else if(curCard[3] == "Red"){
                RedNonLand+=curCard[14].toInt();
            }else if(curCard[3] == "Green"){
                GreenNonLand+=curCard[14].toInt();
            }else if(curCard[3] == "White"){
                WhiteNonLand+=curCard[14].toInt();
            }else if(curCard[3] == "Colorless"){
                ColorlessNonLand+=curCard[14].toInt();
            }else if(curCard[3] == "Multicolored"){
                MulticoloredNonLand+=curCard[14].toInt();
            }
        }
        //end på kollar lands & färg
        //[rarity]
        if(ui->rarityIncludeLands->isChecked()){
            if(curCard[7] == "Common"){
                commons += curCard[14].toInt();
            }else if(curCard[7] == "Uncommon"){
                uncommons += curCard[14].toInt();
            }else if(curCard[7] == "Rare"){
                rares += curCard[14].toInt();
            }else if(curCard[7] == "Mythic Rare"){
                mythics += curCard[14].toInt();
            }
        }else{
            if(!curCard[4].contains("Land", Qt::CaseInsensitive)){
                if(curCard[7] == "Common"){
                    commons += curCard[14].toInt();
                }else if(curCard[7] == "Uncommon"){
                    uncommons += curCard[14].toInt();
                }else if(curCard[7] == "Rare"){
                    rares += curCard[14].toInt();
                }else if(curCard[7] == "Mythic Rare"){
                    mythics += curCard[14].toInt();
                }
            }
        }
        //[/rarity]
        //[type]
        if(!ui->typeCheckBox->isChecked()){
            if(curCard[4].contains("Creature", Qt::CaseInsensitive)){
                creatures+=curCard[14].toInt();
            }
            if(curCard[4].contains("Artifact", Qt::CaseInsensitive)){
                artifacts+=curCard[14].toInt();
            }
            if(curCard[4].contains("Planeswalker", Qt::CaseInsensitive)){
                planeswalkers+=curCard[14].toInt();
            }
            if(curCard[4].contains("Enchantment", Qt::CaseInsensitive)){
                enchantments+=curCard[14].toInt();
            }
            if(curCard[4].contains("Instant", Qt::CaseInsensitive)){
                instants+=curCard[14].toInt();
            }
            if(curCard[4].contains("Sorcery", Qt::CaseInsensitive)){
                sorceries+=curCard[14].toInt();
            }
        }else{
            if(curCard[4].contains("Creature", Qt::CaseInsensitive)){
                creatures+=curCard[14].toInt();
            }else if(curCard[4].contains("Artifact", Qt::CaseInsensitive)){
                artifacts+=curCard[14].toInt();
            }else if(curCard[4].contains("Planeswalker", Qt::CaseInsensitive)){
                planeswalkers+=curCard[14].toInt();
            }else if(curCard[4].contains("Enchantment", Qt::CaseInsensitive)){
                enchantments+=curCard[14].toInt();
            }else if(curCard[4].contains("Instant", Qt::CaseInsensitive)){
                instants+=curCard[14].toInt();
            }else if(curCard[4].contains("Sorcery", Qt::CaseInsensitive)){
                sorceries+=curCard[14].toInt();
            }
        }
        //[/type]
        //[mana]
        if(!curCard[4].contains("Land",Qt::CaseInsensitive)){
        if(curCard[15] == "0"){
            mana0+=curCard[14].toInt();
        }else if(curCard[15] == "1"){
            mana1+=curCard[14].toInt();
        }else if(curCard[15] == "2"){
            mana2+=curCard[14].toInt();
        }else if(curCard[15] == "3"){
            mana3+=curCard[14].toInt();
        }else if(curCard[15] == "4"){
            mana4+=curCard[14].toInt();
        }else if(curCard[15] == "5"){
            mana5+=curCard[14].toInt();
        }else if(curCard[15] == "6"){
            mana6+=curCard[14].toInt();
        }else if(curCard[15] == "7"){
            mana7+=curCard[14].toInt();
        }else if(curCard[15] == "8"){
            mana8+=curCard[14].toInt();
        }else if(curCard[15].toInt() >= 9){
            mana9p+=curCard[14].toInt();
        }
        }
        //[/mana]
    }
    curOvrigtLand = curColorlessLand+curMulticoloredLand;
    if(ui->incLandsCheckBox->isChecked()){
        BlackNonLand += curBlackLand;
        BlueNonLand += curBlueLand;
        RedNonLand += curRedLand;
        GreenNonLand += curGreenLand;
        WhiteNonLand += curWhiteLand;
        ColorlessNonLand += curColorlessLand;
        MulticoloredNonLand += curMulticoloredLand;
    }
    ui->RedCur->setText(QString::number(curRedLand));
    ui->BlackCur->setText(QString::number(curBlackLand));
    ui->BlueCur->setText(QString::number(curBlueLand));
    ui->ColorleCur->setText(QString::number(curOvrigtLand));
    ui->WhiteCur->setText(QString::number(curWhiteLand));
    ui->GreenCur->setText(QString::number(curGreenLand));

    float total = RedNonLand+BlueNonLand+BlackNonLand+WhiteNonLand+GreenNonLand+ColorlessNonLand+MulticoloredNonLand;
    ColorChart->reset();
    ColorChart->getChart()->addPiece("Black",Qt::black,(BlackNonLand/total)*100);
    ColorChart->getChart()->addPiece("Red",Qt::red,(RedNonLand/total)*100);
    ColorChart->getChart()->addPiece("Blue",Qt::blue,(BlueNonLand/total)*100);
    ColorChart->getChart()->addPiece("White",Qt::white,(WhiteNonLand/total)*100);
    ColorChart->getChart()->addPiece("Green",Qt::green,(GreenNonLand/total)*100);
    ColorChart->getChart()->addPiece("Colorless",Qt::gray,(ColorlessNonLand/total)*100);
    ColorChart->getChart()->addPiece("Multicolored",Qt::yellow,(MulticoloredNonLand/total)*100);
    ColorScene->update();

    total = totalLands+creatures+artifacts+enchantments+instants+planeswalkers+sorceries;
    TypeChart->reset();
    TypeChart->getChart()->addPiece("Land",Qt::green, (totalLands/total)*100);
    TypeChart->getChart()->addPiece("Creature",Qt::black, (creatures/total)*100);
    TypeChart->getChart()->addPiece("Artifact",Qt::gray, (artifacts/total)*100);
    TypeChart->getChart()->addPiece("Enchantment",Qt::yellow, (enchantments/total)*100);
    TypeChart->getChart()->addPiece("Instant",Qt::blue, (instants/total)*100);
    TypeChart->getChart()->addPiece("Planeswalker",Qt::white, (planeswalkers/total)*100);
    TypeChart->getChart()->addPiece("Sorcery",Qt::red, (sorceries/total)*100);
    TypeScene->update();

    ManaCurveChart->reset();
    ManaCurveChart->getChart()->addPiece("0",Qt::blue,mana0);
    ManaCurveChart->getChart()->addPiece("1",Qt::blue,mana1);
    ManaCurveChart->getChart()->addPiece("2",Qt::blue,mana2);
    ManaCurveChart->getChart()->addPiece("3",Qt::blue,mana3);
    ManaCurveChart->getChart()->addPiece("4",Qt::blue,mana4);
    ManaCurveChart->getChart()->addPiece("5",Qt::blue,mana5);
    ManaCurveChart->getChart()->addPiece("6",Qt::blue,mana6);
    ManaCurveChart->getChart()->addPiece("7",Qt::blue,mana7);
    ManaCurveChart->getChart()->addPiece("8",Qt::blue,mana8);
    ManaCurveChart->getChart()->addPiece("9+",Qt::blue,mana9p);
    ManaCurveScene->update();

    ui->planeswalkerCount->setText(QString::number(planeswalkers));
    ui->landCount->setText(QString::number(totalLands));
    ui->creatureCount->setText(QString::number(creatures));
    ui->artifactCount->setText(QString::number(artifacts));
    ui->enchantmentCount->setText(QString::number(enchantments));
    ui->instantCount->setText(QString::number(instants));
    ui->sorceryCount->setText(QString::number(sorceries));

    ui->CommonLab2->setText(QString::number(commons));
    ui->UncommonLab2->setText(QString::number(uncommons));
    ui->RareLab2->setText(QString::number(rares));
    ui->MythicLab2->setText(QString::number(mythics));
}

void MainWindow::on_UpdateMenuButton_triggered(){
    UpdateAll();
}
