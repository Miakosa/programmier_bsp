#include "Points.h"
#include <algorithm>
#include <fstream>
#include <sstream>

/* Constructor
* Initializes a window with specified dimensions and position
* Loads the scores from a Json file when object is created
*/
Points::Points(int height, int width, int start_y, int start_x)
    : Window(height, width, start_y, start_x) {
    load_scores();
}

/* Add a score to the high scores list
*  saves playername and score as parameters
*  sort the high score list in descending order and keeps only the top 3 scores
*  saves the updated high scores list to file
*/
void Points::add_score(const std::string& player_name, int score) {
    best_scores.push_back({ player_name, score });
    std::sort(best_scores.begin(), best_scores.end(), [](const auto& a, const auto& b) {
        return a.second > b.second;
        });
    if (best_scores.size() > 3) {
        best_scores.resize(3);
    }
    save_scores();  // Save scores after adding a new one
}

// Display the high scores in the window
void Points::show_high_scores() {
    clear();
    int max_x, max_y;
    getmaxyx(win, max_y, max_x);
    draw_text(1, (max_x - 11) / 2, "High Scores");

    for (size_t i = 0; i < best_scores.size(); i++) {
        std::string score_text = best_scores[i].first + ": " + std::to_string(best_scores[i].second);
        draw_text(3 + i, (max_x - score_text.size()) / 2, score_text.c_str());
    }
    refresh();
}

// save the high scores to file in JSON format
void Points::save_scores() const {
    std::ofstream file(filename);
    if (file.is_open()) {
        file << "{\n";
        file << "  \"highscores\": [\n";
        for (size_t i = 0; i < best_scores.size(); ++i) {
            file << "    {\n";
            file << "      \"name\": \"" << best_scores[i].first << "\",\n";
            file << "      \"score\": " << best_scores[i].second << "\n";
            file << "    }";
            if (i != best_scores.size() - 1) file << ",";
            file << "\n";
        }
        file << "  ]\n";
        file << "}\n";
        file.close();
    }
}

// load the high scores from file in JSON format
void Points::load_scores() {
    std::ifstream file(filename);
    if (file.is_open()) {
        std::string line;
        std::string name;
        int score;
        while (std::getline(file, line)) {
            // Remove the trailing comma and quote
            if (line.find("\"name\":") != std::string::npos) {
                name = line.substr(line.find(":") + 3);
                name = name.substr(0, name.size() - 2); 
            }
            if (line.find("\"score\":") != std::string::npos) {
                score = std::stoi(line.substr(line.find(":") + 2));
                best_scores.push_back({ name, score });
            }
        }
        file.close();
    }
    std::sort(best_scores.begin(), best_scores.end(), [](const auto& a, const auto& b) {
        return a.second > b.second;
        });
    if (best_scores.size() > 3) {
        best_scores.resize(3);
    }
}
