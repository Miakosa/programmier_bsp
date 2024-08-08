#ifndef GAMEWINDOW
#define GAMEWINDOW

#include "Window.h"
#include "Snake.h"
#include "Fruit.h"

class GameWindow : public Window {
public:
    GameWindow(int height, int width, int start_y, int start_x);
    void update(Snake& snake, Fruit& fruit);
};

#endif // GAMEWINDOW
