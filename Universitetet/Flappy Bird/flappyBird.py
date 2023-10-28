import pygame
import random
import math
from bird import Bird
from bar import Bar
from background import Background
from pipe import Pipe


#init fönster o grejer--------------------------------------------
pygame.init()

displayWidth = 800
displayHeight = 500 #int(displayWidth/1.6) #500

gameDisplay = pygame.display.set_mode((displayWidth, displayHeight))
pygame.display.set_caption("Gustav och Co. Flaxande Fågel")
clock = pygame.time.Clock()

running = True #om hela programmet ska köras
displayMenu = True #ifall vi är på menyn

#states var keys vi bryr oss om
keys = {"space" : False, "R" : False, "ad" : False, "au" : False, "enter" : False, "escape" : False, "G" : False}

#saker för spelet----------------------------------------------------------------
gameOver = False #vi har förlorat och game over-skärmen ska visas

barHeight = 20 #height på barsen (uppe och nere)
score = 0 #nuvarande poäng
highscore = 0

pipes = [] #lista med alla pipes (uppe, nere, uppe, nere)
currentPipe = 0 #cp och cp+1 är de två pipesen som ska krocka i fågeln härnäst
pipeDist = 200 #avstånd i x-led, 200
pipeDiffi = 300 #max avstånd mellan pipes i höjdled typ (mittpunkt), 300
pipeGap = 150 #150 #hur stort gapet är av uppe och nere
pipeSpeed = 3 #deras hastighet
pipeWidth = 50 #50 #bredden
pipeLastGap = displayHeight/2 #vart den förra mittpunkten på en gap var
pipeLowerBound = displayHeight - barHeight - 20 - pipeGap/2 #det högsta värdet pipegap kan ha
pipeUpperBound = barHeight + 20 + pipeGap/2 #det lägsta värdet pipegap kan ha
pipeGravityIsFlipped = False #är gravitationen flippad? endast för färger
gravityDist = 0 #antal pipes tills en ny gravityPipe kommer
gravityDistMin = 5 #minsta antalet
gravityDistMax = 10 #max

bAnim = []
for i in range(1, 5):
    bAnim.append(pygame.image.load("flappy"+str(i)+".png").convert_alpha())
    
mainBird = Bird(200, displayHeight/2, bAnim, 20) #fågel man spelar som
bars = [Bar(0, displayWidth, barHeight, pipeSpeed), Bar(displayHeight-20, displayWidth, barHeight, pipeSpeed)] #init bars

background = Background(displayWidth, 1, pygame.image.load("background.jpg").convert_alpha()) #init background

bgMusic = pygame.mixer.Sound("angry_birds.ogg")
dieSound = pygame.mixer.Sound("crash.ogg")

godmode = False #undvika collision på pipes

#saker för menyn---------------------------------------------------------------------
curMenuItem = 0
menuItems = ["Start", "Quit"]

#<game>-------------------------------------------------------------------
def gameUpdate():
    global running, displayMenu, gameOver, keys, godmode
    
    if keys["space"] or keys["enter"]:
        mainBird.fly()
    elif keys["escape"]:
        setMenu()
    elif keys["G"]:
        godmode = not godmode;

    #uppdatera pipes
    updatePipes()

    #uppdatera bird
    mainBird.update()
    
    #uppdatera bars
    for bar in bars:
        bar.update()
    
    #uppdatera background
    background.update()
        
    #fågel krocka i bars
    if collideCircleRectangle(mainBird, bars[0]):
        setGameOver()
        mainBird.blockAtY(bars[0].y + bars[0].h, True, False)
    elif collideCircleRectangle(mainBird, bars[1]):
        setGameOver()
        mainBird.blockAtY(bars[1].y, False, False)
        
    #collide pipes
    if not godmode and (pipes[currentPipe].collideBird(mainBird, collideCircleRectangle) or pipes[currentPipe+1].collideBird(mainBird, collideCircleRectangle)): 
        setGameOver()

