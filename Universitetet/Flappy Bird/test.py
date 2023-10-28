import pygame
import random

pygame.init()

displayWidth = 800
displayHeight = 600

gameDisplay = pygame.display.set_mode((displayWidth, displayHeight))
pygame.display.set_caption("testing typ")
clock = pygame.time.Clock()

#stuffs
running = True
gameOver = False
score = 0

#bil
carImg = pygame.image.load("racecar.png")
carWidth = 73
carHeight = 82
carPos = [displayWidth*0.5-carWidth/2, displayHeight*0.8]
cardx = 0
carSpeed = 8

#thing
thing_startSpeed = 7
thing_startWidth = 100
thing = [0, 0, thing_startSpeed, thing_startWidth, 100] #x, y, speed, w, h

#controls
rightKey = False
leftKey = False

#funktioner
def update():
    global running, cardx, leftKey, rightKey, carWidth, gameOver
    
    #hantera events
    for event in pygame.event.get():
        if event.type == pygame.QUIT:
            running = False  
        elif event.type == pygame.KEYDOWN:
            if event.key == pygame.K_LEFT:
                leftKey = True
            elif event.key == pygame.K_RIGHT:
                rightKey = True
            elif event.key == pygame.K_r:
                gameOver = False
                reset()
            elif event.key == pygame.K_ESCAPE:
                running = False
        elif event.type == pygame.KEYUP:        
            if event.key == pygame.K_RIGHT:
                rightKey = False
            elif event.key == pygame.K_LEFT:
                leftKey = False
        
        print(event)
        
    #logic
    if not gameOver:
        updateCar()
        updateThing()
        theyCrash()
        
#<logics>
def updateCar():
    global carPos, cardx
    #move 
    if leftKey:
        cardx = -carSpeed
    elif rightKey:
        cardx = carSpeed
    else:
        cardx = 0
    carPos[0] += cardx
    
    #edges
    if carPos[0] < 0:
        carPos[0] = 0
    elif carPos[0] + carWidth > displayWidth:
        carPos[0] = displayWidth - carWidth
        
def updateThing():
    global score
    thing[1] += thing[2]
    
    if thing[1] > displayHeight:
        score += 1
        thing[2] += 0.2
        thing[3] = thing_startWidth + (score*5)
        spawnThing()
    
def theyCrash(): #collide two rectangles
    global gameOver
    if carPos[0] < thing[0]+thing[3] and carPos[0]+carWidth > thing[0] and carPos[1] < thing[1]+thing[4] and carPos[1]+carHeight > thing[1]:
        gameOver = True
    
#</logics>
        
#<draws>
def draw():
    gameDisplay.fill((255, 255, 255))
    drawCar()
    drawThing()
    things_dodged(score)
    if gameOver:
        messageCentered("Du dog typ", 200, 115)
        messageCentered("R to starta om", 300, 80)
    
def drawCar():
    gameDisplay.blit(carImg, carPos)
    
def drawThing():
    drawRect(thing[0], thing[1], thing[3], thing[4], (255, 0, 0))

def drawRect(x, y, w, h, color):
    pygame.draw.rect(gameDisplay, color, (x, y, w, h))
    
def messageCentered(text, y, fontSize):
    largeText = pygame.font.Font('freesansbold.ttf',fontSize)
    textSurface = largeText.render(text, True, (0, 0, 0))
    textRect = textSurface.get_rect()
    textRect.center = ((displayWidth/2), y)
    gameDisplay.blit(textSurface, textRect)
    
def things_dodged(count):
    font = pygame.font.SysFont(None, 25)
    text = font.render("Dodged: "+str(count), True, (0, 0, 0))
    gameDisplay.blit(text,(0,0))
#</draws>

def spawnThing():
    thing[0] = random.randint(0, displayWidth-thing[3])
    thing[1] = -thing[4]

def reset():
    global score, carPos
    score = 0
    carPos[0] = displayWidth*0.5-carWidth/2
    carPos[1] = displayHeight*0.8
    thing[3] = thing_startWidth
    thing[2] = thing_startSpeed
    spawnThing()
    
def main():
    reset()
    #main loop
    while running:
        update()
        draw()
        pygame.display.update()    
        clock.tick(60)
        
      
        
main()
pygame.quit()  