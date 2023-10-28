class Bar:
    
    def __init__(self, y, w, h, s):
        self.y = y
        self.x = 0 #ska alltid vara 0
        self.w = w
        self.h = h
        self.w2 = h*1.6
        self.offset = 0
        self.offsetSpeed = s
        
    def update(self):
        #flytta sig
        self.offset -= self.offsetSpeed
        if self.offset <= -self.w2*2:
            self.offset += self.w2*2
        
    def draw(self, pygame, gameDisplay):
        #ljusröna grejen
        pygame.draw.rect(gameDisplay, (255, 0, 8), (int(self.x), int(self.y), int(self.w), int(self.h)))
        
        #mörkare grejer
        i = self.offset
        while i < self.w:
            pygame.draw.rect(gameDisplay, (150, 0, 5), (int(i), int(self.y), int(self.w2), int(self.h)))
            i += self.w2*2