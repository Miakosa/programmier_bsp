

#include <curses.h>
#include <string.h>
#include <windows.h>

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

    // a moving symbol named snake
    int snake_y = 0;
    int snake_x = 0;
    const char* snake = "*";
    int limit_x = max_x /1.2;
    int limit_y = max_y / 1.2;

    // initial direction of the symbol
    int dir_x = 1;
    int dir_y = 0;
    // display Start in center of Window
    mvwprintw(window, text_y, text_x, text);
    wrefresh(window); // window refresh
    
    

    // something happens when Space is clicked -> something should later be a started game 
    int ch; // key input
    int color_pair = 1;
    WINDOW* game_window = nullptr; // game window pointer

    nodelay(stdscr, TRUE);          // getch() is not blocking the loop
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

                    int g_win_height = max_y / 1.2;
                    int g_win_width = max_x / 1.2;
                    int g_win_y = (max_y - g_win_height) / 2;
                    int g_win_x = (max_x - g_win_width) / 2;

                    game_window = newwin(g_win_height, g_win_width, g_win_y, g_win_x);
                    wbkgd(game_window, COLOR_PAIR(1));

                    // set position symbol to center game_window
                    snake_y = g_win_height / 2;
                    snake_x = g_win_width / 2;
                    wrefresh(window);
                    wrefresh(game_window);
                }

                 wrefresh(window);
                 break;

            case KEY_UP:
                dir_x = 0;
                dir_y = -1;
                break;

            case KEY_DOWN:
                dir_x = 0;
                dir_y = 1;
                break;

            case KEY_LEFT:
                dir_x = -1;
                dir_y = 0;
                break;

            case KEY_RIGHT:
                dir_x = 1;
                dir_y = 0;
                break;
            }
        }
     

        // if game_window is active move the symbol

        if (running) {
            werase(game_window); // clear game_window
            mvwprintw(game_window, snake_y, snake_x, snake); // print the symbol onto game window
            wrefresh(game_window);
         
            snake_x += dir_x;
            snake_y += dir_y;

            // move the symbol  and stay inside the game window

            if (snake_x < 1) {
                snake_x = 1;
            }
            else if (snake_x >= limit_x - 1) {
                snake_x = limit_x - 2;
            }
            else if (snake_y < 1) {
                snake_y = 1;
            }
            else if (snake_y >= limit_y - 1) {
                snake_y = limit_y - 2;
            }


            Sleep(1000);

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
