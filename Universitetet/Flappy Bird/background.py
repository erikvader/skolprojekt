class Background:
    def __init__(self, w, xSpeed, img): #w = skärmens bredd
        self.y = 0
        self.x = 0 #ska alltid vara 0
        self.w = w
        self.bw = img.get_rect().width #bildens bredd
        self.img = img
        self.xSpeed = xSpeed
        
    def update(self):
        self.x -= self.xSpeed
        if self.x <= -self.bw:
            self.x += self.bw
        
    def draw(self, pygame, gameDisplay):
        i = self.x
        while i < self.w:
            gameDisplay.blit(self.img, (int(i), 0))
            i += self.bw