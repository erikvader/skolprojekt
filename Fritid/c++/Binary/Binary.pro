#-------------------------------------------------
#
# Project created by QtCreator 2014-01-15T20:10:42
#
#-------------------------------------------------

QT       += core gui

greaterThan(QT_MAJOR_VERSION, 4): QT += widgets

TARGET = Binary
TEMPLATE = app


SOURCES += main.cpp\
        mainwindow.cpp \
    Base.cpp \
    binarystring.cpp 

HEADERS  += mainwindow.h \
    Base.h \
    binarystring.h

FORMS    += mainwindow.ui

OTHER_FILES +=

RESOURCES += \
    icons.qrc
