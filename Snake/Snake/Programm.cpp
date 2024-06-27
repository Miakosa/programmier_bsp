
#include <curses.h>

int main()
{
    initscr();                      // initialise Bib
   

    start_color();                  // activate Colorfunction
    init_pair(1, COLOR_BLUE, COLOR_WHITE);  // define a Color - Pair 1
    init_pair(2, COLOR_WHITE, COLOR_BLUE);
    bkgd(COLOR_PAIR(2));    // Color Main-Window set to ´Pair 2

    
    WINDOW* window = newwin(12, 12, 1, 1); // create Window 
    wbkgd(window, COLOR_PAIR(1));   // Color window set to Pair 1
    mvwprintw(window, 1, 1, "Hello"); 
    wrefresh(window); // window refresh

    printw("Hello World !!!");      // print that
    refresh();  // refresh main Window
    getch(); // wait for user input
    delwin(window); // delete window
    endwin();  // end 

    return 0;
}