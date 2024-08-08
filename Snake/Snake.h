#ifndef SNAKE_H
#define SNAKE_H

#include <vector>
#include "Object.h"

class Snake : public Object {
private:
    int dir_x, dir_y;
    int limit_x, limit_y;
    std::vector<std::pair<int, int>> body;

public:
    Snake(const char* symbol);

    void move() ;
    void draw(WINDOW* win) const override;
    bool has_collided() const ;
    void reset() ;
    void grow();
    void change_direction(int new_dir_x, int new_dir_y);
    void set_position(int start_x, int start_y) override;
    void set_limit(int limit_x, int limit_y);
};

#endif // SNAKE_H
