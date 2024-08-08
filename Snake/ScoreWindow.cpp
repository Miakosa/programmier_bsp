#include "ScoreWindow.h"
#include <string>

// Constructor
ScoreWindow::ScoreWindow(int height, int width, int start_y, int start_x)
	// Call the base class constructor
	: Window(height, width, start_y, start_x)
{
}

// Display the score in the window
void ScoreWindow::show_score(int score)
{
	clear();
	draw_int(0, 0, score);
	refresh();
}
