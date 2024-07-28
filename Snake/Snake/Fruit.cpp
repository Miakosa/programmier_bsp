#include <curses.h>
#include <random>
#include "Fruit.h"

Fruit::Fruit(const char* symbol) : symbol(symbol) {
    // Konstruktor
}

void Fruit::set_position(int max_x, int max_y) {
    std::random_device rd; // obtain a random number from hardware
    std::mt19937 gen(rd()); // seed the generator
    std::uniform_int_distribution<> distr_x(1, max_x - 1); // define the range for x
    std::uniform_int_distribution<> distr_y(1, max_y - 1); // define the range for y

    fruit_x = distr_x(gen);
    fruit_y = distr_y(gen);
}

void Fruit::draw(WINDOW* win) const {
    mvwprintw(win, fruit_y, fruit_x, symbol);
}

int Fruit::get_x() const {
    return fruit_x;
}

int Fruit::get_y() const {
    return fruit_y;
}