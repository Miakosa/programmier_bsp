#include "Object.h"
#include <random>

// Constructor
Object::Object(const char* symbol)
	// Initializes the object with a given symbol to represent it
	: symbol(symbol)
{
	x = 0;
	y = 0;
}

// Sets the position of the object to specified coordinates (x, y)
void Object::set_position(int x, int y)
{
	this->x = x;
	this->y = y;
}

// Sets a random position for the object within given maximum bounds (max_x, max_y)
void Object::set_random_position(int max_x, int max_y) {
	
	// obtain a random number from hardware
	std::random_device rd; 

	// seed the generator
	std::mt19937 gen(rd());

	// define the range for x & y
	std::uniform_int_distribution<> distr_x(1, max_x - 1); 
	std::uniform_int_distribution<> distr_y(1, max_y - 1);

	// Generate a random x and y position within the range
	x = distr_x(gen);
	y = distr_y(gen);
}

// Draws the object on a given window at its current position (x, y)
void Object::draw(WINDOW* win) const
{
	mvwprintw(win, y, x, symbol);
}

// Returns the current x-coordinate of the object
int Object::get_x() const
{
	return x;
}

// Returns the current y-coordinate of the object
int Object::get_y() const
{
	return y;
}