def updatePipes():
    global pipes, gameOver, currentPipe, score
    
    #ny pipe?
    lastPipe = pipes[-2]
    if lastPipe.x + lastPipe.w < displayWidth - pipeDist:
        if score == gravityDist:
            createGravityFlipPipe()
            newGravityDist()
        else:
            createNormalPipe()
    
    #ta bort första?
    lastPipe = pipes[0]
    if lastPipe.isOutside():
        pipes = pipes[2:]
        currentPipe -= 2
     
    #flytta dem
    for p in pipes:
        p.update()

    #fågel förbi currentPipe
    if pipes[currentPipe].isPast(mainBird.x-mainBird.radius):
        if pipes[currentPipe].flipsGravity:
            mainBird.flipGravity()
        else:
            score += 1
        currentPipe += 2
        
#skapa en vanlig pipe
def createNormalPipe():
    global pipeLastGap
    
    #kollar vart nya pipesens gap ska vara någonstans
    newY = random.randint(pipeUpperBound, pipeLowerBound)
    if newY - pipeLastGap > pipeDiffi:
        newY = pipeLastGap + pipeDiffi
    elif newY - pipeLastGap < -pipeDiffi:
        newY = pipeLastGap - pipeDiffi
    
    pipeLastGap = newY
    
    if pipeGravityIsFlipped:
        color = Pipe.flippedColor
    else:
        color = Pipe.normalColor

    appendPipes(pipeWidth, newY, color, pipeGap, displayWidth)
    
#hjälpfunktion för att skapa pipes. 
def appendPipes(w, y, color, pg, x):
    pipes.append(Pipe(x, barHeight, w, y-pg/2-barHeight, color, pipeSpeed))
    pipes.append(Pipe(x, y+pg/2, w, displayHeight-barHeight-pg/2-y, color, pipeSpeed))
    
#skapar säkra pipes som flippar gravitationen
def createGravityFlipPipe():
    global pipeLastGap, pipeGravityIsFlipped
    
    newY = displayHeight/2
    pipeLastGap = newY
    pipeGravityIsFlipped = not pipeGravityIsFlipped
    
    c = Pipe.gravityColor
    w = displayWidth/2
    pg = displayHeight/3
    
    appendPipes(w/2-mainBird.radius, newY, c, pg, displayWidth)
    pipes[-2].setAsGravityFlipper()
    appendPipes(w/2+mainBird.radius, newY, c, pg, displayWidth + w/2-mainBird.radius)
    
    for pi in pipes[-4:]:
        pi.setAsSafe()

#lista ut ett nytt random score som en gravity flip pipe ska skapas på
def newGravityDist():
    global gravityDist
    gravityDist = score + random.randint(gravityDistMin, gravityDistMax)
    
#ritar spelet, fågel pipes background etc.
def gameDraw():
    #fyller bakgrund (resnar gammalt)
    #gameDisplay.fill((255, 255, 255))
    background.draw(pygame, gameDisplay)
    
    mainBird.draw(pygame, gameDisplay)
    
    for bar in bars:
        bar.draw(pygame, gameDisplay)
        
    for p in pipes:
        p.draw(pygame, gameDisplay)
        
    message(str(score), 58, 60, 50)
    
#skriver ut lite text
def message(text, x, y, fontSize):
    largeText = pygame.font.Font('freesansbold.ttf',fontSize)
    textSurface = largeText.render(text, True, (255, 255, 255))
    textRect = textSurface.get_rect()
    textRect.center = (x, y)
    gameDisplay.blit(textSurface, textRect)
   
#game over----------------
def gameOverUpdate():
    global keys
    if keys["R"]:
        reset()
    elif keys["escape"]:
        setMenu()
                
def gameOverDraw():
    message("Game Over", displayWidth/2, displayHeight/2, 50)
    message("press 'R' to restart", displayWidth/2, displayHeight/2 + 50, 20)
    message("Highscore: "+str(highscore), displayWidth/2, displayHeight/2 + 70, 20)
        
def game():
    if not gameOver:
        gameUpdate()
    else:
        gameOverUpdate()
        
    gameDraw()
    
    if gameOver:
        gameOverDraw()

#</game>--------------------------------------------------------------------

