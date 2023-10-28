#-------------------------------------------------
#
# Project created by QtCreator 2014-01-25T18:16:21
#
#-------------------------------------------------

QT       += core gui

greaterThan(QT_MAJOR_VERSION, 4): QT += widgets

TARGET = magicProgram
TEMPLATE = app


SOURCES += main.cpp\
        mainwindow.cpp \
    echart.cpp \
    nightcharts.cpp \
    cardcontroller.cpp \
    magiccard.cpp \
    editcard.cpp

HEADERS  += mainwindow.h \
    echart.h \
    nightcharts.h \
    cardcontroller.h \
    magiccard.h \
    editcard.h

FORMS    += mainwindow.ui \
    editcard.ui

RESOURCES += \
    Icons.qrc

OTHER_FILES += \
    DatabaseSave.txt
