#include "Snake.h"

	// set the used Symbol for the Snake
	Snake::Snake(const char* symbol) {
		this->symbol = symbol;
		
		
	}
    void Snake::set_position(int start_x, int start_y) {
        snake_x = start_x;
        snake_y = start_y;
    }

    void Snake::set_limit(int limit_x, int limit_y) {
        this->limit_x = limit_x;
        this->limit_y = limit_y;
    }

    void Snake::change_direction(int new_dir_x, int new_dir_y) {
        dir_x = new_dir_x;
        dir_y = new_dir_y;
    }

    void Snake::move() {
        snake_x += dir_x;
        snake_y += dir_y;
    }

    void Snake::draw(WINDOW* win) const {
        mvwprintw(win, snake_y, snake_x, symbol);
    }

    bool Snake::has_collided() const {
        return (snake_x <= 0 || snake_x >= limit_x  || snake_y <= 0 || snake_y >= limit_y );
    }

    int Snake::get_x() const {
        return snake_x;
    }

    int Snake::get_y() const {
        return snake_y;
    }