#<collision>--------------------------------------------------------------------
def collideCircleRectangle(c, r):
    if c.y > r.y+r.h and c.x < r.x:
        return pointInCircle(c, (r.x, r.y+r.h))
    elif c.y < r.y and c.x < r.x:
        return pointInCircle(c, (r.x, r.y))
    elif c.y < r.y and c.x > r.x+r.w:
        return pointInCircle(c, (r.x+r.w, r.y))
    elif c.y > r.y+r.h and c.x > r.x+r.w:
        return pointInCircle(c, (r.x+r.w, r.y+r.h))
    else:
        return pointInRectangle((r.x-c.radius, r.y-c.radius, r.w+2*c.radius, r.h+2*c.radius), (c.x, c.y))

def pointInCircle(c, p):
    return math.sqrt((c.x - p[0])**2 + (c.y-p[1])**2) <= c.radius
    
def pointInRectangle(r, p):
    return p[0] >= r[0] and p[0] <= r[0]+r[2] and p[1] >= r[1] and p[1] <= r[1]+r[3]

#</collision>---------------------------------------------------------------------

#<transitions>--------------------------------------------
def setGameOver():
    global gameOver
    gameOver = True
    bgMusic.stop()
    dieSound.play()
    compareHighscore(score)
    
def setGame():
    global displayMenu
    reset()
    displayMenu = False
    
def setMenu():
    global displayMenu
    bgMusic.stop()
    dieSound.stop()
    displayMenu = True
#</transitions>----------------------------------------
    
#<menyer>-----------------------------------------------------------------
def menuUpdate():
    global curMenuItem, running, displayMenu
    
    if keys["au"]:
        curMenuItem = (curMenuItem-1) % len(menuItems)
    elif keys["ad"]:
        curMenuItem = (curMenuItem+1) % len(menuItems)
    elif keys["enter"]:
        if curMenuItem == 1:
            running = False
        elif curMenuItem == 0:
            setGame()
    
def menuDraw():
    #bakgrund
    gameDisplay.fill((0, 0, 0))
    
    #text
    message("Flaxande Fågel", displayWidth/2, displayHeight/2 - 80, 50)
    
    #menyn
    i = 0
    while i < len(menuItems):
        message((">> " if curMenuItem == i else "") + menuItems[i] + (" <<" if curMenuItem == i else ""), displayWidth/2, displayHeight/2 + i*25, 20)
        i += 1

def menu():
    menuUpdate()
    menuDraw()
#</menyer>------------------------------------------------------------------------

def reset():
    global gameOver, pipeLastGap, score, currentPipe, pipeGravityIsFlipped
    
    gameOver = False
    mainBird.reset()
    mainBird.fly()
    del pipes[:]
    pipeLastGap = displayHeight/2
    pipeGravityIsFlipped = False
    score = 0
    newGravityDist()
    currentPipe = 0
    createNormalPipe()
    
    bgMusic.play(-1)
    
    #reset bar och background
    
def loadHighscore():
    global highscore
    f = open("highscore.txt", "r")
    highscore = int(f.readline())
    
def compareHighscore(s):
    global highscore
    if s > highscore:
        highscore = s
        f = open("highscore.txt", "w")
        f.write(str(highscore))
    
def main():
    global keys, running
    loadHighscore()

    while running:
        #göra så att keys endast är True i ett tick
        for k in keys:
            if keys[k]:
                keys[k] = False
                
        #hantera events
        for event in pygame.event.get():
            if event.type == pygame.QUIT:
                running = False  
            elif event.type == pygame.KEYDOWN:
                if event.key == pygame.K_SPACE:
                    keys["space"] = True
                elif event.key == pygame.K_r:
                    keys["R"] = True
                elif event.key == pygame.K_UP:
                    keys["au"] = True
                elif event.key == pygame.K_DOWN:
                    keys["ad"] = True
                elif event.key == pygame.K_RETURN:
                    keys["enter"] = True
                elif event.key == pygame.K_ESCAPE:
                    keys["escape"] = True
                elif event.key == pygame.K_g:
                    keys["G"] = True
        
        if displayMenu:
            menu()
        else:
            game()
        
        pygame.display.update()    
        clock.tick(60)
        
    pygame.quit()
    
main()
