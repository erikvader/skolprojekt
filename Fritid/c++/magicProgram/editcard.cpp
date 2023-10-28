#include "editcard.h"
#include "ui_editcard.h"
#include "mainwindow.h"

EditCard::EditCard(QWidget *parent) :
    QFrame(parent),
    ui(new Ui::EditCard)
{
    ui->setupUi(this);

    intToColor[0] = "Colorless";
    intToColor[1] = "Black";
    intToColor[2] = "Blue";
    intToColor[3] = "Green";
    intToColor[4] = "Red";
    intToColor[5] = "White";
    intToColor[6] = "Multicolored";

}

EditCard::~EditCard()
{
    delete ui;
}

void EditCard::setCardController(CardController *c){
    controller = c;
}

void EditCard::edit(QListWidgetItem *item){
    MagicCard *tempCard = controller->getCard(item);

    ui->nameEdit->setText(tempCard->Name);
    ui->cardTextEdit->setPlainText(tempCard->CardText);
    ui->TypeLineEdit->setText(tempCard->Type);
    ui->SetEdit->setText(tempCard->Set);
    ui->RarityComboBox->setCurrentText(tempCard->Rarity);
    ui->ChooseColorComboBox->setCurrentIndex(intToColor.key(tempCard->Color));
    ui->PowerComboBox->setCurrentText(tempCard->Power);
    ui->ToughnessComboBox->setCurrentText(tempCard->Toughness);
    ui->RedComboBox->setCurrentText(tempCard->RedMana);
    ui->blueComboBox->setCurrentText(tempCard->BlueMana);
    ui->GreenComboBox->setCurrentText(tempCard->GreenMana);
    ui->BlackComboBox->setCurrentText(tempCard->BlackMana);
    ui->ColorlessComboBox->setCurrentText(tempCard->ColorlessMana);
    ui->WhiteComboBox->setCurrentText(tempCard->WhiteMana);

    editingItem = item;
    editing = true;
    this->show();

}


void EditCard::on_AddButton_clicked(){
    MagicCard *tempCard = new MagicCard();

    tempCard->setValues(ui->nameEdit->text(),ui->ColorlessComboBox->currentText(),
                        ui->BlackComboBox->currentText(),
                        ui->blueComboBox->currentText(),
                        ui->WhiteComboBox->currentText(),
                        ui->GreenComboBox->currentText(),
                        ui->RedComboBox->currentText(),
                        ui->PowerComboBox->currentText(),
                        ui->ToughnessComboBox->currentText(),
                        intToColor[ui->ChooseColorComboBox->currentIndex()],
                        ui->TypeLineEdit->text(),ui->cardTextEdit->toPlainText(),
                        ui->RarityComboBox->currentText(),ui->SetEdit->text());


    if(editing){
        controller->replace(editingItem, tempCard);
    }else{
        controller->add(tempCard);
    }
    this->close();

}
