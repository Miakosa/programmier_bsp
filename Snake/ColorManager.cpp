#include "ColorManager.h"

// Constructor
ColorManager::ColorManager() : inverted(false) {
}

// Initialize color pairs
void ColorManager::init_colors() {
    start_color();
    init_pair(1, COLOR_GREEN, COLOR_WHITE);
    init_pair(2, COLOR_WHITE, COLOR_GREEN);
    init_pair(3, COLOR_WHITE, COLOR_BLACK);
}

// Set the color of given window
void ColorManager::set_color(WINDOW* win, int color_pair) {
    wbkgd(win, COLOR_PAIR(color_pair));
}

// Invert the colors for all registered windows
void ColorManager::invert_colors() {
    if (inverted) {
        // Reset to original colors
        init_pair(1, COLOR_GREEN, COLOR_WHITE);
        init_pair(2, COLOR_WHITE, COLOR_GREEN);
        init_pair(3, COLOR_WHITE, COLOR_BLACK);
    }
    else {
        // Invert colors
        /* couldn't figure out how to make it work wioth A_REVERSE
        *  with A_REVERSE would the text color change and each text/symbol got an background. 
        *  The Windows themselfse did not change color. I guess that is just usable with letters? :(
        * 
        * inverted = !inverted;
        *   for (auto win : windows) {
        *       wattron(win, A_REVERSE);
        *       wrefresh(win);
        *   }
        */
        init_pair(1, COLOR_MAGENTA, COLOR_BLACK);
        init_pair(2, COLOR_BLACK, COLOR_MAGENTA);
        init_pair(3, COLOR_BLACK, COLOR_WHITE);
    }
    inverted = !inverted;
    refresh_windows();
}

// register a window for color management
void ColorManager::add_window(WINDOW* win) {
    windows.push_back(win);
}

// refresh all registered windows
void ColorManager::refresh_windows() {
    for (WINDOW* win : windows) {
        wrefresh(win);
    }
}
