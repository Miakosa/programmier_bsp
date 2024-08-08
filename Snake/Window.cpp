#include "Window.h"
#include <string>

/* Consturctor
* Initializes a new window with specified dimensions and position
*/
Window::Window(int height, int width, int start_y, int start_x) 
    : selected_option(0)
{
    win = newwin(height, width, start_y, start_x);
}

/* Deconstructor
*  Deletes the window to free up memory
*/
Window::~Window() {
    delwin(win);
}

// Draws text at a specific position in the window
void Window::draw_text(int y, int x, const char* text) const {
    mvwprintw(win, y, x, text);
}

// Draws an integer at a specific position in the window
void Window::draw_int(int y, int x, int number) const
{
    std::string num_2_str = std::to_string(number);
    mvwprintw(win, y, x, num_2_str.c_str());

}

// Clear the content of the window
void Window::clear() {
    werase(win);
}

// refreshes the window to make changes visible
void Window::refresh() const {
    wrefresh(win);
}

// get the window object
WINDOW* Window::get_window() const {
    return win;
}

/* display a list of options in the window
*  also highlight the selected option
*/
void Window::show_options(const std::vector<std::string>& options) {
    // store options in the object
    this->options = options;
    clear();

    // get window dimensions
    int max_x, max_y;
    getmaxyx(win, max_y, max_x);

    // Loop through each option to display it
    for (size_t i = 0; i < options.size(); ++i) {
        int text_length = options[i].length();

        // center each text horizontally
        int text_x = (max_x - text_length) / 2;

        // Distribute text vertically
        int text_y = (max_y - options.size()) / 2 + i;

        if (i == selected_option) {
            // Highlight the selected option
            wattron(win, A_BOLD); 
            draw_text(text_y, text_x, options[i].c_str());
            wattroff(win, A_BOLD);
        }
        else {
            draw_text(text_y, text_x, options[i].c_str());
        }
    }
    // display changes
    refresh();
}

/* naviagate through the list of options
*  return the selected option index
*  or special values for ESC and SPACE 
*/
int Window::navigate_options() {
    int ch;
    // display initial options
    show_options(options); 
    /* Loop to handle user input for navigation
    *  '\n' is the ENTER key
    *  27 is the ESC key
    *  ' ' is the SPACE key
    */
    while ((ch = getch()) != '\n' && ch != 27 && ch != ' ') { 
        bool need_update = false;
        switch (ch) {
        case KEY_UP:
            /* + option.size() to make sure no negative value occures
            *  % option.sie() so you can cycle through the options          
            */
            selected_option = (selected_option - 1 + options.size()) % options.size();
            need_update = true;
            break;
        case KEY_DOWN:
            selected_option = (selected_option + 1) % options.size();
            need_update = true;
            break;
        }

        // Update displayed options if key up/down was pressed
        if (need_update) {
            show_options(options);
        }
    }
    if (ch == 27) {
        return -1; // Special value to indicate ESC was pressed
    }
    if (ch == ' ')
    {
        return -2; // Special value to indicate SPACE was pressed
    }
    return selected_option;
}