#-------------------------------------------------
#
# Project created by QtCreator 2014-03-18T19:49:03
#
#-------------------------------------------------

QT       += core gui

greaterThan(QT_MAJOR_VERSION, 4): QT += widgets

TARGET = RubensTimer
CONFIG += c++11
TEMPLATE = app

LIBS += -lwinmm

SOURCES += main.cpp\
        mainwindow.cpp \
    timeitem.cpp \
    addnewtimewindow.cpp \
    scramble.cpp \
    timeitemcollection.cpp

HEADERS  += mainwindow.h \
    timeitem.h \
    addnewtimewindow.h \
    scramble.h \
    timeitemcollection.h

FORMS    += mainwindow.ui \
    addnewtimewindow.ui

OTHER_FILES +=

RESOURCES += \
    resurser.qrc
