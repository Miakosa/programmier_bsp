#include <curses.h>
#include "Fruit.cpp"

class Snake {
private:
	int snake_x, snake_y;
	int limit_x, limit_y;
	const char* symbol;
	int dir_x, dir_y;

public:
	// set the used Symbol for the Snake
	Snake(const char* symbol) {
		this->symbol = symbol;
		
		
	}
	
	void set_position(int start_x, int start_y) {
		snake_x = start_x;
		snake_y = start_y;
		
	}
	void set_limit(int limit_x, int limit_y) {
		this->limit_x = limit_x;
		this->limit_y = limit_y;
	}
	void change_direction(int new_dir_x, int new_dir_y) {
		dir_x = new_dir_x;
		dir_y = new_dir_y;
	}

	void move() {
		snake_x += dir_x;
		snake_y += dir_y;
		
	}
	
	void draw(WINDOW* win) {
		mvwprintw(win, snake_y, snake_x, symbol);
	}
	// snake hit any wall of game_window
	bool has_collided() {
		return (snake_x <= 0 || snake_x >= limit_x - 1 || snake_y <= 0 || snake_y >= limit_y - 1);
	}

	
	int get_x()  {
		return snake_x;
	}
	int get_y() {
		return snake_y;
	}
};