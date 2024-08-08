#ifndef SCOREWINDOW
#define SCOREWINDOW

#include "Window.h"

class ScoreWindow : public Window
{
public:
	ScoreWindow(int height, int width, int start_y, int start_x);
	void show_score(int score);
};

#endif