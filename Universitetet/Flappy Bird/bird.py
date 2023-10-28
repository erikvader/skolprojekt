import math

class Bird:
    #ska egentligen ha sets o gets
    
    def __init__(self, x, y, img, radius):
        self.x = x
        self.startX = x
        self.y = y
        self.startY = y
        self.flySpeed = -7
        self.dy = 0
        self.gravity = 0.3
        self.maxdy = 10
        self.radius = radius
        self.img = img
        self.currentImg = 0
        self.animationSpeed = 3
        self.animationCounter = 0
        self.g = 1  #either 1 or -1
        self.collideX = math.cos(math.pi/4) * self.radius
        self.collideY = -math.sin(math.pi/4) * self.radius
        
    def reset(self):
        self.dy = 0
        self.x = self.startX
        self.y = self.startY
        self.g = 1
        
        #reset animationer
        
    def update(self):
        #speeds
        self.dy += self.gravity*self.g
        if self.dy > self.maxdy and self.g == 1 or self.dy < -self.maxdy and self.g == -1:
            self.dy = self.maxdy*self.g
        
        self.y += self.dy
        
        #update animation
        self.animationCounter += 1
        if self.animationCounter >= self.animationSpeed:
            self.animationCounter = 0
            self.currentImg += 1 - (self.currentImg == len(self.img)-1)*len(self.img)
            
        #debbugging
        #if self.y > 459:
        #    self.y = 459
        
    def fly(self):
        #if self.y > 0:
        self.dy = self.flySpeed*self.g
        
    def flipGravity(self):
        self.g *= -1
        
    #tar en y-kordinat som fågel inte kan åka genom
    #isUp ifall man inte kan åka upp eller ner från denna kordinat
    #sätter fågelns y-position till rätt
    def blockAtY(self, y, isUp, alive):
        self.y = self.radius * (1 if isUp else -1) + y
        if alive:
            self.dy = 0
        
    def blockAtX(self, x, isLeft):
        self.x = self.radius * (1 if isLeft else -1) + x
        
    def draw(self, pygame, gameDisplay):
        #pygame.draw.circle(gameDisplay, (255, 0, 0), (int(self.x), int(self.y)), int(self.radius))
        image = self.img[self.currentImg]
        if self.g == -1:
            image = pygame.transform.flip(image, False, True)
        angle = -5*self.dy + 10*self.g
        image = pygame.transform.rotate(image, angle)
        gameDisplay.blit(image, (int(self.x-image.get_rect().width/2), int(self.y-image.get_rect().height/2)))

        
