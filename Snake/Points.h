#ifndef POINTSWINDOW
#define POINTSWINDOW

#include "Window.h"
#include <vector>
#include <string>

class Points : public Window {
private:
    std::vector<std::pair<std::string, int>> best_scores;
    const std::string filename = "highscores.json";

public:
    Points(int height, int width, int start_y, int start_x);
    void add_score(const std::string& player_name, int score);
    void show_high_scores();
    void save_scores() const;
    void load_scores();
};

#endif
