#include <iostream>
#include "Window.h"


int main() {
	std::cout << " Create Window\n";

		Window* pWindow = new Window(); // Pointer
		bool running = true;
		while (running) {
			
			if (!pWindow->ProcessMessages())
			{
				std::cout << "Closing Window\n";
				running = false;
			}

			// "Render"
			//
			Sleep(10);
		}
		delete pWindow;

		return 0;
}
