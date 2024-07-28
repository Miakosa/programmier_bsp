#ifndef SNAKE_H
#define SNAKE_H

#include <curses.h>

class Snake {
private:
    int snake_x, snake_y;
    int limit_x, limit_y;
    const char* symbol;
    int dir_x, dir_y;

public:
    Snake(const char* symbol);

    void set_position(int start_x, int start_y);
    void set_limit(int limit_x, int limit_y);
    void change_direction(int new_dir_x, int new_dir_y);
    void move();
    void draw(WINDOW* win) const;
    bool has_collided() const;
    int get_x() const;
    int get_y() const;
  
};

#endif // SNAKE_H
