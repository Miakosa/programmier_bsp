#ifndef FRUIT_H
#define FRUIT_H

#include <curses.h>

class Fruit {
private:
    int fruit_x, fruit_y;
    const char* symbol;

public:
    Fruit(const char* symbol);

    void set_position(int max_x, int max_y);
    void draw(WINDOW* win) const;
    int get_x() const;
    int get_y() const ;
};

#endif // FRUIT_H
