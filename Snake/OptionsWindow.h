#include "Window.h"
#include "ColorManager.h"
#include <string>

class OptionsWindow : public Window {
private:
    ColorManager& color_manager;
    int& sleep_duration;
    std::vector<std::string> options;
    int selected_option;
    std::string player_name;

public:
    OptionsWindow(int height, int width, int start_y, int start_x, ColorManager& color_manager, int& sleep_duration);
    std::string get_player_name();
};
