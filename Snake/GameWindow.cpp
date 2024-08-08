#include "GameWindow.h"

// Constructor
GameWindow::GameWindow(int height, int width, int start_y, int start_x)
    : Window(height, width, start_y, start_x) {}

// Updates the game_window with the current state of the snake and fruit
void GameWindow::update(Snake& snake, Fruit& fruit) {
    clear();
    snake.move();
    snake.draw(win);
    fruit.draw(win);
    refresh();
}
