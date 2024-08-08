#include "OptionsWindow.h"

// Constructor

OptionsWindow::OptionsWindow(int height, int width, int start_y, int start_x, ColorManager& cm, int& sd)
    : Window(height, width, start_y, start_x),
    // initializes references to a ColorManager and an integer for sleep duration
    color_manager(cm), 
    sleep_duration(sd), 
    selected_option(0) {}

// get player name input from user
std::string OptionsWindow::get_player_name() {
    clear();

    // display prompt msg
    draw_text(1, 1, "Enter your name: ");

    // Enable echoing of characters
    echo(); 
    
    // Buffer to store input 
    char name[50];

    // Get the string input
    wgetnstr(win, name, 50); 
    
    // Disable echoing of characters
    noecho(); 
    return std::string(name);
}
