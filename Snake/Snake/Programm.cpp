
#include <curses.h>
#include <string.h>
#include <windows.h>
#include "Snake.h"
#include "Fruit.h"

int main()
{
    initscr();                      // initialise Bib
   

    start_color();                  // activate Colorfunction
    init_pair(1, COLOR_GREEN, COLOR_WHITE);  // define a Color - Pair 1
    init_pair(2, COLOR_WHITE, COLOR_GREEN);

    refresh();  // refresh main Window
    
    WINDOW* window = newwin(0, 0, 0, 0); // create Window 
    wbkgd(window, COLOR_PAIR(1));   // Color window set to Pair 1

    bool running = false;
    bool exit_programm = false;
    
    // get sice of terminal
    int max_x, max_y;
    getmaxyx(window, max_y, max_x);

    const char* text = "START";
    int text_length = strlen(text);    // get length of text
    int text_x = (max_x - text_length) / 2; 
    int text_y = (max_y - 1)/ 2; // -1 for the height of the text

    // create Snake Object
    Snake snake("*");

    Fruit fruit("x");

    // sice of game_window
    int g_win_height = max_y / 1.2;
    int g_win_width = max_x / 1.2;
    int g_win_y = (max_y - g_win_height) / 2;
    int g_win_x = (max_x - g_win_width) / 2;

    

    // display Start in center of Window
    mvwprintw(window, text_y, text_x, text);
    wrefresh(window); // window refresh
    
    

    // something happens when Space is clicked -> something should later be a started game 
    int ch; // key input
    int color_pair = 1;
    WINDOW* game_window = nullptr; // game window pointer

    nodelay(stdscr, TRUE);          // getch() is not blocking the loop stdscr: Main window
    keypad(stdscr, TRUE);           // enable keypad input for arrow keys

    // main loop 
    while (!exit_programm)
    {
        if ((ch = getch()) != ERR) // check for user input
        {

            switch (ch) {
            case 27:
                exit_programm = true;
                break;// quit loop

            case ' ':
                color_pair = (color_pair == 1) ? 2 : 1; // toggle between init_pairs
                wbkgd(window, COLOR_PAIR(color_pair));
                werase(window); // delete everything from the window
            
                if (color_pair == 1) {
                    mvwprintw(window, text_y, text_x, text); // display "START" only if color pair 1
                // delete game_window when in "menu"
                    if (game_window != nullptr) {
                        delwin(game_window);
                        game_window = nullptr;
                    }
                }
                else if (color_pair == 2) {
                    running = true;

                    
                    game_window = newwin(g_win_height, g_win_width, g_win_y, g_win_x);
                    wbkgd(game_window, COLOR_PAIR(1));

                    // set position symbol to center game_window
                    snake.set_position(g_win_width / 2, g_win_height / 2);
                    snake.set_limit(g_win_width,g_win_height);

                    
                    fruit.set_position(g_win_width, g_win_height);
                    wrefresh(window);
                    wrefresh(game_window);
                }

                 wrefresh(window);
                 break;

            
            }
        }
     
        // if game_window is active move the symbol

        if (running) {
           
            switch (ch) {
                case KEY_UP:
                    snake.change_direction(0, -1);
                    break;

                case KEY_DOWN:
                    snake.change_direction(0, 1);
                    break;

                case KEY_LEFT:
                    snake.change_direction(-1, 0);
                    break;

                case KEY_RIGHT:
                    snake.change_direction(1, 0);
                    break;
            }

            werase(game_window); // clear game_window
            snake.move();

            // Check if snake has collided with the fruit
            if (snake.get_x() == fruit.get_x() && snake.get_y() == fruit.get_y()) {
                // Set new random position for the fruit
                fruit.set_position(g_win_width, g_win_height);
            }


            if (snake.has_collided()) {
                running = false;
                color_pair = 1;
                wbkgd(window, COLOR_PAIR(color_pair));
                werase(window);
                mvwprintw(window, text_y, text_x, text); // display "START" only if color pair 1
                // delete game_window when in "menu"
                if (game_window != nullptr) {
                    delwin(game_window);
                    game_window = nullptr;
                }
                wrefresh(window);
            }
            else {
                snake.draw(game_window); // draw snake onto game window
                fruit.draw(game_window);
                wrefresh(game_window);
            }
            wrefresh(window);
            Sleep(100);

        }

    }
   
   
   // if the game-window still exists delete it
    if (game_window != nullptr) {
        delwin(game_window);
   }

   // getch(); // wait for user input


    delwin(window); // delete window
    endwin();  // end 

    return 0;
}