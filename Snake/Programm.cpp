#include <curses.h>
#include <string.h>
#include <windows.h>
#include "MenuWindow.h"
#include "GameWindow.h"
#include "ScoreWindow.h"
#include "Snake.h"
#include "Fruit.h"
#include "Points.h"
#include "ColorManager.h"
#include "OptionsWindow.h"

int main() {

    initscr(); // initialise Bib

    ColorManager color_manager;
    color_manager.init_colors(); // activate Colorfunction

    refresh(); // refresh terminal

    bool running = false;
    bool exit_programm = false;
    bool in_menu = true;

    int sleep_duration = 100;
    int score = 0;

    int ch; // key input
    std::string player_name = "Unknown";

    // get size of terminal
    int max_x, max_y;
    getmaxyx(stdscr, max_y, max_x);

    // create Snake Object
    Snake snake("S");
    // create Fruit Object
    Fruit fruit("F");

    // size of game_window
    int g_win_height = static_cast<int>(max_y / 1.2);
    int g_win_width = static_cast<int>(max_x / 1.2);
    int g_win_y = (max_y - g_win_height) / 2;
    int g_win_x = (max_x - g_win_width) / 2;

    // create MenuWindow object
    MenuWindow menu_window(0, 0, 0, 0);
    // options in Menu
    std::vector<std::string> menu_options = { "Start", "Options", "Points" };

    // create GameWindow object
    GameWindow game_window(g_win_height, g_win_width, g_win_y, g_win_x);

    // create ScoreWindow object
    ScoreWindow score_window(1, g_win_width, g_win_y - 1, g_win_x);
  
    // create Points Window object
    Points points(g_win_height, g_win_width - 30, g_win_y, g_win_x + 15);

    // Create OptionsWindow object
   OptionsWindow options_window(g_win_height, g_win_width, g_win_y, g_win_x, color_manager, sleep_duration);
   // options in option
   std::vector<std::string> options_options = { "Invert Colors", "Toggle Speed", "Set Playername"};

    // Register windows with the color manager
    color_manager.add_window(menu_window.get_window());
    color_manager.add_window(game_window.get_window());
    color_manager.add_window(score_window.get_window());
    color_manager.add_window(points.get_window());
    color_manager.add_window(options_window.get_window());


    nodelay(stdscr, TRUE); // getch() is not blocking the loop 
    keypad(stdscr, TRUE); // enable keypad input for arrow keys

    // main loop
    while (!exit_programm) {
        // if Menu_window is open
        if (in_menu) {
            color_manager.set_color(menu_window.get_window(), 1);
            menu_window.show_options(menu_options);
            int selected = menu_window.navigate_options();

            if (selected == -1) { // ESC was pressed in menu
                exit_programm = true;
                continue;
            }

            switch (selected) {
            case 0: // Start
                color_manager.set_color(menu_window.get_window(), 2);
                color_manager.set_color(game_window.get_window(), 1);
                color_manager.set_color(score_window.get_window(), 3);
                menu_window.refresh();
                in_menu = false;
                running = true;

                
                // set position symbol to center game_window
                snake.set_position(g_win_width / 2, g_win_height / 2);
                snake.set_limit(g_win_width, g_win_height);
                fruit.set_random_position(g_win_width, g_win_height);

                score = 0; // Reset score
                score_window.show_score(score);
                break;

            case 1: // Options
                color_manager.set_color(menu_window.get_window(), 2);
                color_manager.set_color(options_window.get_window(), 1);
                menu_window.refresh();
                in_menu = false;
                options_window.show_options(options_options);
                {
                    int option_selected = options_window.navigate_options();
                    switch (option_selected) {
                    case -2:
                        in_menu = !in_menu;
                        color_manager.set_color(menu_window.get_window(), 1);
                        break;
                    case -1: // ESC pressed
                        exit_programm = true;
                        break;
                    case 0:
                        color_manager.invert_colors();
                        break;
                    case 1:
                        sleep_duration = (sleep_duration == 100) ? 150 : 100;
                        break;
                    case 2:
                        player_name = options_window.get_player_name();
                        break;
                    }
                }
                in_menu = true;
                break;

            case 2: // Points
                color_manager.set_color(menu_window.get_window(), 2);
                color_manager.set_color(points.get_window(), 1);
                menu_window.refresh();
                in_menu = false;
                points.show_high_scores();
                break;
            }
        }

        // check for valid user input
        if ((ch = getch()) != ERR) { 
            switch (ch) {

            // quit main loop
            case 27:
                exit_programm = true;
                break;

            // back to menu_window
            case ' ': 

                // set correct color for the window
                color_manager.set_color(menu_window.get_window(), 1);

                // add the name and score to the Points Window if it is in the top 3 
                points.add_score(player_name, score);

                in_menu = !in_menu;
                running = !in_menu;
                
                break;

            case KEY_UP:
                if (running) snake.change_direction(0, -1);
                break;

            case KEY_DOWN:
                if (running) snake.change_direction(0, 1);
                break;

            case KEY_LEFT:
                if (running) snake.change_direction(-1, 0);
                break;

            case KEY_RIGHT:
                if (running) snake.change_direction(1, 0);
                break;
            }
        }

        if (running) {
           
            /* i couldn't figure out how to use "need_update" like in Window::navigate_options().
             * snake would only move when a key was clicked and the Fruit still fickered :(  
             */
             // while the game_window is visible update it each sleep_duration
            game_window.update(snake, fruit);

            // Check if snake has collided with the fruit
            if (snake.get_x() == fruit.get_x() && snake.get_y() == fruit.get_y()) {

                // Set new random position for the fruit
                fruit.set_random_position(g_win_width, g_win_height);

                // add a Point to the Score
                score++;
                score_window.show_score(score);

                // Extend the snake
                snake.grow(); 
            }

            // check if the snake has hit itself or the game_window walls 
            if (snake.has_collided()) {
                // set correct color for the window
                color_manager.set_color(menu_window.get_window(), 1);
                running = false;
                in_menu = true;

                // add the name and score to the Points Window if it is in the top 3 
                points.add_score(player_name, score);

                // reset the score
                score = 0;
                
                // reset the snake size
                snake.reset();
            }
            
            Sleep(sleep_duration);
        }
    }
    endwin(); // end 
    return 0;
}
