#ifndef WINDOW_H
#define WINDOW_H

#include <curses.h>
#include <string>
#include <vector>

class Window {
protected:
    WINDOW* win;
    int selected_option;
    std::vector<std::string> options;

public:
    Window(int height, int width, int start_y, int start_x);
    virtual ~Window();

    void draw_text(int y, int x, const char* text) const;
    void draw_int(int y, int x, int number) const;
    void clear();
    void refresh() const;
    WINDOW* get_window() const;

    void show_options(const std::vector<std::string>& options);
    int navigate_options();

    
};

#endif // WINDOW_H
