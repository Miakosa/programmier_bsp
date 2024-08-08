#ifndef COLORMANAGER_H
#define COLORMANAGER_H

#include <vector>
#include <curses.h>

class ColorManager {
private:
    std::vector<WINDOW*> windows;
    bool inverted;

public:
    ColorManager();

    void init_colors();
    void set_color(WINDOW* win, int color_pair);
    void invert_colors();
    void add_window(WINDOW* win);
    void refresh_windows();
};

#endif // COLORMANAGER_H
