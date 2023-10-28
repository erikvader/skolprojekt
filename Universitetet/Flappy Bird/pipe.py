class Pipe:

    gravityColor = (255,255,255)
    normalColor = (0, 255, 0)
    flippedColor = (255, 0, 0)

    def __init__(self, x, y, w, h, color, speed):
        self.x = x
        self.y = y
        self.w = w
        self.h = h
        self.color = color
        self.speed = speed
        self.flipsGravity = False
        self.isSafe = False
        
    def update(self):
        self.x -= self.speed
        
    #pipen har åkt utanför skärmen
    def isOutside(self):
        return self.x + self.w < 0
        
    #fågel har helt och hållet passerat denna pipe. x är fågelns vänsterkant
    def isPast(self, x):
        return self.x + self.w < x
        
    #denna pipe flippar gravitationen
    def setAsGravityFlipper(self):
        self.flipsGravity = True
        
    #man dör inte ifall man krockar ovanifrån eller underifrån
    def setAsSafe(self):
        self.isSafe = True
        
    #sätter fågelns x och y mot rätt vägg samt avgör ifall man ska förlora eller ej
    def collideBird(self,  bird, intersectsFunc):
        if not intersectsFunc(bird, self):
            return False

        if bird.x+bird.collideX >= self.x:
            if bird.y < self.y:
                bird.blockAtY(self.y, False, self.isSafe)
                return not self.isSafe
            else:
                bird.blockAtY(self.y+self.h, True, self.isSafe)
                return not self.isSafe
        else:
            bird.blockAtX(self.x, False)
            return True
        
        return False
    
    def draw(self, pygame, gameDisplay):
        pygame.draw.rect(gameDisplay, self.color, (int(self.x), int(self.y), int(self.w), int(self.h)))

        
        
        
        
        
