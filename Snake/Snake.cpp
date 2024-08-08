#include "Snake.h"
#include <chrono>
#include <thread>

// Constructor
Snake::Snake(const char* symbol) 
    // Initializes the base class 
    : Object(symbol) {
    // Initial variables
    dir_x = 0;
    dir_y = 0; 
    limit_x = 0;
    limit_y = 0;
}
// Moves the sbake in given direction
void Snake::move() {

    if (!body.empty()) {
        //move each segment to the position of the previous segment
        for (size_t i = body.size() - 1; i > 0; --i) {
            body[i] = body[i - 1];
        }

        // update the first segment to the current head position
        body[0] = { x, y }; 
    }

    // Update head position
    x += dir_x;
    y += dir_y;

    // Add a delay when moving in the x-direction to compensate for rectangular cells
    if (dir_y != 0) {
        std::this_thread::sleep_for(std::chrono::milliseconds(50));
    }
}

// Draws the snake on the given window
void Snake::draw(WINDOW* win) const {
    // Draw head
    mvwprintw(win, y, x, symbol);

    // Draw body
    for (const auto& segment : body) {
        mvwprintw(win, segment.second, segment.first, symbol);
    }
}

// Grow the snake by adding a new segment at the end
void Snake::grow() {

    if (body.empty()) {
        // add a segment behind the head
        body.push_back({ x - dir_x, y - dir_y });
    }
    else {
        // add a segment at the position of the last segment
        body.push_back(body.back());
    }
}

// Check if the snake has collided with the wall or itself
bool Snake::has_collided() const {

    // Check for collision with walls
    if (x <= 0 || x >= limit_x - 1 || y <= 0 || y >= limit_y - 1) {
        return true;
    }

    // Check for collision with itself
    for (const auto& segment : body) {
        if (x == segment.first && y == segment.second) {
            return true;
        }
    }

    return false;
}

// Resets the snake to its initial state
void Snake::reset() {
    // clear the body segments
    body.clear(); 

    // Reset directions
    dir_x = 0;
    dir_y = 0;
}

// Change the direction of the snake
void Snake::change_direction(int new_dir_x, int new_dir_y) {
    dir_x = new_dir_x;
    dir_y = new_dir_y;
}

// set the starting position of the snake
void Snake::set_position(int start_x, int start_y) {
    x = start_x;
    y = start_y;
}

// set the limits of the game_window to detect collisions
void Snake::set_limit(int limit_x, int limit_y) {
    this->limit_x = limit_x;
    this->limit_y = limit_y;
}

